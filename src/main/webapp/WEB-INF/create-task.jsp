<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleLogin.css" />
  <title>Criar Tarefa</title>
</head>
<body>
<div class="container">
  <h1 class="title_page">Criar Tarefa</h1>

  <form:form action="/tasks/create-task" method="post" modelAttribute="task">
    <form:hidden path="id" />

    <div class="form_input">
      <form:label path="title">Titulo:</form:label>
      <form:input path="title" id="titulo" cssClass="input" required="true" />
    </div>

    <div class="form_input">
      <form:label path="description">Descrição:</form:label>
      <form:input path="description" id="descricao" cssClass="input" />
    </div>

    <div class="form_input">
      <form:label path="date">Data:</form:label>
      <form:input path="date" id="data" type="date" cssClass="input" required="true" />
    </div>

    <div class="form_input">
      <form:label path="category.name">Categoria:</form:label>
      <form:select path="category.name" id="categoria" cssClass="input">
        <form:option value="" label=" " />
        <form:option value="trabalho" label="Trabalho" />
        <form:option value="pessoal" label="Pessoal" />
        <form:option value="estudo" label="Estudo" />
      </form:select>
    </div>

    <div class="form_input">
      <button type="submit">Criar</button>
    </div>

  </form:form>

</div>
</body>
</html>
