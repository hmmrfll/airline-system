<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div class="container">
    <div class="jumbotron">
        <h1>Панель диспетчера</h1>
        <p>Добро пожаловать в систему управления авиакомпанией</p>
    </div>

    <div class="row">
        <div class="col-md-6">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">Управление бригадами</h5>
                    <p class="card-text">Формирование летных бригад для рейсов</p>
                    <a href="${pageContext.request.contextPath}/app?command=crewList" class="btn btn-primary">
                        <i class="fas fa-user-friends"></i> Перейти к бригадам
                    </a>
                </div>
            </div>
        </div>

        <div class="col-md-6">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">Просмотр рейсов</h5>
                    <p class="card-text">Просмотр информации о рейсах</p>
                    <a href="${pageContext.request.contextPath}/app?command=flightList" class="btn btn-primary">
                        <i class="fas fa-plane"></i> Перейти к рейсам
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>