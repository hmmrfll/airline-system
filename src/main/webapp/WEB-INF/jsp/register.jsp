<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Регистрация - Авиакомпания</title>
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
        .register-container {
            max-width: 550px;
            width: 100%;
            padding: 30px;
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
            margin: 0 auto; /* Добавить это */
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
        .btn-register {
            border-radius: 0.5rem;
            padding: 0.75rem 1rem;
            background-color: #0d6efd;
            border: none;
            font-weight: 500;
            transition: all 0.3s;
        }
        .btn-register:hover {
            background-color: #0b5ed7;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .login-link {
            text-align: center;
            margin-top: 20px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="register-container">
        <div class="logo">
            <h1>Авиакомпания</h1>
            <p>Регистрация в системе управления</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger d-flex align-items-center" role="alert">
                <i class="fas fa-exclamation-circle me-2"></i>
                <div>${error}</div>
            </div>
        </c:if>

        <c:if test="${not empty success}">
            <div class="alert alert-success d-flex align-items-center" role="alert">
                <i class="fas fa-check-circle me-2"></i>
                <div>${success}</div>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/app" method="post">
            <input type="hidden" name="command" value="register">

            <div class="row mb-3">
                <div class="col-md-6">
                    <label for="firstName" class="form-label">Имя</label>
                    <input type="text" class="form-control" id="firstName" name="firstName" required placeholder="Введите имя">
                </div>
                <div class="col-md-6">
                    <label for="lastName" class="form-label">Фамилия</label>
                    <input type="text" class="form-control" id="lastName" name="lastName" required placeholder="Введите фамилию">
                </div>
            </div>

            <div class="mb-3">
                <label for="username" class="form-label">Имя пользователя</label>
                <input type="text" class="form-control" id="username" name="username" required placeholder="Введите имя пользователя">
            </div>

            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control" id="email" name="email" required placeholder="Введите email">
            </div>

            <div class="row mb-4">
                <div class="col-md-6">
                    <label for="password" class="form-label">Пароль</label>
                    <input type="password" class="form-control" id="password" name="password" required placeholder="Введите пароль">
                </div>
                <div class="col-md-6">
                    <label for="confirmPassword" class="form-label">Подтверждение пароля</label>
                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required placeholder="Повторите пароль">
                </div>
            </div>

            <button type="submit" class="btn btn-register btn-primary w-100">
                <i class="fas fa-user-plus me-2"></i> Зарегистрироваться
            </button>

            <div class="login-link">
                <p>Уже зарегистрированы? <a href="${pageContext.request.contextPath}/app?command=login">Войти</a></p>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>