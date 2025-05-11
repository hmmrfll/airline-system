<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Вход в систему - Авиакомпания</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background-color: #f8f9fa;
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .login-container {
            max-width: 450px;
            width: 100%;
            padding: 30px;
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
        }
        .logo {
            text-align: center;
            margin-bottom: 30px;
        }
        .logo h1 {
            color: #0d6efd;
            font-weight: 600;
        }
        .logo p {
            color: #6c757d;
        }
        .form-control {
            border-radius: 0.5rem;
            padding: 0.75rem 1rem;
        }
        .btn-login {
            border-radius: 0.5rem;
            padding: 0.75rem 1rem;
            background-color: #0d6efd;
            border: none;
            font-weight: 500;
            transition: all 0.3s;
        }
        .btn-login:hover {
            background-color: #0b5ed7;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .input-group-text {
            border-radius: 0.5rem 0 0 0.5rem;
            background-color: #f8f9fa;
        }
        .form-control {
            border-left: 0;
            border-radius: 0 0.5rem 0.5rem 0;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="login-container">
        <div class="logo">
            <h1>Авиакомпания</h1>
            <p>Система управления персоналом и рейсами</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger d-flex align-items-center" role="alert">
                <i class="fas fa-exclamation-circle me-2"></i>
                <div>${error}</div>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/app" method="post">
            <input type="hidden" name="command" value="login">

            <div class="mb-4">
                <label for="username" class="form-label">Имя пользователя</label>
                <div class="input-group">
                    <span class="input-group-text"><i class="fas fa-user"></i></span>
                    <input type="text" class="form-control" id="username" name="username" required placeholder="Введите имя пользователя">
                </div>
            </div>

            <div class="mb-4">
                <label for="password" class="form-label">Пароль</label>
                <div class="input-group">
                    <span class="input-group-text"><i class="fas fa-lock"></i></span>
                    <input type="password" class="form-control" id="password" name="password" required placeholder="Введите пароль">
                </div>
            </div>

            <button type="submit" class="btn btn-login btn-primary w-100 mb-3">
                <i class="fas fa-sign-in-alt me-2"></i> Войти
            </button>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>