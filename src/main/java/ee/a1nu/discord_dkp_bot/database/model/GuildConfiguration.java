package ee.a1nu.discord_dkp_bot.database.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "guild_configuration")
@Getter
@Setter
public class GuildConfiguration extends BaseEntity {
    @JoinColumn(name = "guild_id", nullable = false)
    @OneToOne
    GuildEntity guild;

    @Column(name = "admin_role_snowflake")
    Long administratorRoleSnowflake;

    @Column(name = "moderator_role_snowflake")
    Long memberRoleSnowflake;

    @Column(name = "member_role_snowflake")
    Long moderatorRoleSnowflake;

    @Column(name = "stash_enabled")
    boolean stashEnabled;

    @Column(name = "attendance_enabled")
    boolean attendanceEnabled;

    @Column(name = "loot_pretending_days_delay")
    int lootPretendingDaysDelay;
}
