package kz.am.imagehosting.domain;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "auth_role")
public class AuthRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "authRoles")
    private Set<AuthUser> authUser;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<AuthUser> getAuthUser() {
        return authUser;
    }

    public void setAuthUser(Set<AuthUser> authUser) {
        this.authUser = authUser;
    }
}
