<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleLogin.css" />
</head>

<body>
<div class="page">

    <div class="container">
        <div class="title_page">
            <h2>Login</h2>
        </div>

        <div class="login_form">
            <!-- Formulário de Login -->
            <form action="/login" method="post">
                <div class="form_input">
                    <label for="email"><b>Email</b></label>
                    <input id="email" type="email" placeholder="email" name="email" />
                </div>

                <div class="form_input">
                    <label for="password"><b>Senha</b></label>
                    <input
                            id="password"
                            type="password"
                            placeholder="password"
                            name="password"
                    />
                </div>

                <!-- Grupo de Botões: Logar e Cadastrar Usuário -->
                <div class="button_group">
                    <button type="submit" name="action" value="logar">Logar</button>
                    <a href="/user/register-user" class="button">Cadastrar Usuário</a>




                </div>
            </form>
        </div>

        <!-- Mensagem de erro se necessário -->
        <c:if test="${not empty msg}">
            <h2>${msg}</h2>
        </c:if>
    </div>

    <div class="page_image">
        <img
                class="background-image"
                src="${pageContext.request.contextPath}/assets/Mobile login-amico.svg"
                alt="Imagem de fundo"
        />
    </div>

</div>

</body>
</html>
