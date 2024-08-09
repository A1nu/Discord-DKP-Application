package ee.a1nu.discord_dkp_bot.api.dto;

import ee.a1nu.discord_dkp_bot.api.util.Permission;
import lombok.Data;

import java.util.Set;

@Data
public class MemberPermissionsDTO {
    private String guildId;
    private Set<Permission> permissions;
}
