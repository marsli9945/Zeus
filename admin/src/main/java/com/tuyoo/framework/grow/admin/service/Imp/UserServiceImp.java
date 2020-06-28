package com.tuyoo.framework.grow.admin.service.Imp;

import com.tuyoo.framework.grow.admin.entities.UserEntities;
import com.tuyoo.framework.grow.admin.repository.UserRepository;
import com.tuyoo.framework.grow.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService
{
    @Autowired
    private UserRepository userRepository;

    public UserEntities findOne(Long id){
        return userRepository.findById(id).get();
    }

    public UserEntities save(UserEntities user){
        return userRepository.save(user);
    }

    public List<UserEntities> queryAll(){
        return userRepository.findAll();
    }
}
