package ee.a1nu.discord_dkp_bot.database.model;

import ee.a1nu.discord_dkp_bot.api.util.EventStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Set;

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
    Set<EventAttendance> eventAttendance;

    @Column(nullable = false, name = "event_date")
    OffsetDateTime eventDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "event")
    Set<Screenshot> screenshots;

    @Column(name = "event_status")
    EventStatus eventStatus;
}
