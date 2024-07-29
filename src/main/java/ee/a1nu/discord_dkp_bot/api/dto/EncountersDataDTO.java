package ee.a1nu.discord_dkp_bot.api.dto;


import java.util.List;

public record EncountersDataDTO(
        String guildName,
        List<Byte> weights,
        List<EncounterDTO> encounters
) {
}
