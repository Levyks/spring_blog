package com.levyks.spring_blog.models;

import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.*;
import javax.persistence.*;

import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
    }
)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ToString.Exclude
    private Set<Role> roles;

    @Column(nullable = false)
    private Date passwordChangedAt = new Date();

    public Set<String> getRolesAsString() {
        return roles.stream().map(Role::getName).collect(Collectors.toSet());
    }

    public String getFullname() {
        return firstName + " " + lastName;
    }

}
