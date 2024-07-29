package ee.a1nu.discord_dkp_bot.api.validator;

import ee.a1nu.discord_dkp_bot.api.dto.EncounterDTO;
import ee.a1nu.discord_dkp_bot.database.service.EncounterService;
import org.springframework.stereotype.Component;
import oshi.util.tuples.Pair;

@Component
public class EncounterValidator {
    private final EncounterService encounterService;

    public EncounterValidator(EncounterService encounterService) {
        this.encounterService = encounterService;
    }

    public Pair<Boolean, String> isNewEncounterValid(EncounterDTO encounter, Long guildId) {
        if (isEncounterNew(encounter) && isDuplicated(encounter, guildId)) {
            return new Pair<>(false, "error.encounter.duplicatedName");
        }

        if (encounter.name().isEmpty()) {
            return new Pair<>(false, "error.encounter.emptyName");
        }

        if (!isScheduledEncounterContainsTime(encounter)) {
            return new Pair<>(false, "error.encounter.notSpecifiedTimeForScheduledEncounter");
        }
        return new Pair<>(true, "ok");
    }

    private boolean isScheduledEncounterContainsTime(EncounterDTO encounter) {
        if (encounter.scheduledEncounter()) {
            if (encounter.everyDay() && encounter.everyDaySpawns().isEmpty()) {
                return false;
            }
            if (encounter.onMonday() && encounter.mondaySpawns().isEmpty()) {
                return false;
            }
            if (encounter.onTuesday() && encounter.tuesdaySpawns().isEmpty()) {
                return false;
            }
            if (encounter.onWednesday() && encounter.wednesdaySpawns().isEmpty()) {
                return false;
            }
            if (encounter.onThursday() && encounter.thursdaySpawns().isEmpty()) {
                return false;
            }
            if (encounter.onFriday() && encounter.fridaySpawns().isEmpty()) {
                return false;
            }
            if (encounter.onSaturday() && encounter.saturdaySpawns().isEmpty()) {
                return false;
            }
            return !encounter.onSunday() || !encounter.sundaySpawns().isEmpty();
        }
        return true;
    }

    private boolean isEncounterNew(EncounterDTO encounter) {
        return encounter.id() == null || encounter.id().isEmpty();
    }

    private boolean isDuplicated(EncounterDTO encounter, Long guildId) {
        return encounterService.isExistByName(encounter, guildId);
    }
}
