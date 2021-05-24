package com.tuyoo.framework.grow.security.security;

import com.tuyoo.framework.grow.security.entities.UserEntities;
import com.tuyoo.framework.grow.security.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService
{
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException
    {
        UserEntities userEntitiesByUsername = userRepository.findUserEntitiesByUsername(s);
        if (userEntitiesByUsername == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return userEntitiesByUsername;
    }
}
