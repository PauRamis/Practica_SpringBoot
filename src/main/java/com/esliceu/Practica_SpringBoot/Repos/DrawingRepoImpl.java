package com.esliceu.Practica_SpringBoot.Repos;

import com.esliceu.Practica_SpringBoot.entities.Drawing;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DrawingRepoImpl implements DrawingRepo{
    @Override
    public void storeDrawing(Drawing drawing) {

    }

    @Override
    public List<Drawing> showDrawings() {
        return null;
    }

    @Override
    public List<Drawing> showUserDrawings(String user) {
        return null;
    }

    @Override
    public Drawing getDrawingById(int id) {
        return null;
    }

    @Override
    public boolean idUsed(int id) {
        return false;
    }

    @Override
    public void deleteDrawing(int id) {

    }
}
