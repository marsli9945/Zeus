package com.tuyoo.framework.grow.admin;

import com.tuyoo.framework.grow.admin.ga.entities.GaGameEntities;
import com.tuyoo.framework.grow.admin.ga.entities.GaStudioEntities;
import com.tuyoo.framework.grow.admin.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
public class Test
{
    @Autowired
    PermissionRepository permissionRepository;

    @org.junit.jupiter.api.Test
    public void test () {
        GaStudioEntities gaStudioEntities = new GaStudioEntities();
        gaStudioEntities.setId(1);
        gaStudioEntities.setName("工作室");

        GaGameEntities gaGameEntities = new GaGameEntities();
        gaGameEntities.setId(1);
        gaGameEntities.setName("游戏");
        gaGameEntities.setIcon("http://baidu.com");
        gaGameEntities.setIs_own(false);

        ArrayList<GaGameEntities> gameList = new ArrayList<>();
        gameList.add(gaGameEntities);

        gaStudioEntities.setGame(gameList);

        System.out.println(gaStudioEntities);

        for (GaGameEntities game :
                gaStudioEntities.getGame())
        {
            game.setIs_own(true);
        }

        System.out.println(gaStudioEntities);

    }
}
