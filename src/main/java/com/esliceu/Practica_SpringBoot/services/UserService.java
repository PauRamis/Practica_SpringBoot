package com.esliceu.Practica_SpringBoot.services;

import com.esliceu.Practica_SpringBoot.Repos.UserRepo;
import com.esliceu.Practica_SpringBoot.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    public void saveUser(String user, String cont) {
        userRepo.save(user, cont);
    }

    public boolean userExists(String userName) {
        return userRepo.userExists(userName);
    }

    public User findUserByPassword(User u, String p){
        return userRepo.findUserByPassword(u, p);
    }
}
