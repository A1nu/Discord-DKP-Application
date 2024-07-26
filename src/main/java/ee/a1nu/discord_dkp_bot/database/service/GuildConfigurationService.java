package ee.a1nu.discord_dkp_bot.database.service;

import ee.a1nu.discord_dkp_bot.database.model.GuildConfiguration;
import ee.a1nu.discord_dkp_bot.database.model.GuildEntity;
import ee.a1nu.discord_dkp_bot.database.repository.GuildConfigurationRepository;
import org.springframework.stereotype.Service;

@Service
public class GuildConfigurationService {
    private final GuildConfigurationRepository guildConfigurationRepository;

    public GuildConfigurationService(GuildConfigurationRepository guildConfigurationRepository) {
        this.guildConfigurationRepository = guildConfigurationRepository;
    }

    public GuildConfiguration getGuildConfiguration(Long guildId) {
        return guildConfigurationRepository.findByGuild_Snowflake(guildId);
    }

    public GuildConfiguration createGuildConfiguration(GuildEntity guild) {
        GuildConfiguration guildConfiguration = new GuildConfiguration();
        guildConfiguration.setCreatorSnowflake(0L);
        guildConfiguration.setGuild(guild);
        return guildConfigurationRepository.save(guildConfiguration);
    }
}
