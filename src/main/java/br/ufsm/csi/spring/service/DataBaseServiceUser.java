package br.ufsm.csi.spring.service;



import br.ufsm.csi.spring.dao.UserDAO;
import br.ufsm.csi.spring.model.User;
import br.ufsm.csi.spring.util.ConnectionFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

@Service
public class DataBaseServiceUser {
    private Connection connection;

    public DataBaseServiceUser() {

        try {
            this.connection = new ConnectionFactory().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao conectar com o Banco de Dados");
        }
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexão com o banco fechada.");
            } catch (SQLException e) {
                System.out.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }


    public String insertUser(User user) {
        try {

            User insertedUser = new UserDAO(connection).insertUser(user);


            if (insertedUser != null && insertedUser.getId() != null) {
                return "Usuário inserido com sucesso! ID: " + insertedUser.getId();
            } else {
                return "Erro ao inserir usuário: ID não gerado.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao inserir usuário: " + e.getMessage();
        }
    }

    public String updateUser(User user) {
        try {
            return new UserDAO(connection).updateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao atualizar usuário: " + e.getMessage();
        }
    }
    public String deleteUser(Integer id) {
        try {
            return new UserDAO(connection).deleteUser(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao deletar usuário: " + e.getMessage();
        }
    }

    public User searchEmail(String email) {
        try {
            return new UserDAO(connection).searchEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User getById(Integer id) {
        try {
            return new UserDAO(connection).getUserById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
