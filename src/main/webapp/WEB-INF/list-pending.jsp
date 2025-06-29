<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8" />
  <title>Tarefas Pendentes</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />

</head>
<body>
<div class="container">
  <h1>Tarefas Pendentes</h1>

  <c:choose>
    <c:when test="${empty tasks}">
      <p>Não há tarefas pendentes.</p>
    </c:when>
    <c:otherwise>

      <div class="button_category">
        <form action="/tasks/list-pending" method="get">
          <button type="submit">Listar Todas</button>
        </form>

        <form action="/tasks/filter-type/trabalho" method="get">
          <button type="submit">Trabalho</button>
        </form>

        <form action="/tasks/filter-type/estudo" method="get">
          <button type="submit">Estudo</button>
        </form>

        <form action="/tasks/filter-type/pessoal" method="get">
          <button type="submit">Pessoal</button>
        </form>
      </div>

      <table>
        <thead>
        <tr>
          <th>ID</th>
          <th>Título</th>
          <th>Descrição</th>
          <th>Categoria</th>
          <th>Prazo</th>

          <th colspan="3">Ações</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="task" items="${tasks}">
          <tr>
            <td>${task.id}</td>
            <td>${task.title}</td>
            <td>${task.description}</td>
            <td>${task.category.name}</td>
            <td>${task.formattedDate}</td>


            <td>
              <form action="/tasks/concluded/${task.id}" method="post">
                <input type="hidden" name="action" value="concluded" />
                <input type="hidden" name="taskId" value="${task.id}" />
                <button type="submit" class="button_list">
                  <img
                          class="icon_button"
                          src="${pageContext.request.contextPath}/assets/check_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg"
                          alt="Ícone Concluída"
                  />
                </button>
              </form>
            </td>

            <td>
              <form action="/tasks/edit/${task.id}" method="get">
                <input type="hidden" name="action" value="edit" />
                <input type="hidden" name="id" value="${task.id}" />
                <button type="submit" class="button_list">
                  <img
                          class="icon_button"
                          src="${pageContext.request.contextPath}/assets/edit_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg"
                          alt="Ícone Editar"
                  />
                </button>
              </form>
            </td>

            <td>
              <form action="/tasks/delete/${task.id}" method="post">
                <input type="hidden" name="action" value="delete" />
                <input type="hidden" name="id" value="${task.id}" />
                <button type="submit" class="button_list">
                  <img
                          class="icon_button"
                          src="${pageContext.request.contextPath}/assets/delete_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg"
                          alt="Ícone Deletar"
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

  <br />

  <form action="/menu/dashboard" method="get">
    <input type="hidden" name="action" value="back" />
    <button type="submit">Voltar</button>
  </form>
</div>
</body>
</html>
