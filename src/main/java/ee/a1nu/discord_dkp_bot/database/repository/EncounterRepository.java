package ee.a1nu.discord_dkp_bot.database.repository;

import ee.a1nu.discord_dkp_bot.database.model.Encounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface EncounterRepository extends JpaRepository<Encounter, UUID> {
    boolean existsByGuild_SnowflakeAndName(Long guild_snowflake, String name);

    Set<Encounter> findAllByGuild_SnowflakeAndScheduledEncounter(Long guild_snowflake, boolean scheduledEncounter);
}
