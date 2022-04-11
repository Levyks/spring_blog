package com.levyks.spring_blog.models;

import lombok.*;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "posts")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Post extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Type(type = "text")
    private String content;

    @Column(nullable = false)
    private Boolean edited = false;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Comment> comments;

    public boolean isAuthor(User user) {
        return Objects.equals(this.author, user);
    }

    public String getContentShort(boolean addEllipsis) {
        return content.length() > 200 ?
                content.substring(0, 200) + (addEllipsis ? "..." : "")
                : content;
    }
}
