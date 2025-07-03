<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>



<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleLogin.css" />

  <title>Atualizar Usuário</title>
</head>
<body>
<div class="container">
<h1>Atualizar Usuário</h1>

  <form:form action="/user/update-user" method="post" modelAttribute="user">

    <input type="hidden" name="id" value="${user.id}" />

    <div class="form_input">
      <form:label path="name" cssClass="label">Nome:</form:label>
      <form:input path="name" id="nome" cssClass="input" required="true" />
    </div>

    <div class="form_input">
      <form:label path="email" cssClass="label">Email:</form:label>
      <form:input path="email" id="email" type="email" cssClass="input" required="true" />
    </div>

    <div class="form_input">
      <form:label path="password" cssClass="label">Senha:</form:label>
      <form:password path="password" id="password" cssClass="input" required="true" />
    </div>


    <div class="button_group">
      <button type="submit" class="button">Atualizar</button>
    </div>

  </form:form>

  <form action="/user/delete-user" method="post">
    <button type="submit" class="button">Deletar</button>
  </form>

  <form action="/menu/dashboard" method="get">
    <button type="submit" class="button">Voltar ao Menu</button>
  </form>



</div>
</body>
</html>
