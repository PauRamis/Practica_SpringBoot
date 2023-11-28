package com.esliceu.Practica_SpringBoot.services;

import com.esliceu.Practica_SpringBoot.Repos.DrawingRepo;
import com.esliceu.Practica_SpringBoot.entities.Drawing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

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

    public List<Drawing> showUserDrawings(String user) {
        return drawingRepo.showUserDrawings(user);
    }

    public Drawing getDrawingById(int id) {
        return drawingRepo.getDrawingById(id);
    }

    public void deleteDrawing(int id){ drawingRepo.deleteDrawing(id); }
}
