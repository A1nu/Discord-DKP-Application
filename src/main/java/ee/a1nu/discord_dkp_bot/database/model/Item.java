package ee.a1nu.discord_dkp_bot.database.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "item_model")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Item extends BaseEntity {
    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "item_price")
    private int itemPrice;

    @Column(name = "synthesized_item")
    private boolean synthesizedItem;

    @Column(name = "item_exp_needed")
    private int itemExpNeeded;

    @Column(name = "countable_item")
    private boolean countableItem;

    @Column(name = "item_quantity")
    private int itemQuantity;

    @JoinColumn(name = "guild_id")
    @ManyToOne
    private GuildEntity guild;
}
