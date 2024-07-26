package ee.a1nu.discord_dkp_bot.api.dto;

import java.util.List;
import java.util.UUID;

public record ApplicationConfigurationDTO(
        UUID id,
        String administratorRoleSnowflake,
        String memberRoleSnowflake,
        String moderatorRoleSnowflake,
        boolean stashEnabled,
        boolean attendanceEnabled,
        int lootPretendingDaysDelay,
        String guildName,
        List<RoleDTO> roles
) {
}
