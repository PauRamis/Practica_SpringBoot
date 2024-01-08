package com.esliceu.Practica_SpringBoot.Repos;

import com.esliceu.Practica_SpringBoot.entities.Drawing;
import com.esliceu.Practica_SpringBoot.entities.Version;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class DrawingRepoImpl implements DrawingRepo{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void storeDrawing(Drawing drawing) {
        System.out.println("Saving...");
        KeyHolder keyHolder = new GeneratedKeyHolder();

        /*jdbcTemplate.update("insert into drawings (json,user,name,isPublic) values (?,?,?,?)",
                drawing.getJson(), drawing.getUser(), drawing.getName(), drawing.isPublic(),
                keyHolder
        );*/
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "insert into drawings (json,user,name,isPublic) values (?,?,?,?)",
                            Statement.RETURN_GENERATED_KEYS
                    );
                    ps.setString(1, drawing.getJson());
                    ps.setString(2, drawing.getUser());
                    ps.setString(3, drawing.getName());
                    ps.setBoolean(4, drawing.isPublic());
                    return ps;
                },
                keyHolder
        );

        //Versions
        System.out.println(keyHolder);
        int drawingNewId = keyHolder.getKey().intValue();
        System.out.println(drawingNewId);

        jdbcTemplate.update("insert into versions (id_drawing,json) values (?,?)",
                drawingNewId, drawing.getJson());
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
    public void retriveFromTrash(int drawingId) {
        String sql = "UPDATE drawings SET inTrash = 0 WHERE id = ?";
        jdbcTemplate.update(sql, drawingId);
    }

    @Override
    public List<Version> showDrawingVersions(int drawingId) {
        String sql = "SELECT * FROM versions WHERE id_drawing = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Version.class), drawingId);
    }

    @Override
    public Version getVersionById(int versionId) {
        System.out.println(versionId);
        String sql = "SELECT * FROM versions WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Version.class), versionId);
    }

    @Override
    public Version getLatestVersion(int drawingId) {
        System.out.println(drawingId);
        String sql = "SELECT * FROM versions WHERE id_drawing = ? ORDER BY id DESC LIMIT 1";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Version.class), drawingId);
    }

    @Override
    public void overrideLatestVersion(int id_drawing, String newJson){
        System.out.println("overrideLatestVersion...");
        System.out.println(id_drawing);
        System.out.println(newJson);

        jdbcTemplate.update("insert into versions (id_drawing,json) values (?,?)",
                id_drawing, newJson);
    }

    @Override
    public void editDrawing(Drawing newDrawing) {
        System.out.println("Editing...");
        int id = newDrawing.getId();
        String newJson = newDrawing.getJson();
        String newName = newDrawing.getName();
        Boolean isPublic = newDrawing.isPublic();
        int tinyint = 0;
        if (isPublic){
            tinyint = 1;
        }
        String sql = "UPDATE drawings SET name = ?, json = ?, isPublic = ? WHERE id = ?";
        Object[] params = {newName, newJson, tinyint, id};
        jdbcTemplate.update(sql, params);

        //New version
        jdbcTemplate.update("insert into versions (id_drawing,json) values (?,?)",
                id, newJson);
    }

    @Override
    public void shareWithUsers(int[] users, int id) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String usersJson = objectMapper.writeValueAsString(users);
        System.out.println(usersJson);
        String sql = "Update drawings SET shared = ? WHERE id = ?";

        jdbcTemplate.update(sql, usersJson, id);
    }

}