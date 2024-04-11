package com.esliceu.Practica_SpringBoot.Repos;

import com.esliceu.Practica_SpringBoot.entities.Drawing;
import com.esliceu.Practica_SpringBoot.entities.Version;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class DrawingRepoImpl implements DrawingRepo {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int storeDrawing(Drawing drawing) {
        System.out.println("Saving...");
        KeyHolder keyHolder = new GeneratedKeyHolder();

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
        int drawingNewId = keyHolder.getKey().intValue();
        System.out.println("Drawing ID: " + drawingNewId);

        jdbcTemplate.update("insert into versions (id_drawing,json) values (?,?)",
                drawingNewId, drawing.getJson());

        return drawingNewId;
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

    @Override //May be out of use
    public Drawing getDrawingByName(String name) {
        String sql = "SELECT * FROM drawings WHERE name = ? ORDER BY id DESC LIMIT 1";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Drawing.class), name);
    }


    @Override
    public void deleteDrawing(int id) {
        String deleteVersionsSql = "DELETE FROM versions WHERE id_drawing = ?";
        jdbcTemplate.update(deleteVersionsSql, id);

        String deleteDrawingSql = "DELETE FROM drawings WHERE id = ?";
        jdbcTemplate.update(deleteDrawingSql, id);
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
        Version v = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Version.class), drawingId);
        if (v.getId() > 0) {
            return v;
        } else return null;
    }

    @Override
    public void overrideLatestVersion(int id_drawing, String newJson) {
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
        if (isPublic) {
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
    public Integer getUserIdByName(String userName){
        String sql = "SELECT id FROM usuaris WHERE userName = ? ORDER BY id DESC LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, userName);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    /**
     * La idea del share, es fer una nova taula amb la id del usuari i el dibuix que se li ha compartit.
     * A la Gallery, es mostrara un apartat on es veuran tots els dibuxios que tengui aquest usuari compartits.
     */
    @Override
    public void shareWithUsers(int id_drawing, int id_user){
        String sql = "insert into shared (id_drawing,id_user) values (?,?)";
        jdbcTemplate.update(sql, id_drawing, id_user);
    }

    @Override
    public List<Drawing> getSharedDrawings(int id) {
        /*String sql = "SELECT * FROM drawings WHERE JSON_CONTAINS(shared, CAST(? AS JSON), '$')";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Drawing.class), "[" + id + "]");*/
        return null;
    }

    @Override
    public boolean isPublic(int currentDrawingId) {
        String sql = "SELECT isPublic FROM drawings WHERE id = ?";
        int isPublicInt = jdbcTemplate.queryForObject(sql, Integer.class, currentDrawingId);
        return (isPublicInt == 1);
    }

}