package com.levyks.spring_blog.dtos.auth;

import com.levyks.spring_blog.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserBasicDTO {
    private Long id;
    private String name;

    public static UserBasicDTO fromUser(User user) {
        return new UserBasicDTO(user.getId(), user.getFirstName() + " " + user.getLastName());
    }
}
