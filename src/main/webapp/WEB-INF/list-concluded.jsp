<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
  <title>Tarefas Concluídas</title>
</head>
<body>
<div class="container">
<h1>Tarefas Pendentes</h1>

<c:choose>
  <c:when test="${empty tasks}">
    <p>Não há tarefas pendentes.</p>
  </c:when>
  <c:otherwise>
    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>Título</th>
        <th>Descrição</th>
        <th>Categoria</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="task" items="${tasks}">
        <tr>
          <td>${task.id}</td>
          <td>${task.title}</td>
          <td>${task.description}</td>
          <td>${task.category.name}</td>


        </tr>
      </c:forEach>
      </tbody>
    </table>
  </c:otherwise>
</c:choose>

<br>

<form action="/menu/dashboard" method="get">
  <button type="submit">Voltar</button>
</form>
</div>
</body>
</html>
