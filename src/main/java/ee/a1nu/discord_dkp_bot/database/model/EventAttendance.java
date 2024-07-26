package ee.a1nu.discord_dkp_bot.database.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "event_attendance")
@Getter
@Setter
public class EventAttendance extends BaseEntity {
    @JoinColumn(name = "event_id", nullable = false)
    @ManyToOne
    private GuildEvent event;

    @JoinColumn(name = "guild_member_id", nullable = false)
    @ManyToOne
    private GuildMember guildMember;
}
