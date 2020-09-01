package com.tuyoo.framework.grow.auth.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.tuyoo.framework.grow.auth.entities.GameEntities;
import com.tuyoo.framework.grow.auth.entities.RoleEntities;
import com.tuyoo.framework.grow.auth.entities.UserEntities;
import com.tuyoo.framework.grow.auth.entities.UserPermissionEntities;
import com.tuyoo.framework.grow.auth.repository.GameRepository;
import com.tuyoo.framework.grow.auth.repository.UserPermissionRepository;
import com.tuyoo.framework.grow.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service("myUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService
{
    @Autowired
    UserRepository userRepository;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    UserPermissionRepository userPermissionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        UserEntities user = userRepository.findByUsername(username);

        if (user == null || user.getId() < 0)
        {
            throw new UsernameNotFoundException("Username not found: " + username);
        }

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

        List<UserPermissionEntities> permissionList = userPermissionRepository.findAllByUsername(user.getUsername());
        if (permissionList == null)
        {
            return authorities;
        }
        List<GameEntities> allGame = gameRepository.findAll();
        HashMap<String, String> gameMap = new HashMap<>();
        for (GameEntities gameEntities :
                allGame)
        {
            gameMap.put(gameEntities.getId(), gameEntities.getProjectId());
        }
        for (UserPermissionEntities permission :
                permissionList)
        {
            try
            {
                List<String> gameList = JSON.parseArray(permission.getGame()).toJavaList(String.class);
                for (String game :
                        gameList)
                {
                    String projectId = gameMap.get(game);
                    if (projectId == null) {
                        projectId = game;
                    }
                    authorities.add(new SimpleGrantedAuthority("PROJECT_" + projectId));
                }
            }
            catch (Exception ignored)
            {

            }
        }

        return authorities;
    }
}

