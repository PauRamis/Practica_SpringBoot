package com.esliceu.Practica_SpringBoot.services;

import com.esliceu.Practica_SpringBoot.Repos.DrawingRepo;
import com.esliceu.Practica_SpringBoot.entities.Drawing;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Random;

public class DrawingService {
    @Autowired
    DrawingRepo drawingRepo;

    public void saveDrawing(Drawing drawing) {
        int id = genRandId();
        drawing.setId(id);
        System.out.println("id = " + id);
        drawingRepo.storeDrawing(drawing);
    }

    private int genRandId() {
        Random rand = new Random();
        int id = rand.nextInt(10000);

        System.out.println("id." + id);

        //TODO second go breaks
        /*if(idUsed(id)) {
            id = genRandId();
        }*/

        System.out.println("id generated:" + id);
        return id;
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

    public boolean idUsed(int id){
        return drawingRepo.idUsed(id);
    }

    public void deleteDrawing(int id){ drawingRepo.deleteDrawing(id); }
}
