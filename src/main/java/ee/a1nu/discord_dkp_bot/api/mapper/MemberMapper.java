package ee.a1nu.discord_dkp_bot.api.mapper;

import ee.a1nu.discord_dkp_bot.api.dto.DiscordGuildMemberDTO;
import ee.a1nu.discord_dkp_bot.api.dto.EncounterTemplateDTO;
import ee.a1nu.discord_dkp_bot.api.dto.EventViewModerationDataDTO;
import ee.a1nu.discord_dkp_bot.bot.service.DiscordBotService;
import ee.a1nu.discord_dkp_bot.database.service.EncounterService;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class MemberMapper {

    private final EncounterService encounterService;
    private final DiscordBotService discordBotService;

    public MemberMapper(EncounterService encounterService, DiscordBotService discordBotService) {
        this.encounterService = encounterService;
        this.discordBotService = discordBotService;
    }

    public EventViewModerationDataDTO mapEventViewModerationData(Long guildId) {
        return new EventViewModerationDataDTO(mapDiscordMembersToDiscordGuildMemberDto(guildId), mapOptionalEncountersToDto(guildId));
    }

    private List<DiscordGuildMemberDTO> mapDiscordMembersToDiscordGuildMemberDto(Long guildId) {
        return discordBotService.getGuildMembers(guildId).map(member ->
                        new DiscordGuildMemberDTO(
                                member.getId().asString(),
                                member.getNickname().orElse(member.getDisplayName()))
                )
                .collectSortedList(Comparator.comparing(DiscordGuildMemberDTO::Name))
                .block();
    }

    private List<EncounterTemplateDTO> mapOptionalEncountersToDto(Long guildId) {
        return encounterService.getOptionalEncounters(guildId).stream().map(encounter ->
                        new EncounterTemplateDTO(encounter.getId().toString(), encounter.getName()))
                .sorted(Comparator.comparing(EncounterTemplateDTO::name))
                .toList();
    }
}
