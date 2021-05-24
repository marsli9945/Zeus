package com.tuyoo.framework.grow.security;

import com.tuyoo.framework.grow.security.entities.RoleEntities;
import com.tuyoo.framework.grow.security.entities.UserEntities;
import com.tuyoo.framework.grow.security.respository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class UserTest
{
    @Autowired
    UserRepository userRepository;

    @Test
    public void contextLoads() {
        UserEntities u1 = new UserEntities();
        u1.setUsername("javaboy");
        u1.setPassword("123");
        u1.setAccountNonExpired(true);
        u1.setAccountNonLocked(true);
        u1.setCredentialsNonExpired(true);
        u1.setEnabled(true);
        List<RoleEntities> rs1 = new ArrayList<>();
        RoleEntities r1 = new RoleEntities();
        r1.setName("ROLE_admin");
        r1.setNameZh("管理员");
        rs1.add(r1);
        u1.setRoleEntitiesList(rs1);
        userRepository.save(u1);

        UserEntities u2 = new UserEntities();
        u2.setUsername("江南一点雨");
        u2.setPassword("123");
        u2.setAccountNonExpired(true);
        u2.setAccountNonLocked(true);
        u2.setCredentialsNonExpired(true);
        u2.setEnabled(true);
        List<RoleEntities> rs2 = new ArrayList<>();
        RoleEntities r2 = new RoleEntities();
        r2.setName("ROLE_user");
        r2.setNameZh("普通用户");
        rs2.add(r2);
        u2.setRoleEntitiesList(rs2);
        userRepository.save(u2);
    }
}
