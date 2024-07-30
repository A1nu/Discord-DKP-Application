package ee.a1nu.discord_dkp_bot.database.model;

import jakarta.persistence.*;

@Entity
@Table(name = "screenshots")
public class Screenshot extends BaseEntity {
    @JoinColumn(name = "event_id", nullable = false)
    @ManyToOne
    private GuildEvent event;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "screenshot")
    private byte[] screenshot;
}
