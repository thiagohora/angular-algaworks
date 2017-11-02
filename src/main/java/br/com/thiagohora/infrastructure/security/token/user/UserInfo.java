package br.com.thiagohora.infrastructure.security.token.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserInfo extends User {

    private final String name;

    public UserInfo(final String name, final String email, final String password, final Collection<? extends GrantedAuthority> authorities) {
        super(email, password, authorities);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
