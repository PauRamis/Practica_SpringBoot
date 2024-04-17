package com.esliceu.Practica_SpringBoot.Controllers;

import com.esliceu.Practica_SpringBoot.entities.Drawing;
import com.esliceu.Practica_SpringBoot.entities.User;
import com.esliceu.Practica_SpringBoot.entities.Version;
import com.esliceu.Practica_SpringBoot.services.DrawingService;
import com.esliceu.Practica_SpringBoot.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

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

    @GetMapping("/")
    public String homepage(){
        return "login";
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
    @ResponseBody
    public ResponseEntity<Integer> drawPost(@RequestBody Map<String, Object> payload){
        //get user
        String userName = (String) session.getAttribute("userName");
        System.out.println("username: " + userName);
        User actualUser = userService.findUserByuserName(userName);

        //save
        Drawing savedDrawing = new Drawing();
        savedDrawing.setJson((String) payload.get("drawingInput"));
        savedDrawing.setName((String) payload.get("drawingName"));
        savedDrawing.setUser(actualUser.getUserName());
        savedDrawing.setPublic((Boolean) payload.get("isPublic"));
        int newDrawingId = drawingService.saveDrawing(savedDrawing);
        System.out.println("currentDrawingId = "+newDrawingId);

        return ResponseEntity.ok(newDrawingId);
    }

    @PostMapping("/draw/version")
    public ResponseEntity<Integer> drawPostVersion(@RequestBody Map<String, Object> payload){
        //get user
        String userName = (String) session.getAttribute("userName");
        System.out.println("username: " + userName);
        User actualUser = userService.findUserByuserName(userName);

        //save
        Drawing savedDrawing = new Drawing();
        savedDrawing.setJson((String) payload.get("drawingInput"));
        savedDrawing.setName((String) payload.get("drawingName"));
        savedDrawing.setUser(actualUser.getUserName()); //Need?
        savedDrawing.setPublic((Boolean) payload.get("isPublic"));
        savedDrawing.setId(Integer.parseInt(payload.get("currentDrawingId").toString()));
        System.out.println("currentDrawingId"+ payload.get("currentDrawingId"));
        drawingService.editDrawing(savedDrawing);

        return ResponseEntity.ok(Integer.valueOf(payload.get("currentDrawingId").toString()));
    }

    //Gallery
    @GetMapping("/gallery")
    public String gallery(Model model){
        String userName = (String) session.getAttribute("userName");
        User currentUser = userService.findUserByuserName(userName);

        List<Drawing> userDrawings = drawingService.showUserDrawings(userName);
        List<Drawing> publicDrawings = drawingService.showPublicDrawings();
        List<Drawing> sharedDrawings = drawingService.getSharedDrawings(currentUser.getId());

        model.addAttribute("userName", userName);
        model.addAttribute("userDrawings", userDrawings);
        model.addAttribute("publicDrawings", publicDrawings);
        model.addAttribute("sharedDrawings", sharedDrawings);
        return "gallery";
    }

    //View
    @GetMapping("/view")
    public String view(Model model,
                       @RequestParam int currentDrawingId){

        Drawing currentDrawing = drawingService.getDrawingById(currentDrawingId);
        model.addAttribute("currentDrawingId", currentDrawingId);
        model.addAttribute("drawingUser", currentDrawing.getUser());

        Version currentVersion = drawingService.getLatestVersion(currentDrawingId);
        model.addAttribute("currentJson", currentVersion.getJson());

        //Comprobam si es el autor del actual dibuix
        String currentUser = (String) session.getAttribute("userName");
        boolean author = currentUser.equals(currentDrawing.getUser());
        model.addAttribute("author", author);

        boolean canWrite = false;
        if (!author){
            //Comprobam si te permis per dibuixar
            int userId = userService.findUserByuserName(currentUser).getId();
            canWrite = drawingService.getSharedPermisions(currentDrawingId, userId);
            System.out.println("Can write? " + canWrite);
            model.addAttribute("canWrite", canWrite);
        }
        return "view";
    }

    @PostMapping("/view")
    public String viewPost(Model model,
                           @RequestParam String currentDrawingId){
        String author = drawingService.getDrawingById(Integer.parseInt(currentDrawingId)).getUser();
        String currentUser = (String) session.getAttribute("userName");
        if (author.equals(currentUser)){
            //Comprobam si es l'autor
            drawingService.sendToTrash(Integer.parseInt(currentDrawingId));
        } else {
            //Comprobam si te el dibux compartit, i l'eliminam.
            System.out.println("To delete");
            int userId = userService.findUserByuserName(currentUser).getId();
            drawingService.removeShare(currentDrawingId, userId);
        }
        return "redirect:/gallery";
    }

    //Versions
    @GetMapping("/versions")
    public String versions(Model model,
                           @RequestParam int currentDrawingId){

        String userName = (String) session.getAttribute("userName");
        List<Version> DrawingVersions = drawingService.showDrawingVersions(currentDrawingId);

        model.addAttribute("DrawingVersions", DrawingVersions);
        model.addAttribute("currentDrawingId", currentDrawingId);

        return "versions";
    }

    @PostMapping("/versions")
    public String versionsPost(Model model){
        return null;
    }

    //Igual a view, pero amb una versió especifica
    @GetMapping("/versionView")
    public String versionView(Model model,
                              @RequestParam int versionId,
                              @RequestParam int currentDrawingId){

        Version currentVersion = drawingService.getVersionById(versionId);
        model.addAttribute("currentDrawingId", currentDrawingId);
        model.addAttribute("versionId", versionId);
        model.addAttribute("currentJson", currentVersion.getJson());
        model.addAttribute("timeStamp", currentVersion.getTimeStamp());

        //Check if allowed to restore
        String currentUser = (String) session.getAttribute("userName");
        String authorName = drawingService.getDrawingById(currentDrawingId).getUser();
        boolean author = currentUser.equals(authorName);
        model.addAttribute("allowed", author);

        //Maybe he can edit
        if (!author){
            boolean canWrite = false;
            int userId = userService.findUserByuserName(currentUser).getId();
            canWrite = drawingService.getSharedPermisions(currentDrawingId, userId);
            System.out.println("Can write? " + canWrite);
            model.addAttribute("allowed", canWrite);
        }

        return "versionView";
    }

    @PostMapping("/versionView")
    public String versionViewPost(Model model,
                                  @RequestParam int versionId,
                                  @RequestParam int currentDrawingId){
        //Comprobam si es el autor del actual dibuix
        String currentUser = (String) session.getAttribute("userName");
        String authorName = drawingService.getDrawingById(currentDrawingId).getUser();
        boolean allowed = currentUser.equals(authorName);
        //model.addAttribute("allowed", author);

        if (!allowed){
            boolean canWrite = false;
            //Comprobam si te permis per editar
            int userId = userService.findUserByuserName(currentUser).getId();
            canWrite = drawingService.getSharedPermisions(currentDrawingId, userId);
            System.out.println("Can write? " + canWrite);
            allowed = canWrite;
            //model.addAttribute("allowed", canWrite);
        }
        if (allowed){
            //Restaurar Versio
            Version currentVersion = drawingService.getVersionById(versionId);
            drawingService.overrideLatestVersion(currentDrawingId, currentVersion.getJson());
        }
        return "redirect:/gallery";
    }

    @PostMapping("/versionCopy")
    public String versionCopyPost(Model model,
                                  @RequestParam int versionId,
                                  @RequestParam int currentDrawingId,
                                  RedirectAttributes redirectAttributes){

        Version currentVersion = drawingService.getVersionById(versionId);
        Drawing currentDrawing = drawingService.getDrawingById(currentDrawingId);

        //Create drawing copy for new user
        String userName = (String) session.getAttribute("userName");
        System.out.println("username: " + userName);
        User actualUser = userService.findUserByuserName(userName);

        Drawing savedDrawing = new Drawing();
        String drawingName = "Copy of " + currentDrawing.getName();
        savedDrawing.setName(drawingName);
        savedDrawing.setUser(actualUser.getUserName());
        savedDrawing.setJson(currentVersion.getJson());
        savedDrawing.setPublic(false);

        //Save Drawing
        int copiedDrawingId = drawingService.saveDrawing(savedDrawing);

        //Redirecto to edit
        redirectAttributes.addAttribute("currentDrawingId", copiedDrawingId);
        return "redirect:/edit";
    }

    //Edit
    @GetMapping("/edit")
    public String edit(Model model,
                       @RequestParam("currentDrawingId")
                       int currentDrawingId){
        Drawing currentDrawing = drawingService.getDrawingById(currentDrawingId);
        model.addAttribute("currentDrawingId", currentDrawingId);
        model.addAttribute("drawingUser", currentDrawing.getUser());
        model.addAttribute("drawingName", currentDrawing.getName());
        boolean isPublic = drawingService.isPublic(currentDrawingId);
        model.addAttribute("wasPublic", isPublic);
        System.out.println("is public?: "+isPublic);

        Version currentVersion = drawingService.getLatestVersion(currentDrawingId);
        model.addAttribute("currentJson", currentVersion.getJson());
        System.out.println(currentDrawing.isPublic());
        return "edit";
    }

    @PostMapping("/edit")
    public String editPost(Model model,
                           @RequestParam String drawingInput,
                           @RequestParam int currentDrawingId,
                           //@RequestParam(value = "isPublic", defaultValue = "false") boolean isPublic,
                           @RequestParam(name = "isPublic", required = false) String isPublicParam,
                           @RequestParam String DrawingName){

        boolean isPublic = "true".equals(isPublicParam);

        String userName = (String) session.getAttribute("userName");
        User actualUser = userService.findUserByuserName(userName);

        Drawing savedDrawing = new Drawing();
        savedDrawing.setJson(drawingInput);
        savedDrawing.setName(DrawingName);
        savedDrawing.setUser(actualUser.getUserName());
        savedDrawing.setPublic(isPublic);
        savedDrawing.setId(currentDrawingId);

        drawingService.editDrawing(savedDrawing);
        return "redirect:/gallery";
    }

    //Copy
    @GetMapping("/copy")
    public String copy(Model model,
                       @RequestParam("currentDrawingId") int currentDrawingId,
                       RedirectAttributes redirectAttributes){
        //Get original drawing
        Drawing currentDrawing = drawingService.getDrawingById(currentDrawingId);
        Version currentVersion = drawingService.getLatestVersion(currentDrawingId);

        //Create drawing copy for new user
        String userName = (String) session.getAttribute("userName");
        System.out.println("username: " + userName);
        User actualUser = userService.findUserByuserName(userName);

        Drawing savedDrawing = new Drawing();
        String drawingName = "Copy of " + currentDrawing.getName();
        savedDrawing.setName(drawingName);
        savedDrawing.setUser(actualUser.getUserName());
        savedDrawing.setJson(currentVersion.getJson());
        savedDrawing.setPublic(false);
        int copiedDrawingId = drawingService.saveDrawing(savedDrawing);

        redirectAttributes.addAttribute("currentDrawingId", copiedDrawingId);
        return "redirect:/edit";
    }

    //Trash can
    @GetMapping("/trash")
    public String trash(Model model){
        String userName = (String) session.getAttribute("userName");
        List<Drawing> userTrash = drawingService.showUserTrash(userName);

        model.addAttribute("userName", userName);
        model.addAttribute("userTrash", userTrash);
        return "trash";
    }

    @PostMapping("/trash")
    public String trashPost(Model model,
                            @RequestParam("formAction") String formAction,
                            @RequestParam("currentDrawingId")
                            String currentDrawingId) {
        String author = drawingService.getDrawingById(Integer.parseInt(currentDrawingId)).getUser();
        String currentUser = (String) session.getAttribute("userName");
        if (author.equals(currentUser)) {
            if ("Esborrar".equals(formAction)) {
                drawingService.deleteDrawing(Integer.parseInt(currentDrawingId));
            } else if ("Recuperar".equals(formAction)) {
                drawingService.retriveFromTrash(Integer.parseInt(currentDrawingId));
            }
        }
        return "redirect:/trash";
    }

    //Share
    @GetMapping("/share")
    public String share(Model model,
                       @RequestParam int currentDrawingId){

        model.addAttribute("currentDrawingId", currentDrawingId);
        return "share";
    }

    @PostMapping("/share")
    public String sharePost(Model model,
                           @RequestParam Integer currentDrawingId,
                            @RequestParam String shareUsers,
                            @RequestParam(value = "canEdit", defaultValue = "false") boolean canEdit){

        String[] usersAr = shareUsers.split(" ");
        List<String> existingUsers = new ArrayList<>();
        List<String> nonExistingUsers = new ArrayList<>();

        for (int i = 0; i < usersAr.length; i++) {
            Integer id = drawingService.getUserIdByName(usersAr[i]);
            if (id != null) {
                existingUsers.add(usersAr[i]);
            } else {
                nonExistingUsers.add(usersAr[i]);
            }
        }
        String existingUsersString = String.join(" ", existingUsers);
        String nonExistingUsersString = String.join(" ", nonExistingUsers);

        System.out.println("Current id: "+currentDrawingId);
        System.out.println("Existing users: "+existingUsersString);
        System.out.println("NonExisting: "+nonExistingUsersString);

        if (nonExistingUsers.isEmpty() && !existingUsers.isEmpty()){
            model.addAttribute("sharedMessage", "Shared with: " + existingUsers);
        } else {
            model.addAttribute("sharedMessage", "Error, this users were not found: " + nonExistingUsers);
        }

        Set<Integer> checkedUsers = new HashSet<Integer>();

        System.out.println("Can edit? " + canEdit);
        for (int i = 0; i < existingUsers.size(); i++) {
            User user = userService.findUserByuserName(existingUsers.get(i));
            int userId =  user.getId();
            if (!checkedUsers.contains(userId)){
                drawingService.shareWithUsers(currentDrawingId, userId, canEdit);
                checkedUsers.add(userId);
            }
        }

        return "share";
    }
}
