package kz.am.imagehosting.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "auth_user_role")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String role;

    @ManyToOne
    private User user;


}
