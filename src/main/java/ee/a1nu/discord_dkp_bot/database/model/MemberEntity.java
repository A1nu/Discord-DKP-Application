package ee.a1nu.discord_dkp_bot.database.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "member")
@Getter
@Setter
public class MemberEntity {
    @Id
    @Column(name = "snowflake", nullable = false, updatable = false)
    Long snowflake;
    @Column(name = "creator_snowflake", nullable = false, updatable = false)
    Long creatorSnowflake;
    @Column(name = "editor_snowflake")
    Long editorSnowflake;
    @JoinColumn(name = "member_configuration")
    @OneToOne(fetch = FetchType.LAZY)
    MemberConfiguration memberConfiguration;
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
        return snowflake == null;
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
