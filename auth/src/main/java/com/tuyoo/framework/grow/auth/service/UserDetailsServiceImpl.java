package com.tuyoo.framework.grow.auth.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.tuyoo.framework.grow.auth.entities.*;
import com.tuyoo.framework.grow.auth.repository.GameRepository;
import com.tuyoo.framework.grow.auth.repository.StudioRepository;
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
    StudioRepository studioRepository;

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
        // 写入角色
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (RoleEntities role : user.getRoleEntitiesList())
        {
            authorities
                    .add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }

        // padmin写入工作室下所有游戏
        List<StudioEntities> studioList = studioRepository.findAllByAdmin(user.getUsername());
        if (studioList != null) {
            for (StudioEntities studioEntities :
                    studioList)
            {
                for (GameEntities gameEntities:
                     studioEntities.getGameEntities())
                {
                    authorities.add(new SimpleGrantedAuthority("PROJECT_" + gameEntities.getProjectId()));
                }
            }
        }

        // 写入权限列表中的游戏
        List<UserPermissionEntities> permissionList = userPermissionRepository.findAllByUsername(user.getUsername());
        if (permissionList != null)
        {
            List<GameEntities> allGame = gameRepository.findAll();
            HashMap<String, String> gameMap = new HashMap<>();
            for (GameEntities gameEntities :
                    allGame)
            {
                gameMap.put(gameEntities.getId().toString(), gameEntities.getProjectId());
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
        }

        return authorities;
    }
}

