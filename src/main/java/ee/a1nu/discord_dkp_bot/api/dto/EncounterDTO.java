package ee.a1nu.discord_dkp_bot.api.dto;


import java.util.List;

public record EncounterDTO(
        String id,
        String name,
        boolean scheduledEncounter,
        boolean primeEncounter,
        boolean everyDay,
        boolean onMonday,
        boolean onTuesday,
        boolean onWednesday,
        boolean onThursday,
        boolean onFriday,
        boolean onSaturday,
        boolean onSunday,
        byte weight,
        List<HourMinuteDTO> everyDaySpawns,
        List<HourMinuteDTO> mondaySpawns,
        List<HourMinuteDTO> tuesdaySpawns,
        List<HourMinuteDTO> wednesdaySpawns,
        List<HourMinuteDTO> thursdaySpawns,
        List<HourMinuteDTO> fridaySpawns,
        List<HourMinuteDTO> saturdaySpawns,
        List<HourMinuteDTO> sundaySpawns,
        ImageDTO imageData
) {
}
