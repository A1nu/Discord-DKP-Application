package ee.a1nu.discord_dkp_bot.database.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "guild_member_note")
public class GuildMemberNote extends BaseEntity {
    @Column(name = "note_text")
    private String noteText;

    @Column(name = "visible_to_member")
    private boolean visibleToMember;

    @JoinColumn(name = "member_id")
    @ManyToOne
    private GuildMember member;
}
