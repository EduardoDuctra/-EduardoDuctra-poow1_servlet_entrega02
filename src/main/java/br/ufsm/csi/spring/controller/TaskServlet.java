package br.ufsm.csi.spring.controller;


import br.ufsm.csi.spring.model.Category;
import br.ufsm.csi.spring.model.Task;
import br.ufsm.csi.spring.model.User;
import br.ufsm.csi.spring.service.DataBaseServiceCategory;
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
import java.util.List;

@WebServlet("/tasks")
public class TaskServlet extends HttpServlet {

    private DataBaseServiceCategory db_category;
    private DataBaseServiceTask db_task;

    @Override
    public void init() throws ServletException {
        super.init();

        db_task = new DataBaseServiceTask();
        db_category = new DataBaseServiceCategory();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        System.out.println("Action recebida no doGet: " + action);

        if ("create".equals(action)) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/create-task.jsp");
            dispatcher.forward(request, response);
            return;
        } else if ("listPending".equals(action)) {

            HttpSession session = request.getSession();


            User user = (User) session.getAttribute("user");

            System.out.println("Codigo Usuario: " + user.getId());
            List<Task> pendingTasks = db_task.listTasks(user.getId());


            for (Task task : pendingTasks) {

                System.out.println("Task ID: " + task.getId()
                        + ", Título: " + task.getTitle()
                        + ", Categoria ID: " + (task.getCategory() != null ? task.getCategory().getId() : "null"));
            }

            request.setAttribute("tasks", pendingTasks);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/list-pending.jsp");
            dispatcher.forward(request, response);
            return;
        } else if ("listConcluded".equals(action)) {

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            System.out.println("Codigo Usuario: " + user.getId());
            List<Task> concludetTasks = db_task.listConcludedTasks(user.getId());


            for (Task task : concludetTasks) {

                System.out.println("Task ID: " + task.getId()
                        + ", Título: " + task.getTitle()
                        + ", Categoria ID: " + (task.getCategory() != null ? task.getCategory().getId() : "null"));
            }

            request.setAttribute("tasks", concludetTasks);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/list-concluded.jsp");
            dispatcher.forward(request, response);
            return;
        }else if ("trabalho".equals(action) || "pessoal".equals(action) || "estudo".equals(action)) {


            String categoryStr = action;

            Category category = new Category();
            switch (categoryStr) {
                case "trabalho":
                    category.setId(1);
                    category.setName("Trabalho");
                    break;
                case "pessoal":
                    category.setId(2);
                    category.setName("Pessoal");
                    break;
                case "estudo":
                    category.setId(3);
                    category.setName("Estudo");
                    break;
            }

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            System.out.println("Codigo Usuario: " + user.getId());

            List<Task> filterTasks = db_task.listFilterTasks(user.getId(), category);

            request.setAttribute("tasks", filterTasks);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/list-pending.jsp");
            dispatcher.forward(request, response);
            return;
        }


        else if ("edit".equals(action)) {

            String idStr = request.getParameter("id");
            System.out.println("Codigo da Task no GET: " + idStr);

            Integer idTask = null;
            try {
                idTask = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            System.out.println("Codigo da Task no GET: " + idTask);

            if (idTask != null) {
                Task t = db_task.getTaskById(idTask);
                request.setAttribute("task", t);
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/update-task.jsp");
            dispatcher.forward(request, response);
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        request.setCharacterEncoding("UTF-8");

        if ("create".equals(action)) {
            String title = request.getParameter("titulo");
            String desc = request.getParameter("descricao");
            String categoryStr = request.getParameter("categoria");

            String dateStr = request.getParameter("data"); // Ex: "2025-06-03"
            LocalDate date = LocalDate.parse(dateStr);      // converte string para LocalDate


            HttpSession session = request.getSession();

            User user = (User) session.getAttribute("user");

            Category category = new Category();
            switch (categoryStr) {
                case "trabalho":
                    category.setId(1);
                    category.setName("Trabalho");
                    break;
                case "pessoal":
                    category.setId(2);
                    category.setName("Pessoal");
                    break;
                case "estudo":
                    category.setId(3);
                    category.setName("Estudo");
                    break;
            }

            Task t = new Task();

            t.setTitle(title);
            t.setDescription(desc);
            t.setStatus(true);
            t.setDate(date);
            t.setUser(user);
            t.setCategory(category);

            db_task.insertTask(t);

            response.sendRedirect(request.getContextPath() + "/tasks?action=listPending");

        } else if ("edit".equals(action)) {

            String newTitle = request.getParameter("titulo");
            String newDesc = request.getParameter("descricao");
            String idStr = request.getParameter("id");
            String dateStr = request.getParameter("data");  // nome da variável String vindo do form
            LocalDate date = LocalDate.parse(dateStr);      // converte para LocalDate


            System.out.println("Id da Task no POST: "+ idStr);

            Integer idTask = null;
            try {
                idTask = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            System.out.println("ID da task passado: " + idTask);

            if (idTask != null) {
                Task t = db_task.getTaskById(idTask);

                if (t != null) {

                    t.setTitle(newTitle);
                    t.setDescription(newDesc);
                    t.setStatus(true);
                    t.setDate(date);

                    db_task.updateTask(t);
                }
            }

            response.sendRedirect(request.getContextPath() + "/tasks?action=listPending");
        }else if ("delete".equals(action)) {
            String idStr = request.getParameter("id");
            System.out.println("Código da Task no POST para deletar: " + idStr);

            try {
                int id = Integer.parseInt(idStr);
                db_task.deletTask(id);
            } catch (NumberFormatException e) {
                System.out.println("ID inválido para deleção: " + idStr);
            }

            response.sendRedirect(request.getContextPath() + "/tasks?action=listPending");
        } else if("concluded".equals(action)){
            String idStr = request.getParameter("taskId");
            System.out.println("Código da Task no POST para concluir: " + idStr);

            try {
                int id = Integer.parseInt(idStr);
                db_task.conCludedTask(id);
            } catch (NumberFormatException e) {
                System.out.println("ID inválido para deleção: " + idStr);
            }
            response.sendRedirect(request.getContextPath() + "/tasks?action=listPending");
        }

    }


}
