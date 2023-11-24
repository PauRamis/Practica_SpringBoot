package com.esliceu.Practica_SpringBoot.Repos;

import com.esliceu.Practica_SpringBoot.entities.User;

public interface UserRepo {
    void save(String userName, String contrasenya);

    boolean userExists(String userName);

    User findUserByPassword(User userName, String p);
}
