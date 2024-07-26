package ee.a1nu.discord_dkp_bot.database.repository;

import ee.a1nu.discord_dkp_bot.database.model.GuildConfiguration;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GuildConfigurationRepository extends JpaRepository<GuildConfiguration, UUID> {
    boolean existsByGuild_Snowflake(Long guildId);

    @Nullable
    GuildConfiguration findByGuild_Snowflake(Long guildId);
}