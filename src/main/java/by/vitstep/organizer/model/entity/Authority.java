package by.vitstep.organizer.model.entity;

import by.vitstep.organizer.model.entity.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

// Класс присвоения юзеру роли ADMIN или USER
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority implements GrantedAuthority {
    private static final String SEQ_NAME = "authority_id_seq";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private Roles authority;
    @ManyToOne
    private User user;

    @Override
    @Transient
    public String getAuthority() {
        return authority.name();
    }
}
