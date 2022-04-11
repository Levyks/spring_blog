package com.levyks.spring_blog.dtos.auth;

import com.levyks.spring_blog.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasicUserDTO {
    private Long id;
    private String name;

    public static BasicUserDTO fromUser(User user) {
        return new BasicUserDTO(user.getId(), user.getFirstName() + " " + user.getLastName());
    }
}
