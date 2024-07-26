package ee.a1nu.discord_dkp_bot.database.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "personalized_item")
public class PersonalizedItem extends Item {

    @Column(name = "item_available")
    private boolean itemAvailable;

    @JoinColumn(name = "item_owner")
    @ManyToOne
    private GuildMember owner;

    @Column(nullable = false, name = "item_exp_inserted")
    private int expInserted;
}
