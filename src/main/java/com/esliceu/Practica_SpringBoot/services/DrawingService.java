package com.esliceu.Practica_SpringBoot.services;

import com.esliceu.Practica_SpringBoot.Repos.DrawingRepo;
import com.esliceu.Practica_SpringBoot.entities.Drawing;
import com.esliceu.Practica_SpringBoot.entities.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrawingService {
    @Autowired
    DrawingRepo drawingRepo;

    public void saveDrawing(Drawing drawing) {
        drawingRepo.storeDrawing(drawing);
    }

    public List<Drawing> showDrawings() {
        return drawingRepo.showDrawings();
    }

    public List<Drawing> showPublicDrawings() {
        return drawingRepo.showPublicDrawings();
    }

    public List<Drawing> showUserDrawings(String user) {
        return drawingRepo.showUserDrawings(user);
    }

    public Drawing getDrawingById(int id) {
        return drawingRepo.getDrawingById(id);
    }

    public void deleteDrawing(int id){ drawingRepo.deleteDrawing(id); }

    public void editDrawing(Drawing newDrawing){ drawingRepo.editDrawing(newDrawing);}

    public void sendToTrash(int drawingId){ drawingRepo.sendToTrash(drawingId);}

    public List<Drawing> showUserTrash(String userName) {
        return drawingRepo.showUserTrash(userName);
    }

    public void retriveFromTrash(int drawingId) {
        drawingRepo.retriveFromTrash(drawingId);
    }

    public List<Version> showDrawingVersions(int currentDrawingId) {return drawingRepo.showDrawingVersions(currentDrawingId);}

    public Version getVersionById(int versionId) {
        return drawingRepo.getVersionById(versionId);
    }

    public Version getLatestVersion(int drawingId) {
        return drawingRepo.getLatestVersion(drawingId);
    }

    public void overrideLatestVersion(int id_drawing, String newJson) {drawingRepo.overrideLatestVersion(id_drawing, newJson);};

    public void shareWithUsers(int[] users, int id) {drawingRepo.shareWithUsers(users, id);}
}
