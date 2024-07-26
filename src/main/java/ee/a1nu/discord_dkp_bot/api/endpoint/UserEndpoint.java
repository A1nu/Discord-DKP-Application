package ee.a1nu.discord_dkp_bot.api.endpoint;

import com.vaadin.hilla.Endpoint;
import ee.a1nu.discord_dkp_bot.api.dto.DiscordUserDetails;
import ee.a1nu.discord_dkp_bot.api.service.SessionService;
import jakarta.annotation.security.PermitAll;

import java.util.Optional;

@Endpoint
@PermitAll
public class UserEndpoint {

    private final SessionService sessionService;

    public UserEndpoint(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public Optional<DiscordUserDetails> getAuthenticatedUser() {
        return sessionService.getUserDetails();
    }

}
