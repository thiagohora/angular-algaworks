package br.com.thiagohora.user.service;

import br.com.thiagohora.infrastructure.security.token.user.UserInfo;
import br.com.thiagohora.user.domain.Permissao;
import br.com.thiagohora.user.domain.User;
import br.com.thiagohora.user.infrastruture.exception.UserNotFoundException;
import br.com.thiagohora.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserDetailServiceSecutity implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailServiceSecutity(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {

        final User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        
        return new UserInfo(user.getNome(), email, user.getSenha(), getGrantedAuthority(user.getPermissoes()));
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthority(final List<Permissao> permissoes) {
        return permissoes
                    .stream()
                    .map(permissao -> new SimpleGrantedAuthority(permissao.getDescricao().toUpperCase()))
                    .collect(Collectors.toSet());
    }

}
