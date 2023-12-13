package com.esliceu.Practica_SpringBoot.Repos;

import com.esliceu.Practica_SpringBoot.entities.Drawing;

import java.util.List;

public interface DrawingRepo {
    void storeDrawing(Drawing drawing);

    List<Drawing> showDrawings();

    List<Drawing> showUserDrawings(String userName);

    Drawing getDrawingById(int id);

    void deleteDrawing(int id);

    void editDrawing(Drawing newDrawing);

    List<Drawing> showPublicDrawings();

    void sendToTrash(int drawingId);

    List<Drawing> showUserTrash(String userName);

    void retriveFromTrash(int drawingId);
}
