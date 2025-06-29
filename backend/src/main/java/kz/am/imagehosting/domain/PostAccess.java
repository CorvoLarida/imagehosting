package kz.am.imagehosting.domain;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "post_access")
public class PostAccess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String type;
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "access")
    private Set<Post> posts;
    public PostAccess(){}

    public Integer getId() {return id;}

    public String getType() {return type;}

    public String getName() {return name;}

}
