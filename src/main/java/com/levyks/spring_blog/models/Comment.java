package com.levyks.spring_blog.models;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "comments")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Comment extends BaseEntity {

    @Column(nullable = false)
    @Type(type = "text")
    private String content;

    @Column(nullable = false)
    private Boolean edited = false;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public boolean isAuthor(User user) {
        return Objects.equals(this.author, user);
    }

}
