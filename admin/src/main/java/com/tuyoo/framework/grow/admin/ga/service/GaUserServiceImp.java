package com.tuyoo.framework.grow.admin.ga.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.tuyoo.framework.grow.admin.entities.*;
import com.tuyoo.framework.grow.admin.ga.GaConfig;
import com.tuyoo.framework.grow.admin.ga.entities.*;
import com.tuyoo.framework.grow.admin.ga.form.GaUserForm;
import com.tuyoo.framework.grow.admin.jwt.ClaimsEntities;
import com.tuyoo.framework.grow.admin.jwt.JwtUtil;
import com.tuyoo.framework.grow.admin.mail.MailService;
import com.tuyoo.framework.grow.admin.repository.GameRepository;
import com.tuyoo.framework.grow.admin.repository.PermissionRepository;
import com.tuyoo.framework.grow.admin.repository.StudioRepository;
import com.tuyoo.framework.grow.admin.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.*;

@Slf4j
@Service
public class GaUserServiceImp implements GaUserService
{
    @Autowired
    GaConfig gaConfig;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    StudioRepository studioRepository;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private MailService mailService;

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 加上自己是管理员的工作室
     *
     * @param studioList 有指定权限的用户的工作室
     * @return 可用工作室总和
     */
    private List<Integer> hasAdminStudioId(ArrayList<Integer> studioList)
    {
        List<StudioEntities> allByAdminAndStatus = studioRepository.findAllByAdminAndStatus(jwtUtil.getUsername(), 1);
        for (StudioEntities studioEntities :
                allByAdminAndStatus)
        {
            studioList.add(studioEntities.getId());
        }
        return studioList;
    }

    /**
     * 获取当前操作用户可分配权限的所有工作室ID
     *
     * @return studioList
     */
    private List<Integer> hasDistributeStudioId()
    {
        // 先找到自己可分配权限的所有工作室
        List<PermissionEntities> allByUsernameAndIsDistributeAndStatus = permissionRepository.findAllByUsernameAndIsDistribute(jwtUtil.getUsername(), 1);
        ArrayList<Integer> studioList = new ArrayList<>();
        for (PermissionEntities permissionEntities :
                allByUsernameAndIsDistributeAndStatus)
        {
            studioList.add(permissionEntities.getStudioId());
        }

        return hasAdminStudioId(studioList);
    }

    /**
     * 获取当前操作用户可分配权限的所有工作室ID
     *
     * @return studioList
     */
    private List<Integer> hasAccessGameStudioId()
    {
        // 先找到自己可分配权限的所有工作室
        List<PermissionEntities> allByUsernameAndIsDistributeAndStatus = permissionRepository.findAllByUsernameAndIsAccessGame(jwtUtil.getUsername(), 1);
        ArrayList<Integer> studioList = new ArrayList<>();
        for (PermissionEntities permissionEntities :
                allByUsernameAndIsDistributeAndStatus)
        {
            studioList.add(permissionEntities.getStudioId());
        }

        return hasAdminStudioId(studioList);
    }


    /**
     * 获取当前操作用户可分配权限的所有工作室中的成员名
     *
     * @return usernameList
     */
    private List<String> hasDistributeUsers()
    {
        List<Integer> studioList = hasDistributeStudioId();

        // 再找出拥有工作室权限的所有用户
        List<PermissionEntities> allByStudioIdInAndStatus = permissionRepository.findAllByStudioIdIn(studioList);
        ArrayList<String> usernameList = new ArrayList<>();
        for (PermissionEntities permissionEntities :
                allByStudioIdInAndStatus)
        {
            usernameList.add(permissionEntities.getUsername());
        }

        return usernameList;
    }

    @Override
    public Page<UserEntities> fetch(Integer page, Integer size, String name)
    {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");

        Specification<UserEntities> specification = (Specification<UserEntities>) (root, criteriaQuery, cb) ->
        {
            List<Predicate> predicates = new ArrayList<>();
            Join<Object, Object> role = root.join("roleEntitiesList", JoinType.INNER);

            // 拥有GA_WEB角色
            predicates.add(cb.equal(role.get("id"), gaConfig.getRoleId()));

            // 排除自己
            predicates.add(cb.notEqual(root.get("username"), jwtUtil.getUsername()));

            // 不是GA_ADMAIN只显示自己有管理用户的工作室下的所有用户
            if (!jwtUtil.isGaAdmin())
            {
                CriteriaBuilder.In<String> usernameIn = cb.in(root.get("username"));
                for (String username :
                        hasDistributeUsers())
                {
                    usernameIn.value(username);
                }
                predicates.add(usernameIn);
            }

            if (!StringUtils.isEmpty(name))
            {
                predicates.add(cb.like(root.get("username"), "%" + name + "%"));
            }


            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return userRepository.findAll(specification, pageable);
    }

    @Override
    public List<GaSelectEntities> allUserSelect()
    {
        ArrayList<GaSelectEntities> select = new ArrayList<>();
        List<UserEntities> allByStatusAndAndRoleEntitiesList = userRepository.findAllByStatusAndRoleEntitiesList(
                1,
                new RoleEntities(gaConfig.getRoleId(), null)
        );
        for (UserEntities userEntities :
                allByStatusAndAndRoleEntitiesList)
        {
            select.add(new GaSelectEntities(userEntities.getName(), userEntities.getUsername()));
        }
        return select;
    }

    // 根据配置构造全量权限
    private String getAllPermission()
    {
        HashMap<Object, Object> parentPermission = new HashMap<>();
        for (GaPermissionEntities permission : gaConfig.getPermission())
        {
            ArrayList<String> childrenPermission = new ArrayList<>();
            for (GaPermissionChildrenEntities children : permission.getChildren())
            {
                childrenPermission.add(children.getId());
            }
            parentPermission.put(permission.getId(), childrenPermission);
        }
        return JSON.toJSONString(parentPermission);
    }

    // 游戏列表转为全量权限
    private List<GaUserInfoGameEntities> gameToInfoGame(List<GameEntities> gameList)
    {
        ArrayList<GaUserInfoGameEntities> infoGameList = new ArrayList<>();
        String permission = getAllPermission();
        for (GameEntities gameEntities :
                gameList)
        {
            if (!gameEntities.getStatus().equals(1))
            {
                break;
            }
            infoGameList.add(new GaUserInfoGameEntities(
                    gameEntities.getProjectId(),
                    gameEntities.getName(),
                    gameEntities.getIcon(),
                    gameEntities.getTimeZone(),
                    permission
            ));
        }
        return infoGameList;
    }

    // 游戏ID列表转为指定权限项的权限
    private List<GaUserInfoGameEntities> gameIdToGameInfo(List<Integer> gameIdList, String permission, HashMap<Integer, GameEntities> gameMap)
    {
        ArrayList<GaUserInfoGameEntities> infoGameList = new ArrayList<>();
        for (Integer gameId :
                gameIdList)
        {
            GameEntities gameEntities = gameMap.get(gameId);
            // 找不到信息，游戏关闭或已删除，不作处理
            if (gameEntities == null)
            {
                break;
            }
            infoGameList.add(new GaUserInfoGameEntities(
                    gameEntities.getProjectId(),
                    gameEntities.getName(),
                    gameEntities.getIcon(),
                    gameEntities.getTimeZone(),
                    permission
            ));
        }
        return infoGameList;
    }

    @Override
    public GaUserInfoEntities userInfo()
    {
        GaUserInfoEntities gaUserInfoEntities = new GaUserInfoEntities();
        UserEntities user = userRepository.findByUsername(jwtUtil.getUsername());
        if (user == null)
        {
            return new GaUserInfoEntities();
        }

        gaUserInfoEntities.setId(user.getId());
        gaUserInfoEntities.setName(user.getName());
        gaUserInfoEntities.setUsername(user.getUsername());

        if (jwtUtil.isGaAdmin())
        {
            gaUserInfoEntities.setIs_admin(1);
            gaUserInfoEntities.setIs_distribute(1);
            gaUserInfoEntities.setIs_access_game(1);

            List<GameEntities> allGame = gameRepository.findAllByStatus(1);
            gaUserInfoEntities.setPermission(gameToInfoGame(allGame));
        }
        else
        {
            gaUserInfoEntities.setIs_admin(0);

            // 获取所有开启的游戏信息，并转化为ID为键的Map
            List<GameEntities> allByStatus = gameRepository.findAllByStatus(1);
            HashMap<Integer, GameEntities> gameMap = new HashMap<>();
            for (GameEntities gameEntities :
                    allByStatus)
            {
                gameMap.put(gameEntities.getId(), gameEntities);
            }

            // 先获取自己是管理员的工作室下的游戏
            List<StudioEntities> allByAdminAndStatus = studioRepository.findAllByAdminAndStatus(jwtUtil.getUsername(), 1);
            List<Integer> studioIdList = new ArrayList<>();
            ArrayList<GameEntities> adminGameEntitiesList = new ArrayList<>();
            List<GaUserInfoGameEntities> permissionList = new ArrayList<>();

            if (!allByAdminAndStatus.isEmpty())
            {
                gaUserInfoEntities.setIs_distribute(1);
                gaUserInfoEntities.setIs_access_game(1);

                for (StudioEntities studioEntities :
                        allByAdminAndStatus)
                {
                    studioIdList.add(studioEntities.getId());
                    adminGameEntitiesList.addAll(studioEntities.getGameEntities());
                }
                // 先整理出是管理员的工作室下所有游戏全量权限
                permissionList.addAll(gameToInfoGame(adminGameEntitiesList));
            }

            if (studioIdList.isEmpty())
            {
                studioIdList.add(-1);
            }
            //  获取自己不是管理员的工作室权限记录
            List<PermissionEntities> allByUsernameAndStudioIdNotInAndStatus = permissionRepository.findAllByUsernameAndStudioIdNotIn(jwtUtil.getUsername(), studioIdList);
            for (PermissionEntities permissionEntities :
                    allByUsernameAndStudioIdNotInAndStatus)
            {
                // 添加游戏和管理成员，任意有一个工作室有就开启
                if (permissionEntities.getIsDistribute().equals(1))
                {
                    gaUserInfoEntities.setIs_distribute(1);
                }
                if (permissionEntities.getIsAccessGame().equals(1))
                {
                    gaUserInfoEntities.setIs_access_game(1);
                }

                JSONArray jsonArray = JSON.parseArray(permissionEntities.getGame());
                List<Integer> gameIdList = jsonArray.toJavaList(Integer.class);
                permissionList.addAll(gameIdToGameInfo(gameIdList, permissionEntities.getPermission(), gameMap));
            }
            gaUserInfoEntities.setPermission(permissionList);
        }

        return gaUserInfoEntities;
    }

    private boolean hasRole(List<RoleEntities> roleEntitiesList, Integer roleId)
    {
        for (RoleEntities roleEntities : roleEntitiesList)
        {
            if (roleEntities.getId().equals(roleId))
            {
                return true;
            }
        }
        return false;
    }

    // 构造工作室下的全量权限
    private GaStudioEntities getStudioPermission(StudioEntities studioEntities, boolean isDistribute)
    {
        GaStudioEntities gaStudioEntities = new GaStudioEntities();
        gaStudioEntities.setId(studioEntities.getId());
        gaStudioEntities.setName(studioEntities.getName());
        List<GaPermissionEntities> permission = new ArrayList<>();
        for (GaPermissionEntities gaPermissionEntities :
                gaConfig.getPermission())
        {
            log.info("gaPermissionEntities:{}", gaPermissionEntities);
            if (gaPermissionEntities.getId().equals("admin") && isDistribute)
            {
                continue;
            }
            permission.add(gaPermissionEntities);
        }
        log.info("permission:{}", permission);
        gaStudioEntities.setPermission(permission);

        ArrayList<GaGameEntities> gameList = new ArrayList<>();
        for (GameEntities gameEntities :
                studioEntities.getGameEntities())
        {
            gameList.add(new GaGameEntities(gameEntities.getId(), gameEntities.getProjectId(), gameEntities.getName(), gameEntities.getIcon(), false));
        }
        gaStudioEntities.setGame(gameList);

        return gaStudioEntities;
    }

    // 判断指定用户等权限在当前操作用户可分配权限中的分布
    private List<GaStudioEntities> setUserPermission(String username, List<GaStudioEntities> gaStudioEntitiesList)
    {
        // 构造用户的权限和游戏快查表
        List<PermissionEntities> allByUsernameAndStatus = permissionRepository.findAllByUsername(username);
        HashMap<Integer, Map<String, List<String>>> userPermissionMap = new HashMap<>();
        HashMap<Integer, List<Integer>> userGameMap = new HashMap<>();
        for (PermissionEntities permissionEntities : allByUsernameAndStatus)
        {
            // 收集当前工作室中的游戏
            userGameMap.put(permissionEntities.getStudioId(), JSON.parseArray(permissionEntities.getGame()).toJavaList(Integer.class));

            // 收集当前工作室权限速查表
            Map<String, Object> studioPermissionMap = JSON.parseObject(permissionEntities.getPermission());
            Map<String, List<String>> permissionMap = new HashMap<>();
            for (Map.Entry<String, Object> entry :
                    studioPermissionMap.entrySet())
            {
                List<String> childrenPermissionList = new ArrayList<>();
                if (entry.getValue() instanceof List<?>)
                {
                    for (Object o : (List<?>) entry.getValue())
                    {
                        childrenPermissionList.add((String) o);
                    }
                }
                permissionMap.put(entry.getKey(), childrenPermissionList);
            }
            userPermissionMap.put(permissionEntities.getStudioId(), permissionMap);
        }

        log.info("userGameMap:{}", userGameMap);
        log.info("userPermissionMap:{}", userPermissionMap);

        ArrayList<GaStudioEntities> newGaStudioEntitiesList = new ArrayList<>();
        // 遍历已构建好的权限，打开被操作人已经拥有的权限
        for (GaStudioEntities gaStudioEntities :
                gaStudioEntitiesList)
        {
            GaStudioEntities newGaStudioEntities = new GaStudioEntities();
            newGaStudioEntities.setId(gaStudioEntities.getId());
            newGaStudioEntities.setName(gaStudioEntities.getName());
            ArrayList<GaGameEntities> newGaGameEntitiesList = new ArrayList<>();
            // 处理游戏是否拥有
            for (GaGameEntities gaGameEntities : gaStudioEntities.getGame())
            {
                GaGameEntities newGaGameEntities = new GaGameEntities();
                newGaGameEntities.setId(gaGameEntities.getId());
                newGaGameEntities.setName(gaGameEntities.getName());
                newGaGameEntities.setIcon(gaGameEntities.getIcon());
                // 开启已拥有的游戏
                newGaGameEntities.setIs_own(userGameMap.get(gaStudioEntities.getId()) != null &&
                        userGameMap.get(gaStudioEntities.getId()).contains(gaGameEntities.getId()));
                newGaGameEntitiesList.add(newGaGameEntities);
            }
            newGaStudioEntities.setGame(newGaGameEntitiesList);

            ArrayList<GaPermissionEntities> newGaPermissionEntitiesList = new ArrayList<>();
            // 处理内部权限是否拥有
            for (GaPermissionEntities gaPermissionEntities :
                    gaStudioEntities.getPermission())
            {
                GaPermissionEntities newGaPermissionEntities = new GaPermissionEntities();
                ArrayList<GaPermissionChildrenEntities> newGaPermissionChildrenEntitiesList = new ArrayList<>();
                newGaPermissionEntities.setId(gaPermissionEntities.getId());
                newGaPermissionEntities.setName(gaPermissionEntities.getName());
                for (GaPermissionChildrenEntities gaPermissionChildrenEntities :
                        gaPermissionEntities.getChildren())
                {
                    GaPermissionChildrenEntities newGaPermissionChildrenEntities = new GaPermissionChildrenEntities();
                    newGaPermissionChildrenEntities.setId(gaPermissionChildrenEntities.getId());
                    newGaPermissionChildrenEntities.setName(gaPermissionChildrenEntities.getName());
                    newGaPermissionChildrenEntities.setIs_own(userPermissionMap.get(gaStudioEntities.getId()) != null &&
                            userPermissionMap.get(gaStudioEntities.getId()).get(gaPermissionEntities.getId()) != null &&
                            userPermissionMap.get(gaStudioEntities.getId()).get(gaPermissionEntities.getId()).contains(gaPermissionChildrenEntities.getId()));
                    newGaPermissionChildrenEntitiesList.add(newGaPermissionChildrenEntities);
                }
                newGaPermissionEntities.setChildren(newGaPermissionChildrenEntitiesList);
                newGaPermissionEntitiesList.add(newGaPermissionEntities);
            }
            newGaStudioEntities.setPermission(newGaPermissionEntitiesList);
            newGaStudioEntitiesList.add(newGaStudioEntities);
        }

        return newGaStudioEntitiesList;
    }

    @Override
    public List<GaStudioEntities> getPermission(String username)
    {
        ArrayList<GaStudioEntities> studioPermissionList = new ArrayList<>();
        // 当前用户为admin，获取到所有工作室权限
        if (jwtUtil.isGaAdmin())
        {
            List<StudioEntities> allByStatus = studioRepository.findAllByStatus(1);
            for (StudioEntities studioEntities :
                    allByStatus)
            {
                studioPermissionList.add(getStudioPermission(studioEntities, false));
            }
        }
        else
        {
            // 先拿到是管理员的工作室
            List<StudioEntities> allByAdminAndStatus = studioRepository.findAllByAdminAndStatus(jwtUtil.getUsername(), 1);
            ArrayList<Integer> adminStudioIdList = new ArrayList<>();
            for (StudioEntities studioEntities :
                    allByAdminAndStatus)
            {
                log.info("adminStudio:{}", studioEntities.getId());
                // 获取全量权限
                studioPermissionList.add(getStudioPermission(studioEntities, false));
                adminStudioIdList.add(studioEntities.getId());
            }

            // 获取非工作室管理员具有管理权限的工作室
            List<PermissionEntities> allByUsernameAndStudioIdNotIn = permissionRepository.findAllByUsernameAndStudioIdNotIn(jwtUtil.getUsername(), adminStudioIdList);
            ArrayList<Integer> distributeStudioIdList = new ArrayList<>();
            for (PermissionEntities permissionEntities :
                    allByUsernameAndStudioIdNotIn)
            {
                distributeStudioIdList.add(permissionEntities.getStudioId());
            }
            List<StudioEntities> allByIdInAndStatus = studioRepository.findAllByIdInAndStatus(distributeStudioIdList, 1);
            for (StudioEntities studioEntities :
                    allByIdInAndStatus)
            {
                log.info("distributeStudio:{}", studioEntities.getId());
                // 获取去掉admin部分的权限
                studioPermissionList.add(getStudioPermission(studioEntities, true));
            }
        }

        // 有用户传入表示要编辑某人的权限
        if (username != null)
        {
            return setUserPermission(username, studioPermissionList);
        }

        return studioPermissionList;
    }

    @Override
    public List<GaSelectEntities> getStudioSelect()
    {
        ArrayList<GaSelectEntities> select = new ArrayList<>();
        List<StudioEntities> studioList;
        if (jwtUtil.isGaAdmin())
        {
            studioList = studioRepository.findAllByStatus(1);
        }
        else
        {
            studioList = studioRepository.findAllByIdInAndStatus(hasAccessGameStudioId(), 1);
        }

        for (StudioEntities studioEntities :
                studioList)
        {
            select.add(new GaSelectEntities(studioEntities.getName(), studioEntities.getId().toString()));
        }
        return select;
    }

    @Override
    @Transactional
    public boolean create(GaUserForm gaUserForm)
    {
        UserEntities user = userRepository.findByUsername(gaUserForm.getUsername());
        if (user == null)
        {
            // 没有用户，添加用户并添加角色
            UserEntities userEntities = new UserEntities();
            userEntities.setUsername(gaUserForm.getUsername());
            userEntities.setName(gaUserForm.getName());
            userEntities.setLevel(gaUserForm.getLevel());
            userEntities.setStatus(0);
            // 新开用户默认给GA_WEB角色
            ArrayList<RoleEntities> roleList = new ArrayList<>();
            roleList.add(new RoleEntities(gaConfig.getRoleId(), null));
            userEntities.setRoleEntitiesList(roleList);
            userRepository.save(userEntities);
        }
        else
        {
            List<RoleEntities> roleEntitiesList = user.getRoleEntitiesList();
            // 有用户没角色，为用户添加GA_WEB角色
            if (!hasRole(roleEntitiesList, gaConfig.getRoleId()))
            {
                roleEntitiesList.add(new RoleEntities(gaConfig.getRoleId(), null));
                user.setRoleEntitiesList(roleEntitiesList);
            }

            // 更新用户可编辑项
            user.setName(gaUserForm.getName());
            user.setLevel(gaUserForm.getLevel());
            userRepository.save(user);
        }

        // 处理权限记录
        for (GaStudioEntities studio : gaUserForm.getPermission())
        {
            saveUserPermission(gaUserForm.getUsername(), studio);
        }

        return true;
    }

    @Override
    public boolean update(GaUserForm gaUserForm)
    {
        UserEntities user = userRepository.findByUsernameAndRoleEntitiesList(
                gaUserForm.getUsername(),
                new RoleEntities(gaConfig.getRoleId(), null)
        );
        // 拥有GA_WEB角色的当前用户不存在
        if (user == null)
        {
            return false;
        }

        // 更新用户表信息
        user.setName(gaUserForm.getName());
        user.setLevel(gaUserForm.getLevel());
        userRepository.save(user);

        // 处理权限记录
        for (GaStudioEntities studio : gaUserForm.getPermission())
        {
            saveUserPermission(gaUserForm.getUsername(), studio);
        }

        return true;
    }

    @Override
    @Transactional
    public boolean delete(String username)
    {
        // 用户不存在不予处理
        UserEntities user = userRepository.findByUsernameAndStatusAndRoleEntitiesList(
                username,
                1,
                new RoleEntities(gaConfig.getRoleId(), null)
        );
        if (user == null)
        {
            return false;
        }

        // GA_ADMAIN 删除用户所有权限项
        if (jwtUtil.isGaAdmin())
        {
            permissionRepository.deleteAllByUsername(username);
        }
        else
        {
            // 删除用户所有自己是管理员或拥有可编辑权限的工作室的权限
            // 先获取自己是管理员的工作室下的游戏
            List<StudioEntities> allByAdminAndStatus = studioRepository.findAllByAdminAndStatus(jwtUtil.getUsername(), 1);
            List<Integer> studioIdList = new ArrayList<>();
            for (StudioEntities studioEntities :
                    allByAdminAndStatus)
            {
                studioIdList.add(studioEntities.getId());
            }

            if (studioIdList.isEmpty())
            {
                studioIdList.add(-1);
            }
            //  获取自己不是管理员的工作室权限记录
            List<PermissionEntities> allByUsernameAndStudioIdNotInAndStatus = permissionRepository.findAllByUsernameAndStudioIdNotIn(jwtUtil.getUsername(), studioIdList);
            for (PermissionEntities permissionEntities :
                    allByUsernameAndStudioIdNotInAndStatus)
            {
                studioIdList.add(permissionEntities.getStudioId());
            }
            permissionRepository.deleteAllByUsernameAndStudioIdIn(username, studioIdList);
        }
        return true;
    }

    @Override
    public void clearNoPermissionUser(String username)
    {
        // 不是任何工作室管理员且没有权限记录才进行账号清理
        List<StudioEntities> allByAdmin = studioRepository.findAllByAdmin(username);
        List<PermissionEntities> allByUsername = permissionRepository.findAllByUsername(username);
        if (allByAdmin.isEmpty() && allByUsername.isEmpty())
        {
            UserEntities byUsername = userRepository.findByUsername(username);
            ArrayList<RoleEntities> newRoleList = new ArrayList<>();
            for (RoleEntities roleEntities :
                    byUsername.getRoleEntitiesList())
            {
                if (!roleEntities.getId().equals(gaConfig.getRoleId()))
                {
                    newRoleList.add(roleEntities);
                }
            }
            byUsername.setRoleEntitiesList(newRoleList);
            userRepository.save(byUsername);
        }
    }

    private void saveUserPermission(String username, GaStudioEntities studioEntities)
    {
        PermissionEntities entities = new PermissionEntities();

        // 记录项已存在把ID赋值回去修改此条即可
        PermissionEntities studio = permissionRepository.findByUsernameAndStudioId(username, studioEntities.getId());
        if (studio != null)
        {
            entities.setId(studio.getId());
        }

        entities.setUsername(username);
        entities.setStudioId(studioEntities.getId());

        // 收集开启的游戏
        ArrayList<Integer> gameList = new ArrayList<>();
        for (GaGameEntities game : studioEntities.getGame())
        {
            if (game.getIs_own())
            {
                gameList.add(game.getId());
            }
        }
        if (gameList.size() == 0)
        {
            return;
        }
        entities.setGame(JSON.toJSONString(gameList));

        // 收集权限信息
        HashMap<Object, Object> parentPermission = new HashMap<>();
        for (GaPermissionEntities permission : studioEntities.getPermission())
        {
            ArrayList<String> childrenPermission = new ArrayList<>();
            for (GaPermissionChildrenEntities children : permission.getChildren())
            {
                if (children.getIs_own())
                {
                    childrenPermission.add(children.getId());

                    // 三个重要权限设置
                    if (children.getId().equals("is_auto"))
                    {
                        entities.setIsAuto(1);
                    }

                    if (children.getId().equals("is_distribute"))
                    {
                        entities.setIsDistribute(1);
                    }

                    if (children.getId().equals("is_access_game"))
                    {
                        entities.setIsAccessGame(1);
                    }
                }
            }
            parentPermission.put(permission.getId(), childrenPermission);
        }
        entities.setPermission(JSON.toJSONString(parentPermission));

        permissionRepository.save(entities);
    }


    /**
     * 为指定拥有指定工作室的所有
     * 有增加后续游戏权限的人添加游戏
     *
     * @param studioId 工作室ID
     * @param gameId   要增加的游戏ID
     */
    @Override
    public void addAutoPermissionGame(Integer studioId, Integer gameId)
    {
        List<PermissionEntities> allByStudioIdAndIsAuto = permissionRepository.findAllByStudioIdAndIsAuto(studioId, 1);
        for (PermissionEntities permissionEntities :
                allByStudioIdAndIsAuto)
        {
            JSONArray jsonArray = JSON.parseArray(permissionEntities.getGame());
            List<Integer> gameIdList = jsonArray.toJavaList(Integer.class);
            gameIdList.add(gameId);
            permissionEntities.setGame(JSON.toJSONString(gameIdList));
            permissionRepository.save(permissionEntities);
        }
    }

    @Override
    public void sendSignEmail(String username)
    {
        // 现获取jwt串
        Context context = getContext(username);
        String emailContent = templateEngine.process("signEmailTemplate", context);

        mailService.sendHtmlMail(username, "GrowAnalytics注册邮件", emailContent);
    }

    @Override
    public boolean sendResetEmail(String username)
    {
        UserEntities byUsernameAndStatusAndRoleEntitiesList = userRepository.findByUsernameAndStatusAndRoleEntitiesList(
                username,
                1,
                new RoleEntities(gaConfig.getRoleId(), null)
        );
        if (byUsernameAndStatusAndRoleEntitiesList == null)
        {
            return false;
        }

        // 现获取jwt串
        Context context = getContext(username);
        context.setVariable("name", username);
        String emailContent = templateEngine.process("resetEmailTemplate", context);

        mailService.sendHtmlMail(username, "GrowAnalytics密码重置邮件", emailContent);

        return true;
    }

    @Override
    public UserEntities validToken(String token)
    {
        ClaimsEntities claimsEntities = jwtUtil.decode(token);
        // 超过过期时间
        if (System.currentTimeMillis() / 1000 > Integer.parseInt(claimsEntities.getExp()))
        {
            return null;
        }
        return userRepository.findByUsernameAndRoleEntitiesList(
                claimsEntities.getUserName(),
                new RoleEntities(gaConfig.getRoleId(), null)
        );
    }

    private Context getContext(String username)
    {
        ClaimsEntities claimsEntities = new ClaimsEntities();
        claimsEntities.setUserName(username);
        // 两小时过期
        long timeMillis = System.currentTimeMillis() / 1000 + gaConfig.getMailTokenExp();
        claimsEntities.setExp(String.valueOf(timeMillis));
        String token = jwtUtil.encode(claimsEntities);

        Context context = new Context();
        context.setVariable("baseUrl", gaConfig.getHost() + "/sign");
        context.setVariable("token", token);

        return context;
    }

    @Override
    public boolean hasPreset(String username, String projectId)
    {
        // admin直接放行
        UserEntities admin = userRepository.findByUsernameAndRoleEntitiesList(
                username,
                new RoleEntities(gaConfig.getAdminId(), null)
        );
        log.info("admin:{}", admin);
        if (admin != null)
        {
            return true;
        }

        // 首先该用户是游戏所属工作室admin可以编辑预制概览
        GameEntities byProjectId = gameRepository.findByProjectId(projectId);
        if (byProjectId == null)
        {
            return false;
        }
        StudioEntities allByAdminAndStatus = studioRepository.findByAdminAndIdAndStatus(
                username,
                byProjectId.getStudio().getId(),
                1);
        log.info("allByAdminAndStatus:{}", allByAdminAndStatus);
        if (allByAdminAndStatus != null)
        {
            return true;
        }

        // 其次判断用户对该游戏的权限中是否有edit_preset_overview权限
        GameEntities game = gameRepository.findByProjectId(projectId);
        if (game == null)
        {
            return false;
        }
        log.info("gameId:{}", game.getId());
        PermissionEntities byUsernameAndGameLike = permissionRepository.findByUsernameAndGameLike(username, "%" + game.getId().toString() + "%");
        log.info("byUsernameAndGameLike:{}", byUsernameAndGameLike);
        if (byUsernameAndGameLike == null)
        {
            return false;
        }
        Map<String, Object> permission = JSON.parseObject(byUsernameAndGameLike.getPermission());
        for (Map.Entry<String, Object> entry :
                permission.entrySet())
        {
            if (entry.getValue() instanceof List<?>)
            {
                for (Object o : (List<?>) entry.getValue())
                {
                    if (o.equals("edit_preset_overview"))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public List<GaSelectEntities> hasProjectUserSelect(String projectId)
    {
        ArrayList<GaSelectEntities> select = new ArrayList<>();
        ArrayList<String> repeats = new ArrayList<>();
        GameEntities game = gameRepository.findByProjectId(projectId);
        if (game == null)
        {
            return select;
        }

        // 先拿到admin用户
        List<UserEntities> adminList = userRepository.findAllByStatusAndRoleEntitiesList(
                1,
                new RoleEntities(gaConfig.getAdminId(), null)
        );
        if (adminList.size() > 0)
        {
            for (UserEntities userEntities :
                    adminList)
            {
                select.add(new GaSelectEntities(
                        userEntities.getName(),
                        userEntities.getUsername()
                ));
                repeats.add(userEntities.getUsername());
            }
        }

        // 现取出所有ga用户生产速查表
        List<UserEntities> allUser = userRepository.findAllByStatusAndRoleEntitiesList(
                1,
                new RoleEntities(gaConfig.getRoleId(), null)
        );
        Map<String, String> userMap = new HashMap<>();
        for (UserEntities userEntities :
                allUser)
        {
            userMap.put(userEntities.getUsername(), userEntities.getName());
        }

        // 再拿到padmin用户
        StudioEntities studioEntities = studioRepository.findById(game.getStudio().getId()).orElse(null);
        assert studioEntities != null;
        select.add(new GaSelectEntities(
                userMap.get(studioEntities.getAdmin()),
                studioEntities.getAdmin()
        ));
        repeats.add(studioEntities.getAdmin());

        // 拿到所有拥有此游戏权限的用户生成下拉数据
        List<PermissionEntities> allByGameLike = permissionRepository.findAllByGameLike("%" + game.getId() + "%");
        for (PermissionEntities permissionEntities :
                allByGameLike)
        {
            // 过滤掉已经加入掉admin和padmin
            if (repeats.contains(permissionEntities.getUsername()))
            {
                continue;
            }
            select.add(new GaSelectEntities(
                    userMap.get(permissionEntities.getUsername()),
                    permissionEntities.getUsername()
            ));
        }
        return select;
    }
}
