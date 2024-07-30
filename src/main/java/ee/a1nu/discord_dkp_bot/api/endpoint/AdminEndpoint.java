package ee.a1nu.discord_dkp_bot.api.endpoint;

import com.vaadin.flow.router.AccessDeniedException;
import com.vaadin.hilla.Endpoint;
import com.vaadin.hilla.exception.EndpointException;
import ee.a1nu.discord_dkp_bot.api.dto.*;
import ee.a1nu.discord_dkp_bot.api.mapper.EncounterMapper;
import ee.a1nu.discord_dkp_bot.api.mapper.GuildConfigurationMapper;
import ee.a1nu.discord_dkp_bot.api.service.PermissionValidationService;
import ee.a1nu.discord_dkp_bot.api.service.SessionService;
import ee.a1nu.discord_dkp_bot.api.util.Action;
import ee.a1nu.discord_dkp_bot.api.util.ChangeContext;
import ee.a1nu.discord_dkp_bot.api.validator.ApplicationSettingsValidator;
import ee.a1nu.discord_dkp_bot.api.validator.EncounterValidator;
import ee.a1nu.discord_dkp_bot.database.model.Encounter;
import ee.a1nu.discord_dkp_bot.database.model.GuildConfiguration;
import ee.a1nu.discord_dkp_bot.database.model.GuildEntity;
import ee.a1nu.discord_dkp_bot.database.model.Transaction;
import ee.a1nu.discord_dkp_bot.database.service.EncounterService;
import ee.a1nu.discord_dkp_bot.database.service.GuildConfigurationService;
import ee.a1nu.discord_dkp_bot.database.service.GuildEntityService;
import ee.a1nu.discord_dkp_bot.database.service.TransactionService;
import jakarta.annotation.security.PermitAll;
import oshi.util.tuples.Pair;

import java.util.UUID;

@Endpoint
@PermitAll
public class AdminEndpoint {
    private final PermissionValidationService permissionValidationService;
    private final SessionService sessionService;
    private final GuildConfigurationMapper guildConfigurationMapper;
    private final ApplicationSettingsValidator applicationSettingsValidator;
    private final GuildConfigurationService guildConfigurationService;
    private final EncounterMapper encounterMapper;
    private final EncounterValidator encounterValidator;
    private final EncounterService encounterService;
    private final GuildEntityService guildEntityService;
    private final TransactionService transactionService;

    public AdminEndpoint(
            PermissionValidationService permissionValidationService,
            SessionService sessionService,
            GuildConfigurationMapper guildConfigurationMapper,
            ApplicationSettingsValidator applicationSettingsValidator,
            GuildConfigurationService guildConfigurationService,
            EncounterMapper encounterMapper,
            EncounterValidator encounterValidator, EncounterService encounterService, GuildEntityService guildEntityService, TransactionService transactionService) {
        this.permissionValidationService = permissionValidationService;
        this.sessionService = sessionService;
        this.guildConfigurationMapper = guildConfigurationMapper;
        this.applicationSettingsValidator = applicationSettingsValidator;
        this.guildConfigurationService = guildConfigurationService;
        this.encounterMapper = encounterMapper;
        this.encounterValidator = encounterValidator;
        this.encounterService = encounterService;
        this.guildEntityService = guildEntityService;
        this.transactionService = transactionService;
    }

    public ApplicationConfigurationDTO getGuildConfiguration(String guildId) throws EndpointException {
        if (!permissionValidationService.hasAdministrativePermission(Long.parseLong(guildId), sessionService.getUserId())) {
            throw new EndpointException(new AccessDeniedException());
        }
        return guildConfigurationMapper.mapApplicationConfiguration(Long.parseLong(guildId));
    }

    public ResponseDTO setGuildConfiguration(String guildId, ApplicationConfigurationDTO configuration) throws EndpointException {
        if (!permissionValidationService.hasAdministrativePermission(Long.parseLong(guildId), sessionService.getUserId())) {
            throw new EndpointException(new AccessDeniedException());
        }
        GuildConfiguration guildConfiguration = guildConfigurationService.getGuildConfiguration(Long.parseLong(guildId));
        Pair<Boolean, String> validateData = applicationSettingsValidator.isDataValid(guildConfiguration, configuration);

        if (!validateData.getA()) {
            return new ResponseDTO(ResponseType.ERROR.toString(), validateData.getB());
        }

        Transaction transaction = new Transaction();

        GuildConfiguration updatedGuildConfiguration = guildConfigurationMapper.mapDtoToEntity(guildConfiguration, configuration);

        updatedGuildConfiguration.setEditorSnowflake(sessionService.getUserId());
        transactionService.saveTransaction(
                guildConfiguration.getEditorSnowflake(),
                ChangeContext.CONFIGURATION,
                guildConfigurationService.save(guildConfiguration).getId().toString(),
                Action.UPDATE);
        return new ResponseDTO(ResponseType.SUCCESS.toString(), "");
    }

    public EncountersDataDTO getEncountersData(String guildId) throws EndpointException {
        if (!permissionValidationService.hasAdministrativePermission(Long.parseLong(guildId), sessionService.getUserId())) {
            throw new EndpointException(new AccessDeniedException());
        }

        GuildEntity guild = guildEntityService.getGuildEntity(Long.parseLong(guildId));

        return encounterMapper.mapToDataDto(guild);
    }

    public ResponseDTO saveEncounter(String guildId, EncounterDTO encounterDTO) throws EndpointException {

        if (!permissionValidationService.hasAdministrativePermission(Long.parseLong(guildId), sessionService.getUserId())) {
            throw new EndpointException(new AccessDeniedException());
        }

        Pair<Boolean, String> validateData = encounterValidator.isNewEncounterValid(encounterDTO, Long.parseLong(guildId));

        if (!validateData.getA()) {
            return new ResponseDTO(ResponseType.ERROR.toString(), validateData.getB());
        }


        try {
            Encounter encounter;

            if (encounterDTO.id().isEmpty()) {
                encounter = new Encounter();
            } else {
                encounter = encounterService.getEncounter(UUID.fromString(encounterDTO.id()));
            }

            transactionService.saveTransaction(
                    sessionService.getUserId(),
                    ChangeContext.ENCOUNTER,
                    encounterService.saveEncounter(
                            encounterMapper.mapDtoToEntity(
                                    encounterDTO,
                                    encounter,
                                    Long.parseLong(guildId),
                                    sessionService.getUserId()
                            )
                    ).getId().toString(),
                    encounterDTO.id().isEmpty() ? Action.CREATE : Action.UPDATE
            );


            return new ResponseDTO(ResponseType.SUCCESS.toString(), "");
        } catch (IllegalArgumentException e) {
            return new ResponseDTO(ResponseType.ERROR.toString(), e.getMessage());
        }
    }

    public ResponseDTO deleteEncounter(String guildId, String encounterId) throws EndpointException {
        if (!permissionValidationService.hasAdministrativePermission(Long.parseLong(guildId), sessionService.getUserId())) {
            throw new EndpointException(new AccessDeniedException());
        }
        transactionService.saveTransaction(sessionService.getUserId(), ChangeContext.ENCOUNTER, UUID.fromString(encounterId).toString(), Action.DELETE);
        encounterService.deleteEncounter(UUID.fromString(encounterId));
        return new ResponseDTO(ResponseType.SUCCESS.toString(), "");
    }
}
