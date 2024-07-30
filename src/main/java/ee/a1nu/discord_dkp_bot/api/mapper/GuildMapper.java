package ee.a1nu.discord_dkp_bot.api.mapper;

import discord4j.core.object.entity.Guild;
import discord4j.rest.util.Image;
import ee.a1nu.discord_dkp_bot.api.dto.DashboardGuildDto;
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

    public List<DashboardGuildDto> guildDTO(Flux<Guild> guilds, Long memberId) {
        return guilds.map(g -> new DashboardGuildDto(
                g.getId().asString(),
                g.getName(),
                permissionValidationService.hasAdministrativePermission(g.getId().asLong(), memberId),
                g.getIconUrl(Image.Format.PNG)
        )).collectList().block();
    }
}
