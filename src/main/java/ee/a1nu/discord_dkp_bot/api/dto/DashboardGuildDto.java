package ee.a1nu.discord_dkp_bot.api.dto;

public record DashboardGuildDto(
        String id,
        String name,
        boolean isConfigAllowed,
        java.util.Optional<String> image
) {
}
