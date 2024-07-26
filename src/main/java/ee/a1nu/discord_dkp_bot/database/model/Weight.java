package ee.a1nu.discord_dkp_bot.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "encounter_weight")
@Getter
@Setter
public class Weight extends BaseEntity {
    @Column(nullable = false, unique = true, name = "encounter_weight")
    Integer weight;
}
