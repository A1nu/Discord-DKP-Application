package ee.a1nu.discord_dkp_bot.api.endpoint;

import com.vaadin.flow.router.AccessDeniedException;
import com.vaadin.hilla.Endpoint;
import com.vaadin.hilla.exception.EndpointException;
import ee.a1nu.discord_dkp_bot.api.dto.ApplicationConfigurationDTO;
import ee.a1nu.discord_dkp_bot.api.dto.ResponseDTO;
import ee.a1nu.discord_dkp_bot.api.dto.ResponseType;
import ee.a1nu.discord_dkp_bot.api.mapper.GuildConfigurationMapper;
import ee.a1nu.discord_dkp_bot.api.service.SessionService;
import ee.a1nu.discord_dkp_bot.api.util.PermissionValidationService;
import ee.a1nu.discord_dkp_bot.api.validator.ApplicationSettingsValidator;
import ee.a1nu.discord_dkp_bot.database.model.GuildConfiguration;
import ee.a1nu.discord_dkp_bot.database.repository.GuildConfigurationRepository;
import ee.a1nu.discord_dkp_bot.database.service.GuildConfigurationService;
import jakarta.annotation.security.PermitAll;
import oshi.util.tuples.Pair;

@Endpoint
@PermitAll
public class AdminEndpoint {
    private final PermissionValidationService permissionValidationService;
    private final SessionService sessionService;
    private final GuildConfigurationMapper guildConfigurationMapper;
    private final ApplicationSettingsValidator applicationSettingsValidator;
    private final GuildConfigurationRepository guildConfigurationRepository;
    private final GuildConfigurationService guildConfigurationService;

    public AdminEndpoint(
            PermissionValidationService permissionValidationService,
            SessionService sessionService,
            GuildConfigurationMapper guildConfigurationMapper,
            ApplicationSettingsValidator applicationSettingsValidator,
            GuildConfigurationRepository guildConfigurationRepository, GuildConfigurationService guildConfigurationService) {
        this.permissionValidationService = permissionValidationService;
        this.sessionService = sessionService;
        this.guildConfigurationMapper = guildConfigurationMapper;
        this.applicationSettingsValidator = applicationSettingsValidator;
        this.guildConfigurationRepository = guildConfigurationRepository;
        this.guildConfigurationService = guildConfigurationService;
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

        GuildConfiguration updatedGuildConfiguration = guildConfigurationMapper.mapDtoToEntity(guildConfiguration, configuration);

        updatedGuildConfiguration.setEditorSnowflake(sessionService.getUserId());
        guildConfigurationRepository.save(guildConfiguration);

        return new ResponseDTO(ResponseType.SUCCESS.toString(), "");
    }
}
