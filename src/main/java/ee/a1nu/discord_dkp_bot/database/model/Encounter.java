package ee.a1nu.discord_dkp_bot.database.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "encounter")
public class Encounter extends BaseEntity {
    @Column(name = "encounter_name", nullable = false)
    String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "encounter", orphanRemoval = true)
    Set<EncounterSpawn> encounterSpawns;

    @Column(name = "scheduled_encounter")
    boolean scheduledEncounter;

    @Column(name = "prime_encounter")
    boolean primeEncounter;

    @JoinColumn(name = "encounter_weight", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    Weight weight;

    @ManyToOne
    @JoinColumn(name = "guild_id", nullable = false)
    GuildEntity guild;

    public boolean isEveryDay() {
        return encounterSpawns.size() == 1 && encounterSpawns.stream().findFirst().get().isEveryday();
    }
}
