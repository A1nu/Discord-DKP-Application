package ee.a1nu.discord_dkp_bot.api.mapper;

import ee.a1nu.discord_dkp_bot.api.dto.ApplicationConfigurationDTO;
import ee.a1nu.discord_dkp_bot.api.dto.RoleDTO;
import ee.a1nu.discord_dkp_bot.api.util.Action;
import ee.a1nu.discord_dkp_bot.api.util.ChangeContext;
import ee.a1nu.discord_dkp_bot.bot.service.DiscordBotService;
import ee.a1nu.discord_dkp_bot.database.model.GuildConfiguration;
import ee.a1nu.discord_dkp_bot.database.model.GuildEntity;
import ee.a1nu.discord_dkp_bot.database.service.GuildConfigurationService;
import ee.a1nu.discord_dkp_bot.database.service.GuildEntityService;
import ee.a1nu.discord_dkp_bot.database.service.TransactionService;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class GuildConfigurationMapper {
    private final DiscordBotService discordBotService;
    private final GuildConfigurationService guildConfigurationService;
    private final GuildEntityService guildEntityService;
    private final TransactionService transactionService;

    public GuildConfigurationMapper(
            DiscordBotService discordBotService,
            GuildConfigurationService guildConfigurationService,
            GuildEntityService guildEntityService,
            TransactionService transactionService) {
        this.discordBotService = discordBotService;
        this.guildConfigurationService = guildConfigurationService;
        this.guildEntityService = guildEntityService;
        this.transactionService = transactionService;
    }

    public ApplicationConfigurationDTO mapApplicationConfiguration(Long guildId) {
        GuildConfiguration guildConfiguration = guildConfigurationService.getGuildConfiguration(guildId);
        if (guildConfiguration == null) {
            GuildEntity guildEntity = guildEntityService.getGuildEntity(guildId);
            guildConfiguration = guildConfigurationService.createGuildConfiguration(guildEntity);
            transactionService.saveTransaction(guildConfiguration.getCreatorSnowflake(), ChangeContext.CONFIGURATION, guildConfiguration.getId().toString(), Action.CREATE);
            guildEntity.setGuildConfiguration(guildConfiguration);
            guildEntityService.updateGuild(guildEntity);
        }
        List<RoleDTO> roles = discordBotService.getGuildRoles(guildId)
                .filter(role -> !role.isEveryone())
                .map(r -> new RoleDTO(r.getId().asString(), r.getName()))
                .collectSortedList(Comparator.comparing(RoleDTO::name))
                .block();

        return new ApplicationConfigurationDTO(
                guildConfiguration.getId(),
                String.valueOf(guildConfiguration.getAdministratorRoleSnowflake()),
                String.valueOf(guildConfiguration.getMemberRoleSnowflake()),
                String.valueOf(guildConfiguration.getModeratorRoleSnowflake()),
                guildConfiguration.isStashEnabled(),
                guildConfiguration.isAttendanceEnabled(),
                guildConfiguration.getLootPretendingDaysDelay(),
                discordBotService.getGuildName(guildId),
                roles
        );
    }

    public GuildConfiguration mapDtoToEntity(GuildConfiguration guildConfiguration, ApplicationConfigurationDTO configuration) {
        guildConfiguration.setAdministratorRoleSnowflake(Long.parseLong(configuration.administratorRoleSnowflake()));
        guildConfiguration.setMemberRoleSnowflake(Long.parseLong(configuration.memberRoleSnowflake()));
        guildConfiguration.setModeratorRoleSnowflake(Long.parseLong(configuration.moderatorRoleSnowflake()));
        guildConfiguration.setAttendanceEnabled(configuration.attendanceEnabled());
        guildConfiguration.setStashEnabled(configuration.stashEnabled());
        guildConfiguration.setLootPretendingDaysDelay(configuration.lootPretendingDaysDelay());
        return guildConfiguration;
    }
}
