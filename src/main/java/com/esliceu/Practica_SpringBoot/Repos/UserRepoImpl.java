package com.esliceu.Practica_SpringBoot.Repos;

import com.esliceu.Practica_SpringBoot.entities.Drawing;
import com.esliceu.Practica_SpringBoot.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
        //logUser es com userExists però comprobant també la password
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
        String sql = "SELECT * FROM userName WHERE userName = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userName}, new BeanPropertyRowMapper<>(User.class));
    }
}
