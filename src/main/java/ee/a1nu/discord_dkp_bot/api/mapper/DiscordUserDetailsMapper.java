package ee.a1nu.discord_dkp_bot.api.mapper;

import ee.a1nu.discord_dkp_bot.api.dto.DiscordUserDetails;
import ee.a1nu.discord_dkp_bot.api.service.PermissionValidationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DiscordUserDetailsMapper {

    private static String discordCdnUrl;
    private final PermissionValidationService permissionValidationService;

    public DiscordUserDetailsMapper(PermissionValidationService permissionValidationService) {
        this.permissionValidationService = permissionValidationService;
    }

    @Value("${discord.cdnUrl}")
    public void setDiscordCdnUrl(String value) {
        discordCdnUrl = value;
    }

    public DiscordUserDetails map(OAuth2User oAuth2User) {
        return new DiscordUserDetails(
                Long.parseLong(oAuth2User.getAttributes().get("id").toString()),
                oAuth2User.getAttributes().get("username").toString(),
                buildAvatarUrl(oAuth2User.getAttributes()),
                oAuth2User.getAttributes().get("global_name").toString(),
                permissionValidationService.mapUserPermissions(Long.parseLong(oAuth2User.getAttributes().get("id").toString()))
        );
    }

    private String buildAvatarUrl(Map<String, Object> attributes) {
        return discordCdnUrl + "avatars/" + attributes.get("id").toString() + "/" + attributes.get("avatar").toString() + ".png";
    }
}
