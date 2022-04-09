package com.levyks.spring_blog.models;

import java.util.Objects;
import org.hibernate.Hibernate;
import javax.persistence.*;

import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Role extends BaseEntity {

    @Column(nullable = false)
    private String name;

}






