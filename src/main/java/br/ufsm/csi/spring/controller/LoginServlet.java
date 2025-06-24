package br.ufsm.csi.spring.controller;


import br.ufsm.csi.spring.model.User;
import br.ufsm.csi.spring.service.LoginService;
import br.ufsm.csi.spring.util.ConnectionFactory;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private Connection connection;

    @Override
    public void init() throws ServletException {
        try {
            connection = ConnectionFactory.getConnection();
        } catch (SQLException e) {
            throw new ServletException("Erro ao conectar ao banco de dados", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("register".equals(action)) {
            resp.sendRedirect(req.getContextPath() + "/user?action=register");
            return;
        }

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            LoginService loginService = new LoginService();

            if (loginService.autentication(email, password)) {

                User user = loginService.db_user.searchEmail(email);

                //Armazena o usuário na sessão
                //Criei um atributo do tipo user e atribui a sessão
                //Sempre que quiser acessar um user, é desse aqui que estou falando
                HttpSession session = req.getSession();

                session.setAttribute("user", user);

                resp.sendRedirect(req.getContextPath() + "/menu");
            } else {
                req.setAttribute("msg", "Usuário ou senha incorreto!");
                RequestDispatcher dispatcher = req.getRequestDispatcher("/index.jsp");
                dispatcher.forward(req, resp);
            }
        } catch (Exception e) {
            req.setAttribute("msg", e.getMessage());
            RequestDispatcher dispatcher = req.getRequestDispatcher("/index.jsp");
            dispatcher.forward(req, resp);
        }
    }

    @Override
    public void destroy() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
