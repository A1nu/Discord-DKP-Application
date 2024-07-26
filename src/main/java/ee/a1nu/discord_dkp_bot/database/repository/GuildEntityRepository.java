package ee.a1nu.discord_dkp_bot.database.repository;

import ee.a1nu.discord_dkp_bot.database.model.GuildEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GuildEntityRepository extends JpaRepository<GuildEntity, UUID> {
    boolean existsBySnowflake(Long snowflake);

    GuildEntity findBySnowflake(Long snowflake);
}