package br.ufsm.csi.spring.dao;

import br.ufsm.csi.spring.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public User insertUser(User user) {
        String sql = "INSERT INTO usuario (nome, email, senha) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());


            pstmt.executeUpdate();


            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGerado = generatedKeys.getInt(1);
                    user.setId(idGerado);
                }
            }

            pstmt.close();

            return user;

        } catch (SQLException e) {
            System.out.println("Erro ao inserir usuário: " + e.getMessage());
        }
        return null;
    }

    public String deleteUser(int id) {
        String sql = "DELETE FROM usuario WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("Nenhum usuário excluído, pois o ID não existe na tabela.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao excluir usuário: " + e.getMessage());
        }

        return "Usuário excluído com sucesso!";
    }


    public String updateUser(User user) {
        String sql = "UPDATE usuario SET nome = ?, email = ?, senha = ?  WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setInt(4, user.getId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("Nenhum usuário atualizado, pois o ID não existe na tabela.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar usuário: " + e.getMessage());
        }

        return "Usuário atualizado com sucesso!";
    }



    public User searchEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";
        User user = null;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("nome"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("senha"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuário por email: " + e.getMessage());
        }

        return user;
    }

    public User getUserById(int id) {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        User user = null;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("nome"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("senha"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao recuperar usuário: " + e.getMessage());
        }

        return user;
    }



}
