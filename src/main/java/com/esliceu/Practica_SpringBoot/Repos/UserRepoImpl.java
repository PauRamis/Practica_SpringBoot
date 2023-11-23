package com.esliceu.Practica_SpringBoot.Repos;

import com.esliceu.Practica_SpringBoot.entities.User;

public class UserRepoImpl implements UserRepo{
    @Override
    public void save(String usuari, String contrasenya) {

    }

    @Override
    public boolean userExists(User u) {
        return false;
    }

    @Override
    public User findUserByPassword(User u, String p) {
        return null;
    }
}
