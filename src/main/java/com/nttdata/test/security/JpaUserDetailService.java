package com.nttdata.test.security;

import com.nttdata.test.entity.UserEntity;
import com.nttdata.test.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JpaUserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userOptional = userRepository.findByEmail(username);
        if(userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        UserEntity user = userOptional.orElseThrow();
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ADMIN");
        SimpleGrantedAuthority simpleGrantedAuthority2 = new SimpleGrantedAuthority("USER");
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);
        authorities.add(simpleGrantedAuthority2);
        return new User(user.getEmail(), user.getPassword(), user.isActive(), true, true, true, authorities);
    }
}
