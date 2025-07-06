package br.ufsm.csi.spring.controller;

import br.ufsm.csi.spring.model.User;
import br.ufsm.csi.spring.service.DataBaseServiceTask;
import br.ufsm.csi.spring.service.DataBaseServiceUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {



    //Spring já cria um objeto do tipo service. Sem eu precisar fazer new DataBaseServiceUser()
    @Autowired
    private DataBaseServiceUser db_user;
    @Autowired
    private DataBaseServiceTask db_task;


    @GetMapping("/register-user")
    public String registerUser(Model model) {
        //envio um objeto USER vazio para o formulario de cadastro. Passo pq tenho o atribute no jSP
        model.addAttribute("user", new User());
        return "register-user";  // JSP de cadastro
    }

    @PostMapping("/register-user")
    //@ModelAttribute("user") -> com isso pego o modelo user do formulario (os campos preenchidos)
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {

            db_user.insertUser(user);

            System.out.println("=== Dados do Usuário Inserido ===");
            System.out.println("ID: " + user.getId());
            System.out.println("Nome: " + user.getName());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Senha: " + user.getPassword());
            System.out.println("=================================");

            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("message", "Erro ao cadastrar usuário: " + e.getMessage());
            return "register-user";
        }
    }



    //pega o usuario pela sessção e se estiver logado redireciona pro jSP
    @GetMapping("/update-user")
    public String updateUser(HttpSession session, Model model) {

        User loggedUser = (User) session.getAttribute("user");


        model.addAttribute("user", loggedUser);

        System.out.println("=== Dados do Usuário GET atualizar ===");
        System.out.println("ID: " + loggedUser.getId());
        System.out.println("=================================");

        return "update-user";
    }

    //@ModelAttribute("user") -> com isso pego o modelo user do formulario (os campos preenchidos)
    //Atualizo e setto o novo usuario a sessão
    @PostMapping("/update-user")
    public String updateUser(@ModelAttribute("user") User user, HttpSession session, Model model){
        try {

            db_user.updateUser(user);
            session.setAttribute("user", user);

            System.out.println("=== Dados do Usuário POST atualizar ===");
            System.out.println("ID: " + user.getId());
            System.out.println("Novo nome: " + user.getName());
            System.out.println("Novo Email: " + user.getEmail());
            System.out.println("Nova Senha: " + user.getPassword());
            System.out.println("=================================");

            return "redirect:/menu/dashboard";

        } catch (Exception e) {
            model.addAttribute("message", "Erro ao atualizar usuário: " + e.getMessage());
            model.addAttribute("user", user);
            return "update-user";
        }
    }

    @PostMapping("/delete-user")
    public String deleteUser(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");

        try {
            int id = sessionUser.getId();

            db_task.deletTaskByUserId(id);
            db_user.deleteUser(id);

            // Invalida a sessão, pois o usuário foi deletado
            session.invalidate();

            System.out.println("=== Usuário deletado ===");
            System.out.println("ID: " + id);
            System.out.println("========================");

            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("message", "Erro ao deletar usuário: " + e.getMessage());
            return "update-user";
        }
    }
}






