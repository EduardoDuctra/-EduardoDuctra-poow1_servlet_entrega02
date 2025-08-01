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
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
  <title>Cadastrar Usuário</title>
</head>
<body>
<div class="container">
  <h1>Cadastro de Usuário</h1>

  <form:form action="/user/register-user" method="post" modelAttribute="user">

    <div class="form_input">
      <form:label path="name">Nome:</form:label>
      <form:input path="name" />
    </div>

    <div class="form_input">
      <form:label path="email">Email:</form:label>
      <form:input path="email" />
    </div>

    <div class="form_input">
      <form:label path="password">Senha:</form:label>
      <form:password path="password" />
    </div>

    <div class="form_input">
      <button type="submit">Cadastrar</button>
    </div>

  </form:form>

</div>
</body>
</html>
