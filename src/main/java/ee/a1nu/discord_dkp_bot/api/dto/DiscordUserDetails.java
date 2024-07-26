package ee.a1nu.discord_dkp_bot.api.dto;

public record DiscordUserDetails(
        Long id,
        String username,
        String avatar,
        String globalName
) {
}

