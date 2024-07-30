package ee.a1nu.discord_dkp_bot.database.model;

import ee.a1nu.discord_dkp_bot.api.util.Action;
import ee.a1nu.discord_dkp_bot.api.util.ChangeContext;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transaction")
@Setter
@Getter
public class Transaction {
    @Column(name = "initiator_snowflake", nullable = false, updatable = false)
    Long initiatorSnowflake;
    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
    @Column(updatable = false)
    private Instant createdDate;

    @Column(name = "change_context", nullable = false, updatable = false)
    private ChangeContext changeContext;

    @Column
    private String editedEntity;

    @Column
    private Action action;
}
