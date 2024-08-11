package ee.a1nu.discord_dkp_bot.api.endpoint;

import com.vaadin.flow.router.AccessDeniedException;
import com.vaadin.hilla.Endpoint;
import com.vaadin.hilla.exception.EndpointException;
import ee.a1nu.discord_dkp_bot.api.dto.*;
import ee.a1nu.discord_dkp_bot.api.mapper.EventsMapper;
import ee.a1nu.discord_dkp_bot.api.mapper.MemberMapper;
import ee.a1nu.discord_dkp_bot.api.service.PermissionValidationService;
import ee.a1nu.discord_dkp_bot.api.service.SessionService;
import ee.a1nu.discord_dkp_bot.api.util.Action;
import ee.a1nu.discord_dkp_bot.api.util.ChangeContext;
import ee.a1nu.discord_dkp_bot.database.service.GuildEventService;
import ee.a1nu.discord_dkp_bot.database.service.TransactionService;
import jakarta.annotation.security.PermitAll;

import java.time.OffsetDateTime;
import java.util.UUID;

@Endpoint
@PermitAll
public class EventsEndpoint {
    private final PermissionValidationService permissionValidationService;
    private final SessionService sessionService;
    private final EventsMapper eventsMapper;
    private final MemberMapper memberMapper;
    private final TransactionService transactionService;
    private final GuildEventService guildEventService;

    public EventsEndpoint(
            PermissionValidationService permissionValidationService,
            SessionService sessionService, EventsMapper eventsMapper,
            MemberMapper memberMapper,
            TransactionService transactionService, GuildEventService guildEventService) {
        this.permissionValidationService = permissionValidationService;
        this.sessionService = sessionService;
        this.eventsMapper = eventsMapper;
        this.memberMapper = memberMapper;
        this.transactionService = transactionService;
        this.guildEventService = guildEventService;
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

    public ResponseDTO saveEvent(String guildId, GuildEventDTO event) throws EndpointException {
        if (!permissionValidationService.isGuildPremium(Long.parseLong(guildId))) {
            throw new EndpointException(new AccessDeniedException());
        }

        if (!permissionValidationService.hasModerationPermissions(Long.parseLong(guildId), sessionService.getUserId())) {
            throw new EndpointException(new AccessDeniedException());
        }

        transactionService.saveTransaction(
                sessionService.getUserId(),
                ChangeContext.EVENT,
                guildEventService.saveGuildEvent(
                        eventsMapper.mapDtoToGuildEvent(event, Long.parseLong(guildId), sessionService.getUserId())
                ).getId().toString(),
                event.getId().isEmpty() ? Action.CREATE : Action.UPDATE
        );

        return new ResponseDTO(ResponseType.SUCCESS.toString(), "");
    }

    public ResponseDTO deleteEvent(String guildId, String eventId) throws EndpointException {
        if (!permissionValidationService.isGuildPremium(Long.parseLong(guildId))) {
            throw new EndpointException(new AccessDeniedException());
        }

        if (!permissionValidationService.hasModerationPermissions(Long.parseLong(guildId), sessionService.getUserId())) {
            throw new EndpointException(new AccessDeniedException());
        }

        guildEventService.removeGuildEvent(guildId, UUID.fromString(eventId));
        transactionService.saveTransaction(sessionService.getUserId(), ChangeContext.EVENT, eventId, Action.DELETE);

        return new ResponseDTO(ResponseType.SUCCESS.toString(), "");
    }
}
