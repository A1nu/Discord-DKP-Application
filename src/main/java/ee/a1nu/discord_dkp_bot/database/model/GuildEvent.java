package ee.a1nu.discord_dkp_bot.database.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "guild_event")
public class GuildEvent extends BaseEntity {

    @JoinColumn(nullable = false, name = "guild_event_encounter")
    @ManyToOne
    Encounter encounter;

    @JoinColumn(nullable = false, name = "event_owner_guild")
    @ManyToOne
    GuildEntity guild;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "event")
    List<EventAttendance> eventAttendance;
}
