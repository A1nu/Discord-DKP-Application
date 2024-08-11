package ee.a1nu.discord_dkp_bot.database.repository;

import ee.a1nu.discord_dkp_bot.database.model.Encounter;
import ee.a1nu.discord_dkp_bot.database.model.GuildEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Repository
public interface GuildEventRepository extends JpaRepository<GuildEvent, UUID> {
    Set<GuildEvent> findAllByEncounterAndEventDateBetween(Encounter encounter, OffsetDateTime start, OffsetDateTime end);
}
