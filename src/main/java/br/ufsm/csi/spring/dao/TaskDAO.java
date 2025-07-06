package br.ufsm.csi.spring.dao;


import br.ufsm.csi.spring.model.Category;
import br.ufsm.csi.spring.model.Task;
import br.ufsm.csi.spring.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskDAO {

    private Connection connection;
    private CategoryDAO categoryDAO;

    public TaskDAO(Connection connection) {
        this.connection = connection;
        this.categoryDAO = new CategoryDAO(connection);
    }

    public String insertTask(Task task) {
        String sql = "INSERT INTO tarefa (titulo, descricao, concluido, data, codUsuario, codcategoria) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            // 1. Solicita o retorno da chave primária gerada
            PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setBoolean(3, task.isStatus());
            pstmt.setDate(4, java.sql.Date.valueOf(task.getDate()));
            pstmt.setInt(5, task.getUser().getId());
            pstmt.setInt(6, task.getCategory().getId());

            // 2. Executa a inserção
            pstmt.executeUpdate();

            // 3. Recupera o ID gerado automaticamente
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                task.setId(generatedId); // define o ID no objeto Java
            }

            rs.close();
            pstmt.close();

        } catch (SQLException e) {
            System.out.println("Erro ao inserir tarefa: " + e.getMessage());
        }

        return "Tarefa cadastrada com sucesso!";
    }

    public String deleteTasksByUserId(int userId) {
        String sql = "DELETE FROM tarefa WHERE codusuario = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                return "Nenhuma tarefa encontrada para o usuário.";
            } else {
                return "Todas as tarefas do usuário foram excluídas com sucesso!";
            }

        } catch (SQLException e) {
            System.out.println("Erro ao excluir tarefas do usuário: " + e.getMessage());
            return "Erro ao excluir tarefas do usuário.";
        }
    }

    public Task getTaskById(int id) {
        String sql = "SELECT * FROM tarefa WHERE id = ?";

        Task task = null;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                task = new Task();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("titulo"));
                task.setDescription(rs.getString("descricao"));

                java.sql.Date sqlDate = rs.getDate("data");

                if (sqlDate != null) {
                    task.setDate(sqlDate.toLocalDate());  // converte java.sql.Date para LocalDate
                } else {
                    task.setDate(null);
                }
                task.setStatus(rs.getBoolean("concluido"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao recuperar tarefa: " + e.getMessage());
        }

        return task;
    }


    public String updateTask(Task task) {
        String sql = "UPDATE tarefa SET titulo = ?, descricao = ?, concluido = ?, data = ?, codcategoria = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setBoolean(3, task.isStatus());
            pstmt.setDate(4, java.sql.Date.valueOf(task.getDate()));
            pstmt.setInt(5, task.getCategory().getId());
            pstmt.setInt(6, task.getId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("Nenhuma tarefa atualizada, pois o ID não existe na tabela.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar tarefa: " + e.getMessage());
        }

        return "Tarefa atualizada com sucesso!";
    }

    public String deletTask(int id) {
        String sql = "DELETE FROM tarefa WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("Nenhuma tarefa excluída, pois o ID não existe na tabela.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao excluir tarefa: " + e.getMessage());
        }

        return "Tarefa excluída com sucesso!";
    }

    public List<Task> getTasks(int user_id) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tarefa WHERE codusuario = ? AND concluido = true";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, user_id);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Task task = new Task();
                    task.setId(rs.getInt("id"));
                    task.setTitle(rs.getString("titulo"));
                    task.setDescription(rs.getString("descricao"));


                    java.sql.Date sqlDate = rs.getDate("data");

                    if (sqlDate != null) {
                        task.setDate(sqlDate.toLocalDate());  // converte java.sql.Date para LocalDate
                    } else {
                        task.setDate(null);
                    }

                    task.setStatus(rs.getBoolean("concluido"));

                    User user = new User();
                    user.setId(rs.getInt("codusuario"));
                    task.setUser(user);

                    int categoriaId = rs.getInt("codcategoria");
                    Category category = categoryDAO.getCategoryById(categoriaId);
                    task.setCategory(category);

                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar tarefas por usuário: " + e.getMessage());
        }
        return tasks;
    }

    public List<Task> getTasksByDay(int user_id, Date date) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tarefa WHERE codusuario = ? AND concluido = true and data = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, user_id);
            pstmt.setDate(2, new java.sql.Date(date.getTime()));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Task task = new Task();
                    task.setId(rs.getInt("id"));
                    task.setTitle(rs.getString("titulo"));
                    task.setDescription(rs.getString("descricao"));


                    java.sql.Date sqlDate = rs.getDate("data");

                    if (sqlDate != null) {
                        task.setDate(sqlDate.toLocalDate());  // converte java.sql.Date para LocalDate
                    } else {
                        task.setDate(null);
                    }

                    task.setStatus(rs.getBoolean("concluido"));

                    User user = new User();
                    user.setId(rs.getInt("codusuario"));
                    task.setUser(user);

                    int categoriaId = rs.getInt("codcategoria");
                    Category category = categoryDAO.getCategoryById(categoriaId);
                    task.setCategory(category);

                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar tarefas por usuário: " + e.getMessage());
        }
        return tasks;
    }

    public List<Task> getTasksByCategory(int user_id, Category category) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tarefa WHERE codusuario = ? AND concluido = true and codcategoria = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, user_id);
            pstmt.setInt(2, category.getId());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Task task = new Task();
                    task.setId(rs.getInt("id"));
                    task.setTitle(rs.getString("titulo"));
                    task.setDescription(rs.getString("descricao"));

                    java.sql.Date sqlDate = rs.getDate("data");

                    if (sqlDate != null) {
                        task.setDate(sqlDate.toLocalDate());  // converte java.sql.Date para LocalDate
                    } else {
                        task.setDate(null);
                    }

                    task.setStatus(rs.getBoolean("concluido"));

                    User user = new User();
                    user.setId(rs.getInt("codusuario"));
                    task.setUser(user);

                    task.setCategory(category);

                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar tarefas por usuário: " + e.getMessage());
        }
        return tasks;
    }

    public List<Task> getConcludedTasks(int user_id) {

        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tarefa WHERE codusuario = ? and concluido = false";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, user_id);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Task task = new Task();
                    task.setId(rs.getInt("id"));
                    task.setTitle(rs.getString("titulo"));
                    task.setDescription(rs.getString("descricao"));

                    java.sql.Date sqlDate = rs.getDate("data");

                    if (sqlDate != null) {
                        task.setDate(sqlDate.toLocalDate());  // converte java.sql.Date para LocalDate
                    } else {
                        task.setDate(null);
                    }

                    task.setStatus(rs.getBoolean("concluido"));


                    User user = new User();
                    user.setId(rs.getInt("codusuario"));
                    task.setUser(user);

                    int categoriaId = rs.getInt("codcategoria");
                    Category category = categoryDAO.getCategoryById(categoriaId);
                    task.setCategory(category);

                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar tarefas por usuário: " + e.getMessage());
        }
        return tasks;
    }

    public String concludedTask(int id) {
        String sql = "UPDATE tarefa SET concluido = false WHERE id = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);

            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                return "Tarefa marcada como não concluída.";
            } else {
                return "Nenhuma tarefa foi atualizada.";
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar tarefa: " + e.getMessage(), e);
        }
    }
}
