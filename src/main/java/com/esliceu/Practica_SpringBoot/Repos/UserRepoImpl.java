package com.esliceu.Practica_SpringBoot.Repos;

import com.esliceu.Practica_SpringBoot.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepoImpl implements UserRepo{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void save(String userName, String password) {
        jdbcTemplate.update("insert into usuaris (userName,password) values (?,?)",
                userName, password);
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
