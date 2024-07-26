package ee.a1nu.discord_dkp_bot.database.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {
    @Column(name = "creator_snowflake", nullable = false, updatable = false)
    Long creatorSnowflake;
    @Column(name = "editor_snowflake")
    Long editorSnowflake;
    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
    @Version
    private Long version;
    @CreationTimestamp
    @Column(updatable = false)
    @JsonIgnore
    private Instant createdDate;
    @UpdateTimestamp
    @JsonIgnore
    private Instant lastModifiedDate;

    public boolean isNew() {
        return id == null;
    }

    public void setEditorSnowflake(Long editorSnowflake) {
        if (isNew()) {
            this.creatorSnowflake = editorSnowflake;
        } else {
            this.editorSnowflake = editorSnowflake;
        }
    }

    public boolean isCreatedByUser() {
        return creatorSnowflake != 0;
    }
}