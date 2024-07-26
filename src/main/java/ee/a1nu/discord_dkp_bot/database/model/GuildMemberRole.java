package ee.a1nu.discord_dkp_bot.database.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "guild_member_role")
@Getter
@Setter
public class GuildMemberRole extends BaseEntity {
    @Column(nullable = false, name = "role_snowflake")
    Long administratorRoleSnowflake;
    @JoinColumn(name = "guild_configuration_id", nullable = false)
    @ManyToOne
    private GuildConfiguration guildConfiguration;
}
