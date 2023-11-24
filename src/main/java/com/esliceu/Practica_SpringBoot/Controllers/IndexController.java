package com.esliceu.Practica_SpringBoot.Controllers;

import com.esliceu.Practica_SpringBoot.services.DrawingService;
import com.esliceu.Practica_SpringBoot.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @Autowired
    HttpSession session;

    UserService userService;

    IndexController(UserService userService) {
        this.userService = userService;

    }

    //Login
    @GetMapping("/login")
    public String login(){
        System.out.println("Dins login");
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
}