<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>



<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
  <title>Menu de Tarefas</title>
</head>
<body>
<div class="container">

  <div class="navegation_container">
    <div class="navegation_user">
      <h3>Bem-vindo, ${sessionScope.user.name}</h3>
      <p>ID de Usuário: ${sessionScope.user.id}</p>

    </div>

    <div class="navegation_user_update">
      <form action="/user/update-user" method="get">
        <input type="hidden" name="action" value="update" />
        <button type="submit">Meus Dados</button>
      </form>
    </div>
  </div>

  <div class="menu">
    <ul class="menu_buttons">
      <li>
        <form action="/tasks/create-task" method="get">
          <input type="hidden" name="action" value="create" />
          <button type="submit" class="button_menu">
            <img class="icon_button" src="${pageContext.request.contextPath}/assets/add_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg" alt="Ícone Criar Tarefa" /><br/>
            Criar Tarefa
          </button>
        </form>
      </li>

      <li>
        <form action="/tasks/list-pending" method="get">
          <input type="hidden" name="action" value="listPending">
          <button type="submit" class="button_menu">
            <img class="icon_button" src="${pageContext.request.contextPath}/assets/menu_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg" alt="Ícone Tarefas Pendentes" />
            Tarefas Pendentes
          </button>
        </form>
      </li>

      <li>
        <form action="/tasks/list-concluded" method="get">
          <input type="hidden" name="action" value="listConcluded">
          <button type="submit" class="button_menu">
            <img class="icon_button" src="${pageContext.request.contextPath}/assets/fact_check_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg" alt="Ícone Tarefas Concluidas" />
            Tarefas Concluídas
          </button>
        </form>
      </li>
    </ul>
  </div>


  <div class="menu_list">

    <h2>Tarefas para hoje - ${sessionScope.loginTime}</h2>

    <c:choose>
      <c:when test="${empty tasks}">
        <p>Não há tarefas pendentes.</p>
      </c:when>
      <c:otherwise>
        <table>
          <thead>
          <tr>
            <th>Título</th>
            <th>Descrição</th>
            <th>Prazo</th>
            <th>Concluir</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="task" items="${tasks}">
            <tr>
              <td>${task.title}</td>
              <td>${task.description}</td>
              <td>${task.date}</td>
              <td>
                <form action="/menu/concluded/${task.id}" method="post">
                  <input type="hidden" name="id" value="${task.id}" />
                  <button type="submit" class="button_list">
                    <img
                            class="icon_button"
                            src="${pageContext.request.contextPath}/assets/check_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg"
                            alt="Ícone Concluída"
                    />
                  </button>
                </form>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </c:otherwise>
    </c:choose>

  </div>



</div>

</body>
</html>
