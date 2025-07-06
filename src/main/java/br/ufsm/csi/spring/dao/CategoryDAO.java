package br.ufsm.csi.spring.dao;

import br.ufsm.csi.spring.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryDAO {
    private Connection connection;

    public CategoryDAO(Connection connection) {
        this.connection = connection;
    }


    public Category getCategoryById(int id) {
        String sql = "SELECT * FROM categoria WHERE id = ?";
        Category category = null;
        try {
            PreparedStatement psmt = connection.prepareStatement(sql);
            psmt.setInt(1, id);
            ResultSet rs = psmt.executeQuery();

            if (rs.next()) {
                category = new Category();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("nome"));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar categoria: " + e.getMessage());
        }
        return category;
    }


}
