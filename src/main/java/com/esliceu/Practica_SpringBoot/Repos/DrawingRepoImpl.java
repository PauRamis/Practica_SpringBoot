package com.esliceu.Practica_SpringBoot.Repos;

import com.esliceu.Practica_SpringBoot.entities.Drawing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DrawingRepoImpl implements DrawingRepo{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void storeDrawing(Drawing drawing) {
        System.out.println(drawing.getJson());
        System.out.println(drawing.getUser());
        System.out.println(drawing.getName());
        jdbcTemplate.update("insert into drawings (json,user,name,isPublic) values (?,?,?,?)",
                drawing.getJson(), drawing.getUser(), drawing.getName(), drawing.isPublic());
    }

    @Override
    public List<Drawing> showDrawings() {
        String sql = "SELECT * FROM drawings";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Drawing.class));
    }

    @Override
    public List<Drawing> showPublicDrawings() {
        String sql = "SELECT * FROM drawings WHERE isPublic = 1 AND inTrash = 0";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Drawing.class));
    }

    @Override
    public List<Drawing> showUserDrawings(String userName) {
        String sql = "SELECT * FROM drawings WHERE user = ? AND inTrash = 0";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Drawing.class), userName);
    }

    @Override
    public Drawing getDrawingById(int id) {
        String sql = "SELECT * FROM drawings WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Drawing.class), id);
    }

    @Override
    public void deleteDrawing(int id){
        String sql = "DELETE FROM drawings WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void sendToTrash(int drawingId) {
        String sql = "UPDATE drawings SET inTrash = 1 WHERE id = ?";
        jdbcTemplate.update(sql, drawingId);
    }

    @Override
    public List<Drawing> showUserTrash(String userName) {
        String sql = "SELECT * FROM drawings WHERE user = ? AND inTrash = 1";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Drawing.class), userName);
    }

    @Override
    public void editDrawing(Drawing newDrawing) {
        int id = newDrawing.getId();
        String newJson = newDrawing.getJson();
        String newName = newDrawing.getName();

        String sql = "UPDATE drawings SET name = ?, jdbc = ? WHERE id = ?";
        Object[] params = {newName, newJson, id};
        jdbcTemplate.update(sql, params);
    }
}
