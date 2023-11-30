package com.esliceu.Practica_SpringBoot.Controllers;

import com.esliceu.Practica_SpringBoot.entities.Drawing;
import com.esliceu.Practica_SpringBoot.entities.User;
import com.esliceu.Practica_SpringBoot.services.DrawingService;
import com.esliceu.Practica_SpringBoot.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class IndexController {
    @Autowired
    HttpSession session;

    UserService userService;
    DrawingService drawingService;

    IndexController(UserService userService, DrawingService drawingService) {
        this.userService = userService;
        this.drawingService = drawingService;
    }

    //Login
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(Model model,
                           @RequestParam String userName,
                           @RequestParam String password) {

        if(userService.logUser(userName, password)){
            session.setAttribute("userName", userName);
            return "draw";
        } else {
            model.addAttribute("errType", "Usuari no existent");
            model.addAttribute("errMsg", "Aseguri de que les dades intoduides son correctes, " +
                    "o crei un nou compte.");
            return "logErr";
            //return "redirect:/logErr";
        }
    }

    //Registre
    @GetMapping("/registre")
    public String registre(){
        return "registre";
    }

    @PostMapping("/registre")
    public String registrePost(Model model,
                            @RequestParam String userName,
                            @RequestParam String password) {
        if (userService.userExists(userName)) {
            model.addAttribute("errType", "Usuari ja existent");
            model.addAttribute("errMsg", "Provi amb un altre nom o amb variacións del mateix");
            return "logErr";

        } else if (userName.length() < 3){
            model.addAttribute("errType", "Username no valid");
            model.addAttribute("errMsg", "L'Usuari ha de tenir minim 3 caracters");
            return "logErr";

        } else if (password.length() < 5){
            model.addAttribute("errType", "Password no valida");
            model.addAttribute("errMsg", "La contrasenya ha de tenir minim 5 caracters");
            return "logErr";

        } else {
            System.out.println("Usuari guardat");
            userService.saveUser(userName, password);
            return "draw";
        }
    }

    //Drawing
    @GetMapping("/draw")
    public String draw(){
        return "draw";
    }

    @PostMapping("/draw")
    public String drawPost(Model model,
                           @RequestParam String drawingInput,
                           @RequestParam String DrawingName){

        String userName = (String) session.getAttribute("userName");
        System.out.println("username: " + userName);
        User actualUser = userService.findUserByuserName(userName);

        Drawing savedDrawing = new Drawing();
        savedDrawing.setJson(drawingInput);
        savedDrawing.setName(DrawingName);
        savedDrawing.setUsuari(actualUser);
        drawingService.saveDrawing(savedDrawing);
        return null;
    }

    //Gallery
    @GetMapping("/gallery")
    public String gallery(Model model){
        String userName = (String) session.getAttribute("userName");
        List<Drawing> allDrawings = drawingService.showDrawings();
        List<Drawing> userDrawings = drawingService.showUserDrawings(userName);
        System.out.println(userName);

        model.addAttribute("userName", userName);
        model.addAttribute("allDrawings", allDrawings);
        model.addAttribute("userDrawings", userDrawings);
        return "gallery";
    }

    @PostMapping("/gallery")
    public String galleryPost(Model model){
        return null;
    }

    //View
    @GetMapping("/view")
    public String view(Model model,
                       @RequestParam(name = "currentDrawingId")
                       int currentDrawingId){

        model.addAttribute("currentDrawingId", currentDrawingId);
        Drawing currentDrawing = drawingService.getDrawingById(currentDrawingId);
        model.addAttribute("currentJson", currentDrawing.getJson());
        System.out.println("Current JSON: " + currentDrawing.getJson());
        return "view";
    }

    @PostMapping("/view")
    public String viewPost(Model model){
        return null;
    }
}