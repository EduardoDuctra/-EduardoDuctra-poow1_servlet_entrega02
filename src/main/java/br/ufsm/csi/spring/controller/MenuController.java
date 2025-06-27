package br.ufsm.csi.spring.controller;

import br.ufsm.csi.spring.model.Task;
import br.ufsm.csi.spring.model.User;
import br.ufsm.csi.spring.service.DataBaseServiceTask;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController {

    //Spring já cria um objeto do tipo service. Sem eu precisar fazer new DataBaseServiceTask()
    @Autowired
    private DataBaseServiceTask db_task;

    @GetMapping("/dashboard")
    public String showMenu(HttpSession session, Model model) {

        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/";
        }

        int userId = sessionUser.getId();

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDateTime;

        if (session.getAttribute("loginTime") == null) {
            formattedDateTime = today.format(formatter);
            session.setAttribute("loginTime", formattedDateTime);
        } else {
            formattedDateTime = (String) session.getAttribute("loginTime");
        }

        model.addAttribute("loginTime", formattedDateTime); // Passa para a view

        java.sql.Date sqlDate = java.sql.Date.valueOf(today);

        List<Task> pendingTasks = db_task.listTasksByDate(userId, sqlDate);

        //passo o objeto task para o form
        model.addAttribute("tasks", pendingTasks);

        return "menu";
    }

    @PostMapping("/concluded/{taskId}")
    public String concludeTask(@PathVariable("taskId") int taskId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        try {
            db_task.conCludedTask(taskId);
            System.out.println("Task com o ID concluída: " + taskId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "redirect:/menu/dashboard";
    }
}