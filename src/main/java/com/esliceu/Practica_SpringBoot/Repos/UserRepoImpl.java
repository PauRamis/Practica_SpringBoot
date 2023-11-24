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
    public boolean userExists(String userName) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM usuaris WHERE userName = ?",
                new Object[]{userName},
                Integer.class
        );
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean logUser(String userName, String password) {
        //logUser es diferent a userExists perque també tenim en compte la password
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM usuaris WHERE userName = ? AND password = ?",
                new Object[]{userName, password},
                Integer.class
        );
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public User findUserByuserName(String userName) {
        /*String userResult = jdbcTemplate.queryForObject(
                "SELECT userName FROM usuaris WHERE userName = ?",
                new Object[]{userName},
                String.class
        );

        return userResult;*/
        return null;
    }
}
