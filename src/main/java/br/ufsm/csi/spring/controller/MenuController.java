package br.ufsm.csi.spring.controller;

import br.ufsm.csi.spring.model.Task;
import br.ufsm.csi.spring.model.User;
import br.ufsm.csi.spring.service.DataBaseServiceTask;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private DataBaseServiceTask db_task;

    @GetMapping("/dashboard")
    public String showMenu(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/";
        }

        int userId = sessionUser.getId();
        model.addAttribute("userId", userId);

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
        model.addAttribute("tasks", pendingTasks);

        return "menu";
    }

    @PostMapping("/concluded")
    public String concludeTask(Task task, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        db_task.conCludedTask(task.getId());

        return "redirect:/menu/dashboard";
    }
}