<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>404 - Страница не найдена</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <style>
    body {
      background-color: #f5f5f5;
      padding-top: 50px;
    }
    .error-template {
      padding: 40px 15px;
      text-align: center;
    }
    .error-actions {
      margin-top: 15px;
      margin-bottom: 15px;
    }
    .error-actions .btn {
      margin-right: 10px;
    }
  </style>
</head>
<body>
<div class="container">
  <div class="row">
    <div class="col-md-12">
      <div class="error-template">
        <h1>Упс!</h1>
        <h2>404 - Страница не найдена</h2>
        <div class="error-details">
          Извините, запрошенная страница не найдена!
        </div>
        <div class="error-actions">
          <a href="${pageContext.request.contextPath}/app" class="btn btn-primary">
            <i class="fas fa-home"></i> На главную
          </a>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>