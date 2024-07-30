package ee.a1nu.discord_dkp_bot.api.endpoint;

import com.vaadin.flow.router.AccessDeniedException;
import com.vaadin.hilla.Endpoint;
import com.vaadin.hilla.exception.EndpointException;
import ee.a1nu.discord_dkp_bot.api.dto.EventViewData;
import ee.a1nu.discord_dkp_bot.api.mapper.EventsMapper;
import ee.a1nu.discord_dkp_bot.api.service.PermissionValidationService;
import ee.a1nu.discord_dkp_bot.api.service.SessionService;
import jakarta.annotation.security.PermitAll;

import java.time.OffsetDateTime;

@Endpoint
@PermitAll
public class EventsEndpoint {
    private final PermissionValidationService permissionValidationService;
    private final SessionService sessionService;
    private final EventsMapper eventsMapper;

    public EventsEndpoint(PermissionValidationService permissionValidationService, SessionService sessionService, EventsMapper eventsMapper) {
        this.permissionValidationService = permissionValidationService;
        this.sessionService = sessionService;
        this.eventsMapper = eventsMapper;
    }

    public EventViewData getGuildEventsForWeek(String guildId, OffsetDateTime start) {
        if (!permissionValidationService.hasMemberPermission(Long.parseLong(guildId), sessionService.getUserId())) {
            throw new EndpointException(new AccessDeniedException());
        }


        return eventsMapper.mapEventViewData(Long.parseLong(guildId), start);
    }
}
