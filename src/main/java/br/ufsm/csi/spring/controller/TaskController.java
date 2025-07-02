package br.ufsm.csi.spring.controller;


import br.ufsm.csi.spring.model.Category;
import br.ufsm.csi.spring.model.Task;
import br.ufsm.csi.spring.model.User;
import br.ufsm.csi.spring.service.DataBaseServiceTask;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//se eu quiser editar algo (tarefa ou usuario) ou trabalhar com varias tarefas uso objeto. Se for deletar ou concluir, apenas o id



@Controller
@RequestMapping("/tasks")
public class TaskController {

    //Spring já cria um objeto do tipo service. Sem eu precisar fazer new DataBaseServiceTask()
    @Autowired
    private DataBaseServiceTask db_task;

    @GetMapping("/create-task")
    public String createTaskForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

//        if (user == null) {
//
//            return "redirect:/"; //pagina index.jsp
//        }

        System.out.println("ID do usuário no Get do Criar tarefa: " + user.getId());

        //passo para o formulario o objeto Task vazio
        model.addAttribute("task", new Task());

        return "create-task";
    }

    //recebe o objeto task com os campos preenchidos
    @PostMapping("/create-task")
    public String createTaskSubmit(@ModelAttribute("task") Task task, HttpSession session) {
        User user = (User) session.getAttribute("user");

//        if (user == null) {
//            return "redirect:/";
//        }


        // Define a categoria da tarefa a partir do task.getCategory() recebido do formulário
        Category categoria = new Category();
        switch (task.getCategory().getName()) {
            case "trabalho":
                categoria.setId(1);
                categoria.setName("Trabalho");
                break;
            case "pessoal":
                categoria.setId(2);
                categoria.setName("Pessoal");
                break;
            case "estudo":
                categoria.setId(3);
                categoria.setName("Estudo");
                break;
        }

        task.setUser(user);
        task.setCategory(categoria);
        task.setStatus(true);

        try {
            db_task.insertTask(task);
            System.out.println("Tarefa com ID inserida com sucesso!" + task.getId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        System.out.println("=== Tarefa Criada ===");
        System.out.println("ID: " + task.getId());
        System.out.println("Título: " + task.getTitle());
        System.out.println("Categoria: " + task.getCategory().getName());
        System.out.println("Data: " + task.getDate());
        System.out.println("Usuário ID: " + user.getId());
        System.out.println("=====================");

        return "redirect:/tasks/list-pending";
    }


    @GetMapping("/list-pending")
    public String listPendingTasks(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
//        if (user == null) {
//            return "redirect:/";
//        }

        List<Task> pendingTasks = null;
        try {
            pendingTasks = db_task.listTasks(user.getId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        //atribuo um objeto do tipo tasks para o JSP
        model.addAttribute("tasks", pendingTasks);

        return "list-pending"; // JSP para listar tarefas pendentes
    }

    //pego a categoria pela URL do que o usuário escolheu
    //@PathVariable captura um valor dinâmico que veio pela URL, nessa caso é a categoria
    @GetMapping("/filter-type/{category}")
    public String listFilteredTask(@PathVariable("category") String categoryStr, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
//        if (user == null) {
//            return "redirect:/";
//        }

        Category category = new Category();
        switch (categoryStr.toLowerCase()) {
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

        //passa o id e a tarefa que veio pela URL. Atribuo se for Trabalho, crio um objeto Categoria com o ID de Trabalho 1 e seto o nome dele pra trabalho
        List<Task> filteredTasks = null;
        try {
            filteredTasks = db_task.listFilterTasks(user.getId(), category);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        model.addAttribute("tasks", filteredTasks);

        return "list-pending";
    }

    @GetMapping("/list-concluded")
    public String listConcludedTasks(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
//        if (user == null) {
//            return "redirect:/";
//        }

        List<Task> concludedTasks = null;
        try {
            concludedTasks = db_task.listConcludedTasks(user.getId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        model.addAttribute("tasks", concludedTasks);

        return "list-concluded";
    }

    //pego o id da task pela URL
    //@PathVariable captura um valor dinâmico que veio pela URL, nessa caso é o id da task
    @PostMapping("/concluded/{taskId}")
    public String concludeTask(@PathVariable("taskId") int taskId, HttpSession session) {
        User user = (User) session.getAttribute("user");
//        if (user == null) {
//            return "redirect:/";
//        }

        try {
            db_task.conCludedTask(taskId);
        } catch (Exception e) {
            System.out.println("Erro ao concluir task id: " + taskId);
            e.printStackTrace();
        }

        // Redireciona para a lista de tarefas pendentes
        return "redirect:/tasks/list-pending";
    }

    //pego o id da task pela URL
    //@PathVariable captura um valor dinâmico que veio pela URL, nessa caso é o id da task
    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable("id") int id, HttpSession session) {
        User user = (User) session.getAttribute("user");
//        if (user == null) {
//            return "redirect:/";
//        }

        try {
            System.out.println("ID da task para deletar: " + id);
            db_task.deletTask(id);
        } catch (Exception e) {
            System.out.println("ID inválido para deleção: " + id);
            e.printStackTrace();
        }

        return "redirect:/tasks/list-pending";
    }


    //pego o id da task pela URL
    //@PathVariable captura um valor dinâmico que veio pela URL, nessa caso é o id da task
    @GetMapping("/edit/{id}")
    public String editTask(@PathVariable("id") int id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
//        if (user == null) {
//            System.out.println("Usuário não logado, redirecionando para /");
//            return "redirect:/";
//        }

        Task task = null;
        try {
            System.out.println("GET editar tarefa: " + id);
            task = db_task.getTaskById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        if (task == null) {
            System.out.println("Tarefa não encontrada com id: " + id);
            return "redirect:/tasks/list-pending";
        }

        model.addAttribute("task", task);
        return "update-task";
    }


    @PostMapping("/edit")

    // Recebo o objeto task com os novos dados editados que vieram do formulário JSP e atualizo no banco
    public String editTask(@ModelAttribute("task") Task task, HttpSession session) {
        User user = (User) session.getAttribute("user");
//        if (user == null) {
//            System.out.println("Usuário não logado, redirecionando para /");
//            return "redirect:/";
//        }

        Category categoria = new Category();
        if (task.getCategory() != null && task.getCategory().getName() != null) {
            switch (task.getCategory().getName().toLowerCase()) {
                case "trabalho":
                    categoria.setId(1);
                    categoria.setName("Trabalho");
                    break;
                case "pessoal":
                    categoria.setId(2);
                    categoria.setName("Pessoal");
                    break;
                case "estudo":
                    categoria.setId(3);
                    categoria.setName("Estudo");
                    break;
            }
        }

        task.setCategory(categoria);
        task.setUser(user);
        task.setStatus(true);

        System.out.println("Recebendo edição da tarefa id: " + task.getId());
        System.out.println("Título: " + task.getTitle());
        System.out.println("Descrição: " + task.getDescription());
        System.out.println("Categoria nome: " + (task.getCategory() != null ? task.getCategory().getName() : "null"));
        System.out.println("Data: " + task.getDate());

        try {
            db_task.updateTask(task);
            System.out.println("Tarefa atualizada com sucesso, id: " + task.getId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        return "redirect:/tasks/list-pending";
    }

}
