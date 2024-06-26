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

    public int saveDrawing(Drawing drawing) {
        return drawingRepo.storeDrawing(drawing);
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

    public void shareWithUsers(int id_drawing, int id_user, boolean canEdit) {
        //
        drawingRepo.shareWithUsers(id_drawing, id_user, canEdit);
    }

    public List<Drawing> getSharedDrawings(int id) {return drawingRepo.getSharedDrawings(id);}

    public Drawing getDrawingByName(String name) {return drawingRepo.getDrawingByName(name);}

    public Integer getUserIdByName(String userName) {return drawingRepo.getUserIdByName(userName);}

    public boolean isPublic(int currentDrawingId) {return drawingRepo.isPublic(currentDrawingId);}

    public boolean getSharedPermisions(int currentDrawingId, int userId) {
        return drawingRepo.getSharedPermisions(currentDrawingId, userId);
    }

    public void removeShare(String currentDrawingId, int userId) {drawingRepo.removeShare(currentDrawingId, userId);}
}
