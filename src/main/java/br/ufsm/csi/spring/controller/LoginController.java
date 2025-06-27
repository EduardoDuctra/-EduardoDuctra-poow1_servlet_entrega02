package br.ufsm.csi.spring.controller;

import br.ufsm.csi.spring.model.User;
import br.ufsm.csi.spring.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping
    public String index() {

        return "index";  // Página de login
    }

    @PostMapping("/login")
    public String login(Model model, String email, String password, HttpSession session) {
        try {
            LoginService loginService = new LoginService();


            if (loginService.autentication(email, password)) {
                System.out.printf("email: %s -- password: %s\n", email, password);

                // Busca o usuário e adiciona na sessão
                User user = loginService.db_user.searchEmail(email);
                session.setAttribute("user", user);

                return "redirect:/menu/dashboard";
            } else {


                model.addAttribute("msg", "Login ou senha incorreto!");
                model.addAttribute("email", email);
                return "index";
            }
        } catch (Exception e) {

            model.addAttribute("msg", e.getMessage());
            model.addAttribute("email", email);
            return "index";
        }
    }

}


