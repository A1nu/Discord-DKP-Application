package ee.a1nu.discord_dkp_bot.api.endpoint;

import com.vaadin.flow.router.AccessDeniedException;
import com.vaadin.hilla.Endpoint;
import com.vaadin.hilla.exception.EndpointException;
import ee.a1nu.discord_dkp_bot.api.dto.EventViewDataDTO;
import ee.a1nu.discord_dkp_bot.api.dto.EventViewModerationDataDTO;
import ee.a1nu.discord_dkp_bot.api.mapper.EventsMapper;
import ee.a1nu.discord_dkp_bot.api.mapper.MemberMapper;
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
    private final MemberMapper memberMapper;

    public EventsEndpoint(
            PermissionValidationService permissionValidationService,
            SessionService sessionService, EventsMapper eventsMapper,
            MemberMapper memberMapper
    ) {
        this.permissionValidationService = permissionValidationService;
        this.sessionService = sessionService;
        this.eventsMapper = eventsMapper;
        this.memberMapper = memberMapper;
    }

    public EventViewDataDTO getGuildEventsForWeek(String guildId, OffsetDateTime start) throws EndpointException {
        if (!permissionValidationService.isGuildPremium(Long.parseLong(guildId))) {
            throw new EndpointException(new AccessDeniedException());
        }

        if (!permissionValidationService.hasMemberPermission(Long.parseLong(guildId), sessionService.getUserId())) {
            throw new EndpointException(new AccessDeniedException());
        }
        return eventsMapper.mapEventViewData(Long.parseLong(guildId), start);
    }

    public EventViewModerationDataDTO getEventViewModerationData(String guildId) throws EndpointException {
        if (!permissionValidationService.isGuildPremium(Long.parseLong(guildId))) {
            throw new EndpointException(new AccessDeniedException());
        }

        if (!permissionValidationService.hasModerationPermissions(Long.parseLong(guildId), sessionService.getUserId())) {
            throw new EndpointException(new AccessDeniedException());
        }

        return memberMapper.mapEventViewModerationData(Long.parseLong(guildId));
    }
}
