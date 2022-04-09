package com.levyks.spring_blog.security.details;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.levyks.spring_blog.models.Role;
import com.levyks.spring_blog.models.User;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserDetailsImpl implements UserDetails {

    @EqualsAndHashCode.Include()
    private final User user;

    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(User user) {
        this.user = user;
        this.authorities = mapRolesToAuthorities(user.getRoles());
    }

    static private Collection<GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    public boolean hasRole(String role) {
        return authorities.contains(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
