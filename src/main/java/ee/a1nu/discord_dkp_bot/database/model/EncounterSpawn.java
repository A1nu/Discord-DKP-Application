package ee.a1nu.discord_dkp_bot.database.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.Set;

@Entity
@Table(name = "encounter_spawn")
@Getter
@Setter
public class EncounterSpawn extends BaseEntity {

    @JoinColumn(name = "encounter_id")
    @ManyToOne
    Encounter encounter;

    @Column(name = "everyday")
    boolean everyday;

    @Column(name = "day_of_week")
    @Enumerated(EnumType.STRING)
    DayOfWeek dayOfWeek;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "encounterSpawn", orphanRemoval = true)
    Set<SpawnTime> encounterTimeSet;
}
