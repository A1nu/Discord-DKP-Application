package ee.a1nu.discord_dkp_bot.api.util;

import discord4j.common.util.Snowflake;
import ee.a1nu.discord_dkp_bot.bot.service.DiscordBotService;
import org.springframework.stereotype.Service;

@Service
public class PermissionValidationService {

    private final DiscordBotService discordBotService;

    public PermissionValidationService(
            DiscordBotService discordBotService

    ) {
        this.discordBotService = discordBotService;
    }

    public boolean hasAdministrativePermission(Long guildId, Long memberId) {

        return discordBotService.isMemberAdministrator(Snowflake.of(memberId), Snowflake.of(guildId)) || discordBotService.isMemberHasAdministrativeRole(Snowflake.of(memberId), Snowflake.of(guildId));
    }
}
