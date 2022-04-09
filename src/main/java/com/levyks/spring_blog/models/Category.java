package com.levyks.spring_blog.models;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "categories")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Category extends BaseEntity {

    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Post> posts;

    /**
     * TODO: lazy loading is not working here, Hibernate enhancement plugin is already installed,
     * but it does not seem to be working.
     * https://stackoverflow.com/questions/71811894/maven-hibernate-bytecode-enhancement-not-working
     */
    @Formula("(SELECT COUNT(*) FROM posts p WHERE p.category_id = id)")
    @ToString.Exclude
    @Basic(fetch = FetchType.LAZY)
    private int postCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Category category = (Category) o;
        return getId() != null && Objects.equals(getId(), category.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
