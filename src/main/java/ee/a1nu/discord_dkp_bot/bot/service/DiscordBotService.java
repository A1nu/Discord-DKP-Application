package ee.a1nu.discord_dkp_bot.bot.service;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Role;
import discord4j.rest.util.Permission;
import ee.a1nu.discord_dkp_bot.database.model.GuildConfiguration;
import ee.a1nu.discord_dkp_bot.database.service.GuildConfigurationService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;

@Service
public class DiscordBotService {
    GatewayDiscordClient gatewayDiscordClient;
    GuildConfigurationService guildConfigurationService;

    public DiscordBotService(GatewayDiscordClient gatewayDiscordClient, GuildConfigurationService guildConfigurationService) {
        this.gatewayDiscordClient = gatewayDiscordClient;
        this.guildConfigurationService = guildConfigurationService;
    }

    public Flux<Guild> getBotGuilds(Snowflake memberSnowflake) {
        return gatewayDiscordClient.getGuilds().publishOn(Schedulers.boundedElastic()).filter(guild -> Boolean.TRUE.equals(guild.getMemberById(memberSnowflake).hasElement().block()));
    }

    public boolean isMemberAdministrator(Snowflake memberSnowflake, Snowflake guildSnowflake) {
        Mono<Guild> guild = gatewayDiscordClient.getGuildById(guildSnowflake);
        Mono<Member> member = getMember(memberSnowflake, guildSnowflake);

        return Objects.equals(guild.map(Guild::getOwnerId).block(), member.map(Member::getId).block()) ||
                Objects.requireNonNull(member.flatMap(Member::getBasePermissions).block()).contains(Permission.ADMINISTRATOR);
    }

    public boolean isMemberHasAdministrativeRoleAccess(Snowflake memberSnowflake, Snowflake guildSnowflake) {
        Mono<Member> member = getMember(memberSnowflake, guildSnowflake);
        GuildConfiguration guildConfiguration = guildConfigurationService.getGuildConfiguration(guildSnowflake.asLong());

        if (guildConfiguration != null && guildConfiguration.hasRolesDefined()) {
            return Boolean.TRUE.equals(member.map(Member::getRoleIds).filter(r -> r.contains(Snowflake.of(guildConfiguration.getAdministratorRoleSnowflake()))).hasElement().block());
        }

        return false;
    }

    private Mono<Member> getMember(Snowflake memberSnowflake, Snowflake guildSnowflake) {
        return gatewayDiscordClient.getGuildById(guildSnowflake).flatMap(g -> g.getMemberById(memberSnowflake));
    }

    public Flux<Role> getGuildRoles(Long guildId) {
        return gatewayDiscordClient.getGuildById(Snowflake.of(guildId)).flatMapMany(Guild::getRoles);
    }

    public String getGuildName(Long guildId) {
        return gatewayDiscordClient.getGuildById(Snowflake.of(guildId)).map(Guild::getName).block();
    }

    public boolean isMemberHasModerationRoleAccess(Snowflake memberSnowflake, Snowflake guildSnowflake) {
        Mono<Member> member = getMember(memberSnowflake, guildSnowflake);
        GuildConfiguration guildConfiguration = guildConfigurationService.getGuildConfiguration(guildSnowflake.asLong());
        if (guildConfiguration != null && guildConfiguration.hasRolesDefined()) {
            Snowflake administratorRoleSnowflake = Snowflake.of(guildConfiguration.getAdministratorRoleSnowflake());
            Snowflake moderatorRoleSnowflake = Snowflake.of(guildConfiguration.getModeratorRoleSnowflake());

            return Boolean.TRUE.equals(member.map(Member::getRoleIds).filter(r -> r.contains(administratorRoleSnowflake) || r.contains(moderatorRoleSnowflake)).hasElement().block());
        }
        return false;
    }

    public boolean isMemberHasMemberRoleAccess(Snowflake memberSnowflake, Snowflake guildSnowflake) {
        Mono<Member> member = getMember(memberSnowflake, guildSnowflake);
        GuildConfiguration guildConfiguration = guildConfigurationService.getGuildConfiguration(guildSnowflake.asLong());

        if (guildConfiguration != null && guildConfiguration.hasRolesDefined()) {
            Snowflake administratorRoleSnowflake = Snowflake.of(guildConfiguration.getAdministratorRoleSnowflake());
            Snowflake moderatorRoleSnowflake = Snowflake.of(guildConfiguration.getModeratorRoleSnowflake());
            Snowflake memberRoleSnowflake = Snowflake.of(guildConfiguration.getMemberRoleSnowflake());

            return Boolean.TRUE.equals(
                    member.map(Member::getRoleIds)
                            .filter(r -> r.contains(administratorRoleSnowflake) || r.contains(moderatorRoleSnowflake) || r.contains(memberRoleSnowflake))
                            .hasElement().block());
        }
        return false;
    }
}
