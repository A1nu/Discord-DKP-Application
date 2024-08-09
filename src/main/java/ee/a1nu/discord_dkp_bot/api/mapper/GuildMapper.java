package ee.a1nu.discord_dkp_bot.api.mapper;

import discord4j.core.object.entity.Guild;
import discord4j.rest.util.Image;
import ee.a1nu.discord_dkp_bot.api.dto.DashboardGuildDTO;
import ee.a1nu.discord_dkp_bot.api.service.PermissionValidationService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
public class GuildMapper {
    private final PermissionValidationService permissionValidationService;

    public GuildMapper(PermissionValidationService permissionValidationService) {
        this.permissionValidationService = permissionValidationService;
    }

    public List<DashboardGuildDTO> guildDTO(Flux<Guild> guilds, Long memberId) {
        if (Boolean.TRUE.equals(guilds.hasElements().block())) {
            return guilds.map(g -> new DashboardGuildDTO(
                    g.getId().asString(),
                    g.getName(),
                    permissionValidationService.hasAdministrativePermission(g.getId().asLong(), memberId),
                    g.getIconUrl(Image.Format.PNG),
                    permissionValidationService.isGuildPremium(g.getId().asLong())
            )).collectList().block();
        }
        return List.of();
    }
}
