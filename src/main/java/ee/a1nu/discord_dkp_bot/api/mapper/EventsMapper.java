package ee.a1nu.discord_dkp_bot.api.mapper;

import ee.a1nu.discord_dkp_bot.api.dto.EventViewDataDTO;
import ee.a1nu.discord_dkp_bot.api.dto.GuildEventDTO;
import ee.a1nu.discord_dkp_bot.api.util.EventStatus;
import ee.a1nu.discord_dkp_bot.bot.service.DiscordBotService;
import ee.a1nu.discord_dkp_bot.database.model.Encounter;
import ee.a1nu.discord_dkp_bot.database.model.EncounterSpawn;
import ee.a1nu.discord_dkp_bot.database.service.EncounterService;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.*;

@Component
public class EventsMapper {
    private final EncounterService encounterService;
    private final DiscordBotService discordBotService;

    public EventsMapper(EncounterService encounterService, DiscordBotService discordBotService) {
        this.encounterService = encounterService;
        this.discordBotService = discordBotService;
    }

    private static void mapSpawnsToDto(OffsetDateTime start, List<GuildEventDTO> events, Encounter encounter, DayOfWeek dayOfWeek, EncounterSpawn encounterSpawn) {
        encounterSpawn.getEncounterTimeSet().forEach(ts -> {
            OffsetTime time = ts.getTime().withOffsetSameInstant(ZoneOffset.ofHours(-3));
            events.add(
                    GuildEventDTO.builder()
                            .id(null)
                            .eventStatus(EventStatus.TEMPORAL)
                            .encounterId(encounter.getId().toString())
                            .name(encounter.getName())
                            .startTime(start.plusDays(dayOfWeek.ordinal()).plusHours(time.getHour()).plusMinutes(time.getMinute()).withOffsetSameInstant(ZoneOffset.ofHours(+3)).toLocalDateTime())
                            .isPrime(encounter.isPrimeEncounter())
                            .imageUrl(encounter.getImageUrl())
                            .build()
            );
        });
    }

    public void mapEventsFromGuildEncounters(Long guildId, OffsetDateTime start, List<GuildEventDTO> events) {
        Set<Encounter> encounters = encounterService.getScheduledEncounters(guildId);

        encounters.forEach(encounter -> {
            if (encounter.isEveryDay()) {
                Arrays.stream(DayOfWeek.values()).forEach(dayOfWeek -> {
                    encounter.getEncounterSpawns().stream().findFirst().ifPresent(encounterSpawn -> {
                        mapSpawnsToDto(start, events, encounter, dayOfWeek, encounterSpawn);
                    });
                });
            } else {
                encounter.getEncounterSpawns().forEach(encounterSpawn -> {
                    mapSpawnsToDto(start, events, encounter, encounterSpawn.getDayOfWeek(), encounterSpawn);
                });
            }
        });
        events.sort(Comparator.comparing(GuildEventDTO::getStartTime));
    }

    public EventViewDataDTO mapEventViewData(long guildId, OffsetDateTime start) {
        OffsetDateTime startOfWeek = start.withOffsetSameInstant(ZoneOffset.ofHours(+3));
        List<GuildEventDTO> events = new ArrayList<>();
        mapEventsFromGuildEncounters(guildId, startOfWeek, events);
        return new EventViewDataDTO(
                discordBotService.getGuildName(guildId),
                startOfWeek.toLocalDateTime(),
                startOfWeek.plusDays(6).toLocalDateTime(),
                events
        );
    }
}
