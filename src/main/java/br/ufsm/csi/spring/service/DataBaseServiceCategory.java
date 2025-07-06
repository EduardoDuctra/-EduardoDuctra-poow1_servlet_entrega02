package br.ufsm.csi.spring.service;


import br.ufsm.csi.spring.dao.CategoryDAO;
import br.ufsm.csi.spring.model.Category;
import br.ufsm.csi.spring.util.ConnectionFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

@Service
public class DataBaseServiceCategory {

    private Connection connection;

    public DataBaseServiceCategory() {
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

    public Category getCategoryById(int id) {
        CategoryDAO categoryDAO = new CategoryDAO(connection);
        return categoryDAO.getCategoryById(id);
    }

    public Category returnCategory(String name) {

        Category category = new Category();
        switch (name.toLowerCase()) {
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
        return category;
    }
}
