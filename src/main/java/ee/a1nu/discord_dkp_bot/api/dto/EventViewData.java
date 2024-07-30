package ee.a1nu.discord_dkp_bot.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public record EventViewData(
        String guildName,
        LocalDateTime startDate,
        LocalDateTime endDate,
        List<GuildEventDTO> guildEvents
) {
}
