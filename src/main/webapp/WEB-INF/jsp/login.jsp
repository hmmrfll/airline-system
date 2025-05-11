<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AirSystem - Вход в систему</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet">
    <style>
        :root {
            --primary-color: #FF5C93;
            --secondary-color: #0D223F;
            --accent-color: #00BA98;
            --light-gray: #F5F5F5;
            --medium-gray: #E0E0E0;
            --text-dark: #333333;
            --text-light: #666666;
            --white: #FFFFFF;
        }

        body {
            background: linear-gradient(135deg, var(--secondary-color) 0%, #1A365D 100%);
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: 'Roboto', sans-serif;
            margin: 0;
            padding: 20px;
            overflow-x: hidden;
        }

        .login-container {
            max-width: 400px;
            width: 100%;
            background-color: var(--white);
            border-radius: 20px;
            box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
            overflow: hidden;
            position: relative;
        }

        .login-header {
            background-color: var(--primary-color);
            padding: 30px 20px;
            text-align: center;
            position: relative;
        }

        .login-header h1 {
            color: var(--white);
            font-weight: 700;
            margin: 0;
            font-size: 28px;
        }

        .login-header p {
            color: rgba(255, 255, 255, 0.8);
            margin-top: 5px;
            font-size: 16px;
        }

        .login-header .plane-icon {
            position: absolute;
            top: 15px;
            right: 15px;
            font-size: 24px;
            color: var(--white);
            animation: fly 4s infinite;
        }

        @keyframes fly {
            0% { transform: translate(0, 0) rotate(0deg); }
            25% { transform: translate(-10px, -10px) rotate(-5deg); }
            50% { transform: translate(0, -15px) rotate(0deg); }
            75% { transform: translate(10px, -10px) rotate(5deg); }
            100% { transform: translate(0, 0) rotate(0deg); }
        }

        .login-body {
            padding: 30px;
        }

        .form-group {
            margin-bottom: 25px;
            position: relative;
        }

        .form-control {
            height: 50px;
            border-radius: 25px;
            padding: 10px 20px;
            font-size: 16px;
            border: 1px solid var(--medium-gray);
            background-color: var(--light-gray);
            transition: all 0.3s ease;
        }

        .form-control:focus {
            border-color: var(--primary-color);
            box-shadow: 0 0 0 0.2rem rgba(255, 92, 147, 0.25);
            background-color: var(--white);
        }

        .form-label {
            margin-bottom: 8px;
            font-weight: 500;
            color: var(--text-dark);
            font-size: 14px;
            padding-left: 5px;
        }

        .input-icon {
            position: absolute;
            right: 15px;
            top: 40px;
            color: var(--text-light);
        }

        .btn-login {
            height: 50px;
            border-radius: 25px;
            background-color: var(--primary-color);
            border: none;
            color: var(--white);
            font-weight: 500;
            font-size: 16px;
            padding: 0 20px;
            width: 100%;
            margin-top: 10px;
            box-shadow: 0 4px 15px rgba(255, 92, 147, 0.3);
            transition: all 0.3s ease;
        }

        .btn-login:hover {
            background-color: #e5527f;
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(255, 92, 147, 0.4);
        }

        .alert {
            border-radius: 15px;
            padding: 15px;
            margin-bottom: 20px;
            border: none;
        }

        .alert-danger {
            background-color: #ffebef;
            color: #d63031;
        }

        .alert-success {
            background-color: #e6fff8;
            color: #00b894;
        }

        .register-link {
            text-align: center;
            margin-top: 25px;
        }

        .register-link a {
            color: var(--primary-color);
            text-decoration: none;
            font-weight: 500;
            transition: all 0.3s ease;
        }

        .register-link a:hover {
            text-decoration: underline;
            color: #e5527f;
        }

        /* Decoration elements */
        .decoration {
            position: absolute;
            z-index: -1;
        }

        .decoration-1 {
            width: 150px;
            height: 150px;
            background-color: rgba(255, 92, 147, 0.2);
            border-radius: 50%;
            top: -75px;
            left: -75px;
        }

        .decoration-2 {
            width: 100px;
            height: 100px;
            background-color: rgba(0, 186, 152, 0.2);
            border-radius: 50%;
            bottom: -50px;
            right: -50px;
        }
    </style>
</head>
<body>
<div class="decoration decoration-1"></div>
<div class="decoration decoration-2"></div>

<div class="login-container">
    <div class="login-header">
        <i class="fas fa-plane-departure plane-icon"></i>
        <h1>AirSystem</h1>
        <p>Система управления авиакомпанией</p>
    </div>

    <div class="login-body">
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
            <input type="hidden" name="command" value="login">

            <div class="form-group">
                <label for="username" class="form-label">Имя пользователя</label>
                <input type="text" class="form-control" id="username" name="username"
                       required placeholder="Введите имя пользователя">
                <i class="fas fa-user input-icon"></i>
            </div>

            <div class="form-group">
                <label for="password" class="form-label">Пароль</label>
                <input type="password" class="form-control" id="password" name="password"
                       required placeholder="Введите пароль">
                <i class="fas fa-lock input-icon"></i>
            </div>

            <button type="submit" class="btn btn-login">
                Войти в систему
            </button>

            <div class="register-link">
                <p>Нет аккаунта? <a href="${pageContext.request.contextPath}/app?command=register">Зарегистрироваться</a></p>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Add some animations
        const loginContainer = document.querySelector('.login-container');
        loginContainer.style.opacity = '0';
        loginContainer.style.transform = 'translateY(20px)';

        setTimeout(() => {
            loginContainer.style.transition = 'all 0.5s ease';
            loginContainer.style.opacity = '1';
            loginContainer.style.transform = 'translateY(0)';
        }, 100);

        // Input focus effects
        const inputs = document.querySelectorAll('.form-control');
        inputs.forEach(input => {
            input.addEventListener('focus', function() {
                this.parentElement.querySelector('.input-icon').style.color = '#FF5C93';
            });

            input.addEventListener('blur', function() {
                this.parentElement.querySelector('.input-icon').style.color = '#666666';
            });
        });
    });
</script>
</body>
</html>