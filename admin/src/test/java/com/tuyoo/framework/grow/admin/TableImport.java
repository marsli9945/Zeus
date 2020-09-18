package com.tuyoo.framework.grow.admin;

import com.tuyoo.framework.grow.admin.entities.GameEntities;
import com.tuyoo.framework.grow.admin.entities.RoleEntities;
import com.tuyoo.framework.grow.admin.entities.StudioEntities;
import com.tuyoo.framework.grow.admin.entities.UserEntities;
import com.tuyoo.framework.grow.admin.repository.GameRepository;
import com.tuyoo.framework.grow.admin.repository.RoleRepository;
import com.tuyoo.framework.grow.admin.repository.StudioRepository;
import com.tuyoo.framework.grow.admin.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
public class TableImport
{
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudioRepository studioRepository;

    @Autowired
    GameRepository gameRepository;

    private final Integer num = 5;

    @Test
    public void flashAll()
    {
        flashRole();
        flashStudio();
    }

    @Test
    public void flashRole()
    {

        userRepository.deleteAll();
        roleRepository.deleteAll();

        Integer userId = roleRepository.save(new RoleEntities(null, "GA_WEB")).getId();
        Integer adminId = roleRepository.save(new RoleEntities(null, "GA_ADMAIN")).getId();

        log.info("userId:{}" + userId.toString());
        log.info("adminId:{}" + adminId.toString());

        createUser(adminId, userId);
    }

    @Test
    public void flashStudio()
    {
        studioRepository.deleteAll();
        gameRepository.deleteAll();

        for (int i = 0; i < num; i++)
        {
            StudioEntities save = studioRepository.save(new StudioEntities(
                    null,
                    "工作室" + i,
                    "padmin" + i + "@tuyoogame.com",
                    1,
                    null
            ));
            for (int j = 0; j < num; j++)
            {
                gameRepository.save(new GameEntities(
                        null,
                        i + "" + j,
                        "工作室" + i + "游戏" + j,
                        "http://analytics.tuyoo.com/api/img/0b933762c947ec7eaacdcdacaa93dbe9.png",
                        "+8",
                        "USDT",
                        1,
                        "1",
                        "2",
                        save
                ));
            }
        }
    }

    private void createUser(Integer adminId, Integer userId)
    {
        ArrayList<RoleEntities> roleList = new ArrayList<>();
        roleList.add(new RoleEntities(userId, null));

        for (int i = 0; i < num; i++)
        {
            userRepository.save(new UserEntities(
                    null,
                    "user" + i + "@tuyoogame.com",
                    new BCryptPasswordEncoder().encode("Pass@123"),
                    "user" + i,
                    20,
                    1,
                    roleList
            ));
        }

        for (int i = 0; i < num; i++)
        {
            userRepository.save(new UserEntities(
                    null,
                    "padmin" + i + "@tuyoogame.com",
                    new BCryptPasswordEncoder().encode("Pass@123"),
                    "padmin" + i,
                    20,
                    1,
                    roleList
            ));
        }

        roleList.add(new RoleEntities(adminId, null));
        for (int i = 0; i < num; i++)
        {
            userRepository.save(new UserEntities(
                    null,
                    "admin" + i + "@tuyoogame.com",
                    new BCryptPasswordEncoder().encode("Pass@123"),
                    "admin" + i,
                    20,
                    1,
                    roleList
            ));
        }

    }

    @Test
    @Transactional
    public void inTest()
    {
        ArrayList<Integer> ids = new ArrayList<>();
//        ids.add(19);
//        ids.add(20);

        List<StudioEntities> allByIdInAndStatus = studioRepository.findAllByIdInAndStatus(ids, 1);
        ArrayList<String> studios = new ArrayList<>();
        ArrayList<Integer> games = new ArrayList<>();
        for (StudioEntities studio :
                allByIdInAndStatus)
        {
            studios.add(studio.getName());
            for (GameEntities game :
                    studio.getGameEntities())
            {
                games.add(game.getId());
            }
        }
        log.info("studios:{}" + studios);
        log.info("games:{}" + games);
    }

    @Test
    public void createUserTest()
    {
        ArrayList<RoleEntities> roleList = new ArrayList<>();
        roleList.add(new RoleEntities(12, null));
        UserEntities userEntities = new UserEntities(null, "tuyoo@tuyoogame.com", null, "途游", 1, null, roleList);
        userRepository.save(userEntities);
    }
}
