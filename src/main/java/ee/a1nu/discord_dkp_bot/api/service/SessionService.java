package ee.a1nu.discord_dkp_bot.api.service;

import com.vaadin.flow.spring.security.AuthenticationContext;
import ee.a1nu.discord_dkp_bot.api.dto.DiscordUserDetails;
import ee.a1nu.discord_dkp_bot.api.mapper.DiscordUserDetailsMapper;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionService {
    private final AuthenticationContext authContext;
    private final DiscordUserDetailsMapper userDetailsMapper;

    public SessionService(AuthenticationContext authContext,
                          DiscordUserDetailsMapper userDetailsMapper) {
        this.authContext = authContext;
        this.userDetailsMapper = userDetailsMapper;
    }


    public Optional<DiscordUserDetails> getUserDetails() {
        return authContext.getAuthenticatedUser(OAuth2User.class).map(userDetailsMapper::map);
    }

    public Long getUserId() {
        if (getUserDetails().isPresent()) {
            return getUserDetails().get().id();
        }
        return null;
    }

}
