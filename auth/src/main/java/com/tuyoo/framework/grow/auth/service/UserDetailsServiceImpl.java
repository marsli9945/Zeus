package com.tuyoo.framework.grow.auth.service;

import com.tuyoo.framework.grow.auth.entities.RoleEntities;
import com.tuyoo.framework.grow.auth.entities.UserEntities;
import com.tuyoo.framework.grow.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service("myUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService
{
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        UserEntities user = userRepository.findByUsername(username);

        if (user == null || user.getId() < 0)
        {
            throw new UsernameNotFoundException("Username not found: " + username);
        }

        log.info("*************user: " + user.getUsername());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true, true, true, true, getGrantedAuthorities(user));
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities(
            UserEntities user)
    {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (RoleEntities role : user.getRoleEntitiesList())
        {
            authorities
                    .add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }

        log.info("------------------" + authorities);

        return authorities;
    }
}

