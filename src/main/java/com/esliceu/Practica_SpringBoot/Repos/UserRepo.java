package com.esliceu.Practica_SpringBoot.Repos;

import com.esliceu.Practica_SpringBoot.entities.User;

public interface UserRepo {
    void save(String userName, String password);

    boolean userExists(String userName);

    User findUserByuserName(String userName);

    boolean logUser(String userName, String password);
}
