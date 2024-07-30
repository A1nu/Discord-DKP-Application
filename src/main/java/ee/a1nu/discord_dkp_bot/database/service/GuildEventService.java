package ee.a1nu.discord_dkp_bot.database.service;

import ee.a1nu.discord_dkp_bot.database.model.Encounter;
import ee.a1nu.discord_dkp_bot.database.model.GuildEvent;
import ee.a1nu.discord_dkp_bot.database.repository.GuildEventRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class GuildEventService {
    private final GuildEventRepository guildEventRepository;

    public GuildEventService(GuildEventRepository guildEventRepository) {
        this.guildEventRepository = guildEventRepository;
    }

    public GuildEvent getGuildEvent(Encounter encounter, OffsetDateTime start, OffsetDateTime end) {
        return guildEventRepository.findFirstByEncounterAndEventDateBetween(encounter, start, end);
    }
}
