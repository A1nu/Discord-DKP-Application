package ee.a1nu.discord_dkp_bot.api.dto;

import ee.a1nu.discord_dkp_bot.api.util.EventStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GuildEventDTO {
    private String id;
    private String name;
    private String encounterId;
    private EventStatus eventStatus;
    private LocalDateTime startTime;
    private byte amountOfAttendants;
    private boolean isPrime;
    private String imageUrl;

    public boolean isNew() {
        return id == null || id.isEmpty();
    }
}
