package br.ufsm.csi.spring.controller;


import br.ufsm.csi.spring.model.User;
import br.ufsm.csi.spring.service.DataBaseServiceTask;
import br.ufsm.csi.spring.service.DataBaseServiceUser;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private DataBaseServiceUser db_user;
    private DataBaseServiceTask db_task;

    @Override
    public void init() throws ServletException {
        super.init();
        db_user = new DataBaseServiceUser();
        db_task = new DataBaseServiceTask();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        System.out.println("Action recebida no doGet: " + action);

        if ("register".equals(action)) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/register-user.jsp");
            dispatcher.forward(request, response);
            return;

        } else if ("update".equals(action)) {

            // Obtem a sessção atual, sem create uma nova. Usa o usuário logado
            HttpSession session = request.getSession(false);

            //se a sessão é nula (ngn logado) vai para o index
            if (session == null) {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                return;
            }

            User loggedUser = (User) session.getAttribute("user");

            if (loggedUser == null) {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                return;
            }

            //Apenas atualizei o user da sessão. O que foi passado no LoginServlet
            request.setAttribute("user", loggedUser);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/update-user.jsp");
            dispatcher.forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/menu");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        System.out.println("Action recebida no doPost: " + action);

        try {
            if ("register".equals(action)) {

                String name = request.getParameter("nome");
                String email = request.getParameter("email");
                String password = request.getParameter("password");

                User user = new User();
                user.setName(name);
                user.setEmail(email);
                user.setPassword(password);

                String resultado = db_user.insertUser(user);
                request.getSession().setAttribute("message", resultado);

                response.sendRedirect(request.getContextPath() + "/index.jsp");
                return;
            }

            //Recupero a sessão com o usuário já logado
            HttpSession session = request.getSession(false);

            if (session == null) {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                return;
            }

            User sessionUser = (User) session.getAttribute("user");

            if (sessionUser == null) {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                return;
            }

            // Pego os parâmetros aqui para usar no update
            String name = request.getParameter("nome");
            String email = request.getParameter("email");
            String password = request.getParameter("password");


            if ("delete".equals(action)) {

                int id = sessionUser.getId();

                db_task.deletTaskByUserId(id);
                db_user.deleteUser(id);

                //encerra a sessão. O usuário foi excluido
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/index.jsp");

            } else if ("update".equals(action)) {

                sessionUser.setName(name);
                sessionUser.setEmail(email);
                sessionUser.setPassword(password);

                db_user.updateUser(sessionUser);

                //eu só atribuo a sessão o novo usuario
                session.setAttribute("user", sessionUser);

                response.sendRedirect(request.getContextPath() + "/menu");

            } else if ("back".equals(action)) {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/menu.jsp");
                dispatcher.forward(request, response);
            }


        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "Erro: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/update-user.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    public void destroy() {
        db_user.close();
        super.destroy();
    }
}
