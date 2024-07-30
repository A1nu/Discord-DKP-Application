package ee.a1nu.discord_dkp_bot.api.mapper;

import ee.a1nu.discord_dkp_bot.api.dto.EncounterDTO;
import ee.a1nu.discord_dkp_bot.api.dto.HourMinuteDTO;
import ee.a1nu.discord_dkp_bot.database.model.Encounter;
import ee.a1nu.discord_dkp_bot.database.model.EncounterSpawn;
import ee.a1nu.discord_dkp_bot.database.model.SpawnTime;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class EncounterSpawnMapper {


    public void mapDtoToEntity(EncounterDTO dto, Encounter encounter, Long userId) {
        if (encounter.getEncounterSpawns() == null) {
            encounter.setEncounterSpawns(new HashSet<>());
        }

        Set<EncounterSpawn> encounterSpawns = encounter.getEncounterSpawns();

        encounterSpawns.clear();

        if (!dto.scheduledEncounter()) {
            return;
        }

        if (dto.everyDay()) {
            encounterSpawns.add(generateEveryDayEncounterSpawn(encounter, dto.everyDaySpawns(), userId));
            return;
        }
        if (dto.onMonday()) {
            encounterSpawns.add(generateEncounterSpawn(DayOfWeek.MONDAY, encounter, dto.mondaySpawns(), userId));
        }
        if (dto.onTuesday()) {
            encounterSpawns.add(generateEncounterSpawn(DayOfWeek.TUESDAY, encounter, dto.tuesdaySpawns(), userId));
        }
        if (dto.onWednesday()) {
            encounterSpawns.add(generateEncounterSpawn(DayOfWeek.WEDNESDAY, encounter, dto.wednesdaySpawns(), userId));
        }
        if (dto.onThursday()) {
            encounterSpawns.add(generateEncounterSpawn(DayOfWeek.THURSDAY, encounter, dto.thursdaySpawns(), userId));
        }
        if (dto.onFriday()) {
            encounterSpawns.add(generateEncounterSpawn(DayOfWeek.FRIDAY, encounter, dto.fridaySpawns(), userId));
        }
        if (dto.onSaturday()) {
            encounterSpawns.add(generateEncounterSpawn(DayOfWeek.SATURDAY, encounter, dto.saturdaySpawns(), userId));
        }
        if (dto.onSunday()) {
            encounterSpawns.add(generateEncounterSpawn(DayOfWeek.SUNDAY, encounter, dto.sundaySpawns(), userId));
        }
    }

    private EncounterSpawn generateEveryDayEncounterSpawn(Encounter encounter, List<HourMinuteDTO> times, Long userId) {
        EncounterSpawn encounterSpawn = new EncounterSpawn();
        encounterSpawn.setEncounter(encounter);
        encounterSpawn.setCreatorSnowflake(userId);
        encounterSpawn.setEveryday(true);
        encounterSpawn.setEncounterTimeSet(generateTimeSet(encounterSpawn, times, userId));
        return encounterSpawn;
    }

    private EncounterSpawn generateEncounterSpawn(DayOfWeek day, Encounter encounter, List<HourMinuteDTO> times, Long userId) {
        EncounterSpawn encounterSpawn = new EncounterSpawn();
        encounterSpawn.setEncounter(encounter);
        encounterSpawn.setCreatorSnowflake(userId);
        encounterSpawn.setDayOfWeek(day);
        encounterSpawn.setEncounterTimeSet(generateTimeSet(encounterSpawn, times, userId));
        return encounterSpawn;
    }

    private Set<SpawnTime> generateTimeSet(EncounterSpawn encounterSpawn, List<HourMinuteDTO> times, Long userId) {
        Set<SpawnTime> spawnTimes = new HashSet<>();
        times.forEach(time -> {
            SpawnTime spawnTime = new SpawnTime();
            spawnTime.setCreatorSnowflake(userId);
            spawnTime.setEncounterSpawn(encounterSpawn);
            spawnTime.setTime(OffsetTime.of(time.hour(), time.minute(), 0, 0, ZoneOffset.ofHours(-3)));

            spawnTimes.add(spawnTime);
        });
        return spawnTimes;
    }
}
