package ee.a1nu.discord_dkp_bot.database.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "member_configuration")
public class MemberConfiguration extends BaseEntity {

    @JoinColumn(name = "member_default_guild")
    @OneToOne(fetch = FetchType.LAZY)
    GuildEntity defaultGuild;
}
