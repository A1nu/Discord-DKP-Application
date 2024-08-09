package ee.a1nu.discord_dkp_bot.api.mapper;

import ee.a1nu.discord_dkp_bot.api.dto.EncounterDTO;
import ee.a1nu.discord_dkp_bot.api.dto.EncountersDataDTO;
import ee.a1nu.discord_dkp_bot.api.dto.HourMinuteDTO;
import ee.a1nu.discord_dkp_bot.api.dto.ImageDTO;
import ee.a1nu.discord_dkp_bot.api.service.ImageService;
import ee.a1nu.discord_dkp_bot.bot.service.DiscordBotService;
import ee.a1nu.discord_dkp_bot.database.model.*;
import ee.a1nu.discord_dkp_bot.database.service.GuildEntityService;
import ee.a1nu.discord_dkp_bot.database.service.WeightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class EncounterMapper {
    private static final Logger log = LoggerFactory.getLogger(EncounterMapper.class);
    private final DiscordBotService discordBotService;
    private final WeightService weightService;
    private final GuildEntityService guildEntityService;
    private final EncounterSpawnMapper encounterSpawnMapper;
    private final ImageService imageService;

    public EncounterMapper(DiscordBotService discordBotService, WeightService weightService, GuildEntityService guildEntityService, EncounterSpawnMapper encounterSpawnMapper, ImageService imageService) {
        this.discordBotService = discordBotService;
        this.weightService = weightService;
        this.guildEntityService = guildEntityService;
        this.encounterSpawnMapper = encounterSpawnMapper;
        this.imageService = imageService;
    }

    public EncountersDataDTO mapToDataDto(GuildEntity guild) {
        return new EncountersDataDTO(
                discordBotService.getGuildName(guild.getSnowflake()),
                weightService.getWeights().stream().map(Weight::getWeight).toList(),
                mapGuildEncounters(guild)
        );
    }

    private List<EncounterDTO> mapGuildEncounters(GuildEntity guild) {
        Set<Encounter> encounters = guild.getGuildEncounters();

        if (encounters == null || encounters.isEmpty()) {
            return List.of();
        }

        return encounters.stream().map(
                encounter -> {
                    boolean scheduled = encounter.isScheduledEncounter();
                    boolean isEveryDay = encounter.isEveryDay();

                    if (scheduled && isEveryDay) {
                        return mapEverydayEncounterToDto(encounter);
                    }

                    return mapEncounterToDtoByDays(encounter);
                }
        ).sorted(Comparator.comparing(EncounterDTO::name)).toList();
    }

    private EncounterDTO mapEncounterToDtoByDays(Encounter encounter) {
        Optional<EncounterSpawn> mondaySpawn = encounter.getEncounterSpawns().stream().filter(encounterSpawn -> encounterSpawn.getDayOfWeek().equals(DayOfWeek.MONDAY)).findFirst();
        Optional<EncounterSpawn> tuesdaySpawn = encounter.getEncounterSpawns().stream().filter(encounterSpawn -> encounterSpawn.getDayOfWeek().equals(DayOfWeek.TUESDAY)).findFirst();
        Optional<EncounterSpawn> wednesdaySpawn = encounter.getEncounterSpawns().stream().filter(encounterSpawn -> encounterSpawn.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)).findFirst();
        Optional<EncounterSpawn> thursdaySpawn = encounter.getEncounterSpawns().stream().filter(encounterSpawn -> encounterSpawn.getDayOfWeek().equals(DayOfWeek.THURSDAY)).findFirst();
        Optional<EncounterSpawn> fridaySpawn = encounter.getEncounterSpawns().stream().filter(encounterSpawn -> encounterSpawn.getDayOfWeek().equals(DayOfWeek.FRIDAY)).findFirst();
        Optional<EncounterSpawn> saturdaySpawn = encounter.getEncounterSpawns().stream().filter(encounterSpawn -> encounterSpawn.getDayOfWeek().equals(DayOfWeek.SATURDAY)).findFirst();
        Optional<EncounterSpawn> sundaySpawn = encounter.getEncounterSpawns().stream().filter(encounterSpawn -> encounterSpawn.getDayOfWeek().equals(DayOfWeek.SUNDAY)).findFirst();

        return new EncounterDTO(
                encounter.getId().toString(),
                encounter.getName(),
                encounter.isScheduledEncounter(),
                encounter.isPrimeEncounter(),
                false,
                mondaySpawn.isPresent() && !mondaySpawn.get().getEncounterTimeSet().isEmpty(),
                tuesdaySpawn.isPresent() && !tuesdaySpawn.get().getEncounterTimeSet().isEmpty(),
                wednesdaySpawn.isPresent() && !wednesdaySpawn.get().getEncounterTimeSet().isEmpty(),
                thursdaySpawn.isPresent() && !thursdaySpawn.get().getEncounterTimeSet().isEmpty(),
                fridaySpawn.isPresent() && !fridaySpawn.get().getEncounterTimeSet().isEmpty(),
                saturdaySpawn.isPresent() && !saturdaySpawn.get().getEncounterTimeSet().isEmpty(),
                sundaySpawn.isPresent() && !sundaySpawn.get().getEncounterTimeSet().isEmpty(),
                encounter.getWeight().getWeight(),
                List.of(),
                mapSpawnTimesToDto(mondaySpawn),
                mapSpawnTimesToDto(tuesdaySpawn),
                mapSpawnTimesToDto(wednesdaySpawn),
                mapSpawnTimesToDto(thursdaySpawn),
                mapSpawnTimesToDto(fridaySpawn),
                mapSpawnTimesToDto(saturdaySpawn),
                mapSpawnTimesToDto(sundaySpawn),
                mapImageData(encounter)
        );
    }

    private EncounterDTO mapEverydayEncounterToDto(Encounter encounter) {
        return new EncounterDTO(
                encounter.getId().toString(),
                encounter.getName(),
                encounter.isScheduledEncounter(),
                encounter.isPrimeEncounter(),
                encounter.isEveryDay() && encounter.isScheduledEncounter(),
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                encounter.getWeight().getWeight(),
                mapSpawnTimesToDto(encounter.getEncounterSpawns()),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                mapImageData(encounter)
        );
    }

    private ImageDTO mapImageData(Encounter encounter) {
        if (encounter.getImageData() == null) {
            return null;
        }

        ImageData imageData = encounter.getImageData();

        try {
            return new ImageDTO(
                    imageData.getId().toString(),
                    imageService.getImageAsBase64(imageData.getName(), encounter.getGuild().getSnowflake())
            );
        } catch (IOException e) {
            return null;
        }
    }

    private List<HourMinuteDTO> mapSpawnTimesToDto(Set<EncounterSpawn> encounterSpawns) {
        EncounterSpawn encounterSpawn = encounterSpawns.stream().findFirst().orElse(null);

        if (encounterSpawn == null || encounterSpawns.size() > 1) {
            return List.of();
        }

        return encounterSpawn.getEncounterTimeSet()
                .stream()
                .map(ts ->
                        new HourMinuteDTO((byte) ts.getTime().withOffsetSameInstant(ZoneOffset.ofHours(-3)).getHour(), (byte) ts.getTime().withOffsetSameInstant(ZoneOffset.ofHours(-3)).getMinute())
                ).sorted(Comparator.comparing(HourMinuteDTO::hour))
                .toList();
    }

    private List<HourMinuteDTO> mapSpawnTimesToDto(Optional<EncounterSpawn> encounterSpawn) {
        if (encounterSpawn.isEmpty() || encounterSpawn.get().getEncounterTimeSet().isEmpty()) {
            return List.of();
        }

        return encounterSpawn.get().getEncounterTimeSet()
                .stream()
                .map(ts ->
                        new HourMinuteDTO((byte) ts.getTime().withOffsetSameInstant(ZoneOffset.ofHours(-3)).getHour(), (byte) ts.getTime().withOffsetSameInstant(ZoneOffset.ofHours(-3)).getMinute())
                )
                .toList();
    }

    public Encounter mapDtoToEntity(EncounterDTO dto, Encounter encounter, long guildId, Long userId) {
        Weight weight = weightService.getWeight(dto.weight());

        if (encounter.isNew()) {
            encounter.setCreatorSnowflake(userId);
            encounter.setGuild(guildEntityService.getGuildEntity(guildId));
        } else {
            encounter.setEditorSnowflake(userId);
        }
        if (dto.primeEncounter()) {
            encounter.setWeight(weight);
        } else {
            encounter.setWeight(weightService.getWeight((byte) 0));
        }

        if (dto.imageData() != null && dto.imageData().imageData() != null) {
            if (encounter.getImageData() == null || !encounter.getImageData().getId().toString().equals(dto.imageData().id())) {
                try {
                    String fileName = imageService.saveBase64ImageToStorage(dto.imageData(), guildId);
                    ImageData imageData = new ImageData();
                    imageData.setCreatorSnowflake(userId);
                    imageData.setName(fileName);
                    imageData.setGuild(guildEntityService.getGuildEntity(guildId));
                    imageData.setVersion(1L);
                    encounter.setImageData(imageData);
                } catch (IOException e) {
                    log.error("Error while saving image to storage", e);
                }
            }
        } else {
            encounter.setImageData(null);
        }
        encounterSpawnMapper.mapDtoToEntity(dto, encounter, userId);
        encounter.setScheduledEncounter(dto.scheduledEncounter());
        encounter.setName(dto.name());
        encounter.setPrimeEncounter(dto.primeEncounter());
        return encounter;
    }
}
