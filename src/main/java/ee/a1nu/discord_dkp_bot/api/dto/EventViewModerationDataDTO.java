package ee.a1nu.discord_dkp_bot.api.dto;

import java.util.List;

public record EventViewModerationDataDTO(
        List<DiscordGuildMemberDTO> guildMembers,
        List<EncounterTemplateDTO> encounterTemplates
) {
}
