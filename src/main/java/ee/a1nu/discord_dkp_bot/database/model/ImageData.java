package ee.a1nu.discord_dkp_bot.database.model;

import ee.a1nu.discord_dkp_bot.api.listener.ImageDataListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "image")
@EntityListeners(ImageDataListener.class)
public class ImageData extends BaseEntity {


    @ManyToOne
    @JoinColumn(name = "guild_id", nullable = false)
    GuildEntity guild;
    @Column(name = "disposable")
    private boolean disposable;
    @Column(name = "name")
    private String name;

}
