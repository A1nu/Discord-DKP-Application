package ee.a1nu.discord_dkp_bot.api.service;

import discord4j.common.util.Snowflake;
import ee.a1nu.discord_dkp_bot.api.dto.MemberPermissionsDTO;
import ee.a1nu.discord_dkp_bot.api.util.Permission;
import ee.a1nu.discord_dkp_bot.bot.service.DiscordBotService;
import ee.a1nu.discord_dkp_bot.database.service.GuildEntityService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionValidationService {

    private final DiscordBotService discordBotService;
    private final GuildEntityService guildEntityService;

    public PermissionValidationService(
            DiscordBotService discordBotService, GuildEntityService guildEntityService) {
        this.discordBotService = discordBotService;
        this.guildEntityService = guildEntityService;
    }

    public boolean hasAdministrativePermission(Long guildId, Long memberId) {

        return discordBotService.isMemberAdministrator(Snowflake.of(memberId), Snowflake.of(guildId)) ||
                discordBotService.isMemberHasAdministrativeRoleAccess(Snowflake.of(memberId), Snowflake.of(guildId));
    }

    public boolean hasModerationPermissions(Long guildId, Long memberId) {
        return discordBotService.isMemberAdministrator(Snowflake.of(memberId), Snowflake.of(guildId)) ||
                discordBotService.isMemberHasModerationRoleAccess(Snowflake.of(memberId), Snowflake.of(guildId));
    }

    public boolean hasMemberPermission(Long guildId, Long memberId) {
        return discordBotService.isMemberAdministrator(Snowflake.of(memberId), Snowflake.of(guildId)) ||
                discordBotService.isMemberHasMemberRoleAccess(Snowflake.of(memberId), Snowflake.of(guildId));
    }

    public Set<MemberPermissionsDTO> mapUserPermissions(long userId) {
        return discordBotService.getBotGuilds(Snowflake.of(userId)).map(guild -> {
            MemberPermissionsDTO permissions = new MemberPermissionsDTO();
            Set<Permission> guildPermissions = new HashSet<>();
            Snowflake memberSnowflake = Snowflake.of(userId);
            if (discordBotService.isMemberAdministrator(memberSnowflake, guild.getId()) ||
                    discordBotService.isMemberHasAdministrativeRoleAccess(memberSnowflake, guild.getId())) {
                guildPermissions.add(Permission.ADMINISTRATOR);
                guildPermissions.add(Permission.MODERATOR);
                guildPermissions.add(Permission.MEMBER);
            } else if (discordBotService.isMemberHasModerationRoleAccess(memberSnowflake, guild.getId())) {
                guildPermissions.add(Permission.MODERATOR);
                guildPermissions.add(Permission.MEMBER);
            } else if (discordBotService.isMemberHasMemberRoleAccess(memberSnowflake, guild.getId())) {
                guildPermissions.add(Permission.MEMBER);
            }
            permissions.setGuildId(guild.getId().asString());
            permissions.setPermissions(guildPermissions);
            return permissions;
        }).collect(Collectors.toSet()).block();
    }

    public boolean isGuildPremium(Long guildId) {
        return guildEntityService.getGuildEntity(guildId).getPremium();
    }
}
