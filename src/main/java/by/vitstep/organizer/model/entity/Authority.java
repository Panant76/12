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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
