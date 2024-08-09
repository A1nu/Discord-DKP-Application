package ee.a1nu.discord_dkp_bot.api.endpoint;

import com.vaadin.hilla.Endpoint;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Guild;
import ee.a1nu.discord_dkp_bot.api.dto.DashboardGuildDTO;
import ee.a1nu.discord_dkp_bot.api.mapper.GuildMapper;
import ee.a1nu.discord_dkp_bot.api.service.SessionService;
import ee.a1nu.discord_dkp_bot.bot.service.DiscordBotService;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Flux;

import java.util.List;

@Endpoint
@PermitAll
public class DashboardEndpoint {

    private final DiscordBotService botService;
    private final GuildMapper guildMapper;
    private final SessionService sessionService;
    @Value("${spring.security.oauth2.client.registration.discord.client-id}")
    private String clientId;

    public DashboardEndpoint(
            DiscordBotService botService,
            GuildMapper guildMapper,
            SessionService sessionService
    ) {
        this.botService = botService;
        this.guildMapper = guildMapper;
        this.sessionService = sessionService;
    }

    public List<DashboardGuildDTO> getDashboardGuildList() {
        Flux<Guild> botGuilds = botService.getBotGuilds(Snowflake.of(sessionService.getUserId()));

        return guildMapper.guildDTO(botGuilds, sessionService.getUserId());
    }

    public String getDiscordClientId() {
        return clientId;
    }
}
