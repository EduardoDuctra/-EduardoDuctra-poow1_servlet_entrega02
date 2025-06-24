package br.ufsm.csi.spring.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public static Connection getConnection() throws SQLException {

        try {
            Class.forName("org.postgresql.Driver");
            System.out.print("Driver carregado com sucesso!\n");

            String url = "jdbc:postgresql://localhost:5432/tarefas";
            String user = "postgres";
            String password = "2016010124";

            return DriverManager.getConnection(url, user, password);


        } catch (ClassNotFoundException e) {

            throw new SQLException("Driver do PostgreSQL não encontrado", e);
        } catch (SQLException e) {

            throw new SQLException("Erro ao conectar ao banco de dados", e);
        }

    }
}
