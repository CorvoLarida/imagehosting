package kz.am.imagehosting.domain;

import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "auth_user")
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    private String username;
    private String password;
    private boolean active;
    @Column(name = "created_at")
    private ZonedDateTime createdAt = ZonedDateTime.now();
    @OneToMany(mappedBy = "createdBy")
    private Set<Post> posts;
    @OneToMany(mappedBy = "createdBy")
    private Set<PostCollection> postCollections;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "auth_user_roles",
            joinColumns = {@JoinColumn(name = "user_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "id")}
    )
    private Set<AuthRole> authRoles;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<AuthRole> getUserRoles() {
        return authRoles;
    }

    public void setUserRoles(Set<AuthRole> authRoles) {
        this.authRoles = authRoles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active +
                ", createdAt=" + createdAt +
                ", userRoles=" + authRoles +
                '}';
    }
}
