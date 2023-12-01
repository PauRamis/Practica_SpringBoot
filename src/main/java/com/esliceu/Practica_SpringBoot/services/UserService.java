package com.esliceu.Practica_SpringBoot.services;

import com.esliceu.Practica_SpringBoot.Repos.UserRepo;
import com.esliceu.Practica_SpringBoot.entities.Drawing;
import com.esliceu.Practica_SpringBoot.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    public void saveUser(String userName, String password) {
        userRepo.save(userName, password);
    }

    public boolean logUser(String userName, String password) {
        return userRepo.logUser(userName, password);
    }

    public boolean userExists(String userName) {
        return userRepo.userExists(userName);
    }

    public User findUserByuserName(String userName){
        return userRepo.findUserByuserName(userName);
    }
}
