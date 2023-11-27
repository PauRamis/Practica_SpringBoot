package com.esliceu.Practica_SpringBoot.Repos;

import com.esliceu.Practica_SpringBoot.entities.Drawing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DrawingRepoImpl implements DrawingRepo{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void storeDrawing(Drawing drawing) {
        jdbcTemplate.update("insert into usuaris (json,user) values (?,?)",
                drawing.getJson(), drawing.getUsuari());
    }

    @Override
    public List<Drawing> showDrawings() {
        String sql = "SELECT * FROM drawings";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Drawing.class));
    }

    @Override
    public List<Drawing> showUserDrawings(String userName) {
        String sql = "SELECT * FROM drawings WHERE user = ?";
        return jdbcTemplate.query(sql, new Object[]{userName}, new BeanPropertyRowMapper<>(Drawing.class));
    }

    @Override
    public Drawing getDrawingById(int id) {
        String sql = "SELECT * FROM drawings WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<>(Drawing.class));
    }

    @Override
    public void deleteDrawing(int id){
        String sql = "DELETE FROM drawings WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
