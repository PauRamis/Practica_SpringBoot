package com.esliceu.Practica_SpringBoot.Repos;

import com.esliceu.Practica_SpringBoot.entities.User;

public interface UserRepo {
    void save(String usuari, String contrasenya);

    boolean userExists(User u);

    User findUserByPassword(User u, String p);
}
