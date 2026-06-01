package kz.am.imagehosting.domain;

import jakarta.persistence.*;
import kz.am.imagehosting.json.deserializers.AuthRoleDesirializer;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Set;

@Entity
@Table(name = "auth_role")
@JsonDeserialize(using = AuthRoleDesirializer.class)
public class AuthRole implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "authRoles")
    private Set<AuthUser> authUser;

    public AuthRole(){}

    public Integer getId() {
        return id;
    }

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

    @Override
    public String getAuthority() {
        return name;
    }

    @Override
    public String toString() {
        return "AuthRole{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
