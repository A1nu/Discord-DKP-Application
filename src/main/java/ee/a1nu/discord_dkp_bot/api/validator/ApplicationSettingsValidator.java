package ee.a1nu.discord_dkp_bot.api.validator;

import ee.a1nu.discord_dkp_bot.api.dto.ApplicationConfigurationDTO;
import ee.a1nu.discord_dkp_bot.database.model.GuildConfiguration;
import org.springframework.stereotype.Component;
import oshi.util.tuples.Pair;

import java.util.Objects;

@Component
public class ApplicationSettingsValidator {
    public Pair<Boolean, String> isDataValid(GuildConfiguration guildConfiguration, ApplicationConfigurationDTO applicationConfigurationDTO) {
        if (!guildConfiguration.getId().equals(applicationConfigurationDTO.id())) {
            return new Pair<>(false, "error.dataCorrupted");
        }

        if (
                Objects.equals(applicationConfigurationDTO.administratorRoleSnowflake(), "null") ||
                        Objects.equals(applicationConfigurationDTO.moderatorRoleSnowflake(), "null") ||
                        Objects.equals(applicationConfigurationDTO.memberRoleSnowflake(), "null") ||
                        applicationConfigurationDTO.lootPretendingDaysDelay() < 0
        ) {
            return new Pair<>(false, "error.invalidFieldsData");
        }

        if (
                Objects.equals(applicationConfigurationDTO.administratorRoleSnowflake(), applicationConfigurationDTO.moderatorRoleSnowflake()) ||
                        Objects.equals(applicationConfigurationDTO.administratorRoleSnowflake(), applicationConfigurationDTO.memberRoleSnowflake()) ||
                        Objects.equals(applicationConfigurationDTO.moderatorRoleSnowflake(), applicationConfigurationDTO.memberRoleSnowflake())
        ) {
            return new Pair<>(false, "error.fieldsMustBeDifferent");
        }

        return new Pair<>(true, "ok");
    }
}
