package com.levyks.spring_blog.models;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.levyks.spring_blog.dtos.UserDTO;
import lombok.*;
import javax.persistence.*;

import org.hibernate.Hibernate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ToString.Exclude
    private Set<Role> roles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public UserDTO toDTO() {

        Set<String> rolesStringSet = this.roles.stream().map(Role::getName).collect(Collectors.toSet());

        return new UserDTO(this.id, this.email, this.firstName, this.lastName, rolesStringSet);

    }
}
