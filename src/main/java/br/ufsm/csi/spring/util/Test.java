package br.ufsm.csi.spring.util;


import br.ufsm.csi.spring.model.Category;
import br.ufsm.csi.spring.model.Task;
import br.ufsm.csi.spring.model.User;
import br.ufsm.csi.spring.service.DataBaseServiceCategory;
import br.ufsm.csi.spring.service.DataBaseServiceTask;
import br.ufsm.csi.spring.service.DataBaseServiceUser;

import java.util.List;


public class Test {
    public static void main(String[] args) {

        DataBaseServiceUser db_user = new DataBaseServiceUser();
        DataBaseServiceTask db_task = new DataBaseServiceTask();
        DataBaseServiceCategory db_category = new DataBaseServiceCategory();

        User u = new User();
        u.setName("adm");

        String email = "adm@adm.com";
        u.setEmail(email);

        u.setPassword("adm");


        try {

            User validation = db_user.searchEmail(email);

            if (validation == null) {

                db_user.insertUser(u);
                u = db_user.searchEmail(email);

                System.out.println("Usuário inserido com código: " + u.getId());

            } else {
                System.out.println("Usuário existente. Não foi inserido");
                u = validation;
            }
        } catch (Exception e) {
            System.out.println("Erro ao inserir ou buscar usuário: " + e.getMessage());
        }


        Category c = db_category.getCategoryById(1);

        Task t = new Task();
        t.setTitle("Tarefa de Teste 2");
        t.setDescription("Descrição aaaaaa");
        t.setStatus(false);
        t.setUser(u);
        t.setCategory(c);

        db_task.insertTask(t);

        //----------------//

        updateUser(db_user);
        deleteUser(db_user);
        searchEmail(db_user);
        updateTask(db_task);
        deleteTask(db_task);
        listTasks(db_task);



    }

    private static void updateUser(DataBaseServiceUser db_user) {


            int id = 137;

            User validation = db_user.getById(id);

            if(validation!=null) {
                String name = "nvo name";
                String email = "atualizado@gmail.com";
                String password = "atualizada";


                User user = new User();
                user.setId(id);
                user.setName(name);
                user.setEmail(email);
                user.setPassword(password);


                String result = db_user.updateUser(user);


                System.out.println(result);
            }



    }

    private static void deleteUser(DataBaseServiceUser db_user) {

        int id = 237;

        String result = db_user.deleteUser(id);
        System.out.println(result);

    }


    private static void listTasks(DataBaseServiceTask db_task) {

        int userId = 1;
        List<Task> tasks = db_task.listTasks(userId);

        if (tasks != null && !tasks.isEmpty()) {
            for (Task task : tasks) {
                printTask(task);
            }
        } else {
            System.out.println("Nenhuma tarefa encontrada para o usuário com ID: " + userId);
        }
    }

    private static void searchEmail(DataBaseServiceUser db_user) {
        String email = "teste4@gmail.com";

        User result = db_user.searchEmail(email);

        if (result != null) {
            System.out.println("Usuário encontrado:");
            System.out.println("ID: " + result.getId());
            System.out.println("Nome: " + result.getName());
            System.out.println("Email: " + result.getEmail());
        } else {
            System.out.println("Usuário com e-mail '" + email + "' não encontrado.");
        }

    }

    private static void updateTask(DataBaseServiceTask db_task) {


            int id = 37;
            String title = "novo titulo";
            String description = "nova descricao";
            Boolean isActive = true;

            Task task = new Task();
            task.setId(id);
            task.setTitle(title);
            task.setDescription(description);
            task.setStatus(isActive);


            String resultado = db_task.updateTask(task);


            System.out.println(resultado);

    }

    private static void deleteTask(DataBaseServiceTask db_task) {

        int id = 35;

        String result = db_task.deletTask(id);
        System.out.println(result);

    }

    public static void printTask(Task task) {
        System.out.println("\n");
        System.out.println("--------------------------");
        System.out.println("ID: " + task.getId());
        System.out.println("Título: " + task.getTitle());
        System.out.println("Descrição: " + task.getDescription());
        System.out.println("Concluída: " + task.isStatus());
        System.out.println("\n\n");
    }

}
