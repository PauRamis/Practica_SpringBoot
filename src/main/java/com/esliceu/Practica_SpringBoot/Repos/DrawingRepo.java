package com.esliceu.Practica_SpringBoot.Repos;

import com.esliceu.Practica_SpringBoot.entities.Drawing;

import java.util.List;

public interface DrawingRepo {
    void storeDrawing(Drawing drawing);

    List<Drawing> showDrawings();

    List<Drawing> showUserDrawings(String user);

    Drawing getDrawingById(int id);

    boolean idUsed(int id);

    void deleteDrawing(int id);
}
