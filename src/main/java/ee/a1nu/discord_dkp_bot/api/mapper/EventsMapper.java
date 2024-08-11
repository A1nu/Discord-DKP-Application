package ee.a1nu.discord_dkp_bot.api.mapper;

import ee.a1nu.discord_dkp_bot.api.dto.EventViewDataDTO;
import ee.a1nu.discord_dkp_bot.api.dto.GuildEventDTO;
import ee.a1nu.discord_dkp_bot.api.util.EventStatus;
import ee.a1nu.discord_dkp_bot.bot.service.DiscordBotService;
import ee.a1nu.discord_dkp_bot.database.model.Encounter;
import ee.a1nu.discord_dkp_bot.database.model.EncounterSpawn;
import ee.a1nu.discord_dkp_bot.database.model.GuildEntity;
import ee.a1nu.discord_dkp_bot.database.model.GuildEvent;
import ee.a1nu.discord_dkp_bot.database.repository.GuildEventRepository;
import ee.a1nu.discord_dkp_bot.database.service.EncounterService;
import ee.a1nu.discord_dkp_bot.database.service.GuildEntityService;
import ee.a1nu.discord_dkp_bot.database.service.GuildEventService;
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
    private final GuildEntityService guildEntityService;
    private final GuildEventService guildEventService;

    public EventsMapper(EncounterService encounterService, DiscordBotService discordBotService, GuildEntityService guildEntityService, GuildEventService guildEventService, GuildEventRepository guildEventRepository) {
        this.encounterService = encounterService;
        this.discordBotService = discordBotService;
        this.guildEntityService = guildEntityService;
        this.guildEventService = guildEventService;
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
        Set<Encounter> scheduledEncounters = encounterService.getScheduledEncounters(guildId);
        Set<Encounter> optionalEncounters = encounterService.getOptionalEncounters(guildId);

        scheduledEncounters.forEach(encounter -> {
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

        optionalEncounters.forEach(encounter -> {
            Set<GuildEvent> guildEvents = guildEventService.getGuildEvents(encounter, start.withOffsetSameLocal(ZoneOffset.ofHours(-3)), start.plusWeeks(1L).withOffsetSameLocal(ZoneOffset.ofHours(-3)));
            if (!guildEvents.isEmpty()) {
                guildEvents.forEach(event -> {
                    events.add(
                            GuildEventDTO.builder()
                                    .id(event.getId().toString())
                                    .name(event.getEncounter().getName())
                                    .encounterId(event.getEncounter().getId().toString())
                                    .eventStatus(event.getEventStatus())
                                    .startTime(event.getEventDate().toLocalDateTime())
                                    .amountOfAttendants((byte) event.getEventAttendance().size())
                                    .isPrime(event.getEncounter().isPrimeEncounter())
                                    .imageUrl(event.getEncounter().getImageUrl())
                                    .build()
                    );
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

    public GuildEvent mapDtoToGuildEvent(GuildEventDTO dto, Long guildId, Long memberId) {
        GuildEntity guild = guildEntityService.getGuildEntity(guildId);
        Encounter encounter = encounterService.getEncounter(UUID.fromString(dto.getEncounterId()));
        GuildEvent guildEvent;

        if (dto.isNew()) {
            guildEvent = new GuildEvent();
            guildEvent.setCreatorSnowflake(memberId);
        } else {
            guildEvent = guildEventService.getGuildEvent(UUID.fromString(dto.getId()));
            guildEvent.setEditorSnowflake(memberId);
        }
        guildEvent.setEncounter(encounter);
        guildEvent.setGuild(guild);
        guildEvent.setEventDate(OffsetDateTime.of(dto.getStartTime(), ZoneOffset.ofHours(-3)));
        guildEvent.setEventStatus(EventStatus.CREATED);

        return guildEvent;
    }
}
