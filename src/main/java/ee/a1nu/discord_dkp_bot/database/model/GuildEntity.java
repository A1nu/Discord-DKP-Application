package ee.a1nu.discord_dkp_bot.database.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Set;


@Entity
@Getter
@Setter
@Table(name = "guild")
public class GuildEntity {
    @Id
    @Column(name = "snowflake", nullable = false, updatable = false)
    Long snowflake;
    @Column(name = "creator_snowflake", nullable = false, updatable = false)
    Long creatorSnowflake;
    @Column(name = "editor_snowflake")
    Long editorSnowflake;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "guild", orphanRemoval = true)
    Set<GuildMember> guildMembers;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "guild", orphanRemoval = true)
    Set<GuildEvent> guildEvents;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "guild_configuration_id")
    GuildConfiguration guildConfiguration;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "guild", orphanRemoval = true)
    Set<PersonalizedItem> guildItems;
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
