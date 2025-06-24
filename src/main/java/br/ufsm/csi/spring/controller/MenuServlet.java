package br.ufsm.csi.spring.controller;


import br.ufsm.csi.spring.model.Task;
import br.ufsm.csi.spring.model.User;
import br.ufsm.csi.spring.service.DataBaseServiceTask;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@WebServlet("/menu")
public class MenuServlet extends HttpServlet {

    private DataBaseServiceTask db_task;

    @Override
    public void init() throws ServletException {
        super.init();
        db_task = new DataBaseServiceTask();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);


        if (session == null) {
            // Se não tiver sessão, redireciona para login
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            // Usuário não está logado, redireciona para login
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        int userId = sessionUser.getId();
        request.setAttribute("userId", userId);

        System.out.println("ID do usuário vindo da sessão: " + userId);

        LocalDate today = LocalDate.now();
        Date sqlDate = java.sql.Date.valueOf(today);

        // Define loginTime na sessão, se não existir ainda
        if (session.getAttribute("loginTime") == null) {


            // Formata para exibir
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDateTime = today.format(formatter);
            session.setAttribute("loginTime", formattedDateTime);

        }

        System.out.println("Codigo Usuario para listar tarefas dia: " + userId);
        List<Task> pendingTasks = db_task.listTasksByDate(userId, sqlDate);

        for (Task task : pendingTasks) {
            System.out.println("Task ID: " + task.getId()
                    + ", Título: " + task.getTitle());
        }

        request.setAttribute("tasks", pendingTasks);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/menu.jsp");
        dispatcher.forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        request.setCharacterEncoding("UTF-8");

                if ("concluded".equals(action)) {
                        String idStr = request.getParameter("taskId");
                        System.out.println("Código da Task no POST para concluir: " + idStr);

                        try {
                            int id = Integer.parseInt(idStr);
                            db_task.conCludedTask(id);
                        } catch (NumberFormatException e) {
                            System.out.println("ID inválido para deleção: " + idStr);
                        }
                    response.sendRedirect(request.getContextPath() + "/menu");
                    }
    }
}


