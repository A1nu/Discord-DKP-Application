package ee.a1nu.discord_dkp_bot.database.service;

import ee.a1nu.discord_dkp_bot.database.model.Encounter;
import ee.a1nu.discord_dkp_bot.database.model.GuildEvent;
import ee.a1nu.discord_dkp_bot.database.repository.GuildEventRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Service
public class GuildEventService {
    private final GuildEventRepository guildEventRepository;

    public GuildEventService(GuildEventRepository guildEventRepository) {
        this.guildEventRepository = guildEventRepository;
    }

    public Set<GuildEvent> getGuildEvents(Encounter encounter, OffsetDateTime start, OffsetDateTime end) {
        return guildEventRepository.findAllByEncounterAndEventDateBetween(encounter, start, end);
    }

    public GuildEvent getGuildEvent(UUID guildEventId) {
        return guildEventRepository.findById(guildEventId).orElse(new GuildEvent());
    }

    public GuildEvent saveGuildEvent(GuildEvent guildEvent) {
        return guildEventRepository.save(guildEvent);
    }

    public void removeGuildEvent(String guildId, UUID uuid) {
        GuildEvent guildEvent = guildEventRepository.findById(uuid).orElse(new GuildEvent());
        if (guildEvent.getGuild().getSnowflake().equals(Long.parseLong(guildId))) {
            guildEventRepository.delete(guildEvent);
        }
    }
}
