package ee.a1nu.discord_dkp_bot.database.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetTime;

@Entity
@Table(name = "encounter_spawn_time")
@Getter
@Setter
public class SpawnTime extends BaseEntity {
    @Column(nullable = false, name = "offset_time")
    OffsetTime time;

    @JoinColumn(name = "encounter_spawn_id", nullable = false)
    @ManyToOne
    EncounterSpawn encounterSpawn;
}
