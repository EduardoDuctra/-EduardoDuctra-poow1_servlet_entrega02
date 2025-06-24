package br.ufsm.csi.spring.controller;


import br.ufsm.csi.spring.model.Category;
import br.ufsm.csi.spring.model.Task;
import br.ufsm.csi.spring.model.User;
import br.ufsm.csi.spring.service.DataBaseServiceCategory;
import br.ufsm.csi.spring.service.DataBaseServiceTask;
import br.ufsm.csi.spring.service.DataBaseServiceUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private DataBaseServiceTask db_task;

    @GetMapping("/create-task")
    public String createTaskForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            // Usuário não está logado, redireciona para a página de login ou home
            return "redirect:/";
        }

        System.out.println("ID do usuário no Get do Criar tarefa: " + user.getId());

        model.addAttribute("task", new Task()); // para popular o formulário se quiser

        return "create-task"; // JSP ou template para criar tarefa
    }

    @PostMapping("/create-task")
    public String createTaskSubmit(@ModelAttribute("task") Task task, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/"; // Redireciona se não estiver logado
        }

        // Mapeia a categoria simples com base no nome
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
        task.setStatus(true); // status padrão

        db_task.insertTask(task);

        System.out.println("=== Tarefa Criada ===");
        System.out.println("ID: " + task.getId());
        System.out.println("Título: " + task.getTitle());
        System.out.println("Categoria: " + task.getCategory().getName());
        System.out.println("Data: " + task.getDate());
        System.out.println("Usuário ID: " + user.getId());
        System.out.println("=====================");

        return "redirect:/tasks/list-pending"; // redireciona para listagem
    }


    @GetMapping("/list-pending")
    public String listPendingTasks(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        List<Task> pendingTasks = db_task.listTasks(user.getId());
        model.addAttribute("tasks", pendingTasks);

        return "list-pending"; // JSP para listar tarefas pendentes
    }

    @GetMapping("/filter-type/{category}")
    public String listFilteredTask(@PathVariable("category") String categoryStr, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/"; // ou para a página de login
        }

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

        List<Task> filteredTasks = db_task.listFilterTasks(user.getId(), category);
        model.addAttribute("tasks", filteredTasks);

        return "list-pending";
    }

    @GetMapping("/list-concluded")
    public String listConcludedTasks(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        List<Task> concludedTasks = db_task.listConcludedTasks(user.getId());
        model.addAttribute("tasks", concludedTasks);

        return "list-concluded";
    }

    @PostMapping("/concluded")
    public String concludeTask(@RequestParam("taskId") int taskId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        try {
            db_task.conCludedTask(taskId);
        } catch (Exception e) {
            System.out.println("Erro ao concluir task id: " + taskId);
            e.printStackTrace();
            // Pode adicionar tratamento de erro ou mensagem
        }

        // Redireciona para a lista de tarefas pendentes
        return "redirect:/tasks/list-pending";
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable("id") int id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        try {
            System.out.println("ID da task para deletar: " + id);
            db_task.deletTask(id);
        } catch (Exception e) {
            System.out.println("ID inválido para deleção: " + id);
            e.printStackTrace();
        }

        return "redirect:/tasks/list-pending";
    }


    @GetMapping("/edit/{id}")
    public String editTaskForm(@PathVariable("id") int id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            System.out.println("Usuário não logado, redirecionando para /");
            return "redirect:/";
        }

        System.out.println("GET editar tarefa: " + id);
        Task task = db_task.getTaskById(id);

        if (task == null) {
            System.out.println("Tarefa não encontrada com id: " + id);
            return "redirect:/tasks/list-pending";
        }

        model.addAttribute("task", task);
        return "update-task"; // JSP para editar tarefa
    }

    @PostMapping("/edit")
    public String editTaskSubmit(@ModelAttribute("task") Task task, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            System.out.println("Usuário não logado, redirecionando para /");
            return "redirect:/";
        }

        System.out.println("Recebendo edição da tarefa id: " + task.getId());
        System.out.println("Título: " + task.getTitle());
        System.out.println("Descrição: " + task.getDescription());
        System.out.println("Categoria nome: " + (task.getCategory() != null ? task.getCategory().getName() : "null"));
        System.out.println("Data: " + task.getDate());

        // Ajuste da categoria conforme seu padrão
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
                default:
                    System.out.println("Categoria inválida ou não informada, mantendo a atual.");
                    categoria = task.getCategory(); // mantem categoria atual se for inválida
            }
        }

        task.setCategory(categoria);
        task.setUser(user);
        task.setStatus(true); // mantendo padrão

        db_task.updateTask(task);

        System.out.println("Tarefa atualizada com sucesso, id: " + task.getId());

        return "redirect:/tasks/list-pending";
    }

}
