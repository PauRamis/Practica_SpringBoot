package com.esliceu.Practica_SpringBoot.Repos;

import com.esliceu.Practica_SpringBoot.entities.Drawing;
import com.esliceu.Practica_SpringBoot.entities.Version;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface DrawingRepo {
    void storeDrawing(Drawing drawing);

    List<Drawing> showDrawings();

    List<Drawing> showUserDrawings(String userName);

    Drawing getDrawingById(int id);

    Drawing getDrawingByName(String name);

    void deleteDrawing(int id);

    Version getLatestVersion(int drawingId);

    void overrideLatestVersion(int id_drawing, String newJson);

    void editDrawing(Drawing newDrawing);

    List<Drawing> showPublicDrawings();

    void sendToTrash(int drawingId);

    List<Drawing> showUserTrash(String userName);

    void retriveFromTrash(int drawingId);

    List<Version> showDrawingVersions(int drawingId);

    Version getVersionById(int versionId);

    void shareWithUsers(int[] users, int id) throws JsonProcessingException;

    List<Drawing> getSharedDrawings(int id);
}
