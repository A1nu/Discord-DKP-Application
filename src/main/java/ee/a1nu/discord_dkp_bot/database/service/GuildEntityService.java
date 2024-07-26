package ee.a1nu.discord_dkp_bot.database.service;

import ee.a1nu.discord_dkp_bot.database.model.GuildEntity;
import ee.a1nu.discord_dkp_bot.database.repository.GuildEntityRepository;
import org.springframework.stereotype.Service;

@Service
public class GuildEntityService {
    private final GuildEntityRepository guildEntityRepository;

    public GuildEntityService(GuildEntityRepository guildEntityRepository) {
        this.guildEntityRepository = guildEntityRepository;
    }

    public GuildEntity getGuildEntity(Long guildId) {
        if (guildEntityRepository.existsBySnowflake(guildId)) {
            return guildEntityRepository.findBySnowflake(guildId);
        }
        return createGuildEntity(guildId);
    }

    private GuildEntity createGuildEntity(Long guildId) {
        GuildEntity guildEntity = new GuildEntity();
        guildEntity.setSnowflake(guildId);
        guildEntity.setCreatorSnowflake(0L);
        return guildEntityRepository.save(guildEntity);
    }

    public void updateGuild(GuildEntity guildEntity) {
        if (!guildEntity.isNew()) {
            guildEntity.setEditorSnowflake(0L);
            guildEntityRepository.save(guildEntity);
        }
    }
}
