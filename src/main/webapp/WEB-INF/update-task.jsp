<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
  <title>Atualizar Tarefa</title>
</head>
<body>
<div class="container">
  <h1 class="title_page">Atualizar Tarefa</h1>

  <form:form modelAttribute="task" action="/tasks/edit" method="post">

    <form:hidden path="id"/>
    <form:hidden path="status"/>

    <div class="form_input">
      <form:label path="title">Título:</form:label>
      <form:input path="title" required="true"/>
    </div>

    <div class="form_input">
      <form:label path="description">Descrição:</form:label>
      <form:input path="description"/>
    </div>

    <div class="form_input">
      <form:label path="date">Data:</form:label>
      <form:input path="date" type="date" required="true"/>
    </div>

    <div class="form_input">
      <form:label path="category.name">Categoria:</form:label>
      <form:select path="category.name">
        <form:option value="trabalho" label="Trabalho"/>
        <form:option value="pessoal" label="Pessoal"/>
        <form:option value="estudo" label="Estudo"/>
      </form:select>
    </div>

    <div class="button_group">
      <button type="submit">Atualizar</button>
    </div>

  </form:form>
</div>
</body>
</html>
