package ee.a1nu.discord_dkp_bot.api.dto;

import java.util.Set;

public record DiscordUserDetailsDTO(
        Long id,
        String username,
        String avatar,
        String globalName,
        Set<MemberPermissionsDTO> permissions
) {
}

