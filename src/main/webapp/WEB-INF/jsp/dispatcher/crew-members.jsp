<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div class="container-fluid">
    <div class="row mb-3">
        <div class="col-md-8">
            <h2>Управление составом бригады: ${crew.name}</h2>
            <c:if test="${not empty crew.flight}">
                <p class="text-muted">
                    Рейс: ${crew.flight.flightNumber} (${crew.flight.departureAirport} - ${crew.flight.arrivalAirport})
                </p>
            </c:if>
        </div>
        <div class="col-md-4 text-right">
            <a href="${pageContext.request.contextPath}/app?command=crewList" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> Назад к списку
            </a>
            <a href="${pageContext.request.contextPath}/app?command=crewEdit&id=${crew.id}" class="btn btn-primary">
                <i class="fas fa-edit"></i> Изменить информацию
            </a>
        </div>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger" role="alert">
                ${error}
        </div>
    </c:if>

    <c:if test="${not empty message}">
        <div class="alert alert-success" role="alert">
                ${message}
        </div>
    </c:if>

    <div class="row">
        <div class="col-md-6">
            <div class="card mb-4">
                <div class="card-header bg-primary text-white">
                    <h5 class="mb-0">Текущий состав бригады</h5>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty crew.members}">
                            <p class="text-center text-muted">В бригаде пока нет сотрудников</p>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-striped">
                                    <thead>
                                    <tr>
                                        <th>ФИО</th>
                                        <th>Должность</th>
                                        <th>Опыт</th>
                                        <th>Действия</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="member" items="${crew.members}">
                                        <tr>
                                            <td>${member.lastName} ${member.firstName} ${member.middleName}</td>
                                            <td>${member.position.name}</td>
                                            <td>${member.experience} мес.</td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/app?command=crewRemoveMember&crewId=${crew.id}&employeeId=${member.id}"
                                                   class="btn btn-sm btn-danger"
                                                   onclick="return confirm('Удалить сотрудника из бригады?')">
                                                    <i class="fas fa-user-minus"></i>
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>

                            <div class="card mt-3">
                                <div class="card-header">
                                    Статус комплектации
                                </div>
                                <div class="card-body">
                                    <ul class="list-group">
                                        <li class="list-group-item d-flex justify-content-between align-items-center">
                                            Пилот
                                            <c:choose>
                                                <c:when test="${crew.members.stream().anyMatch(e -> e.position.name eq 'PILOT').orElse(false)}">
                                                    <span class="badge badge-success"><i class="fas fa-check"></i></span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-danger"><i class="fas fa-times"></i></span>
                                                </c:otherwise>
                                            </c:choose>
                                        </li>
                                        <li class="list-group-item d-flex justify-content-between align-items-center">
                                            Второй пилот
                                            <c:choose>
                                                <c:when test="${crew.members.stream().anyMatch(e -> e.position.name eq 'CO_PILOT').orElse(false)}">
                                                    <span class="badge badge-success"><i class="fas fa-check"></i></span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-danger"><i class="fas fa-times"></i></span>
                                                </c:otherwise>
                                            </c:choose>
                                        </li>
                                        <li class="list-group-item d-flex justify-content-between align-items-center">
                                            Штурман
                                            <c:choose>
                                                <c:when test="${crew.members.stream().anyMatch(e -> e.position.name eq 'NAVIGATOR').orElse(false)}">
                                                    <span class="badge badge-success"><i class="fas fa-check"></i></span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-danger"><i class="fas fa-times"></i></span>
                                                </c:otherwise>
                                            </c:choose>
                                        </li>
                                        <li class="list-group-item d-flex justify-content-between align-items-center">
                                            Радист
                                            <c:choose>
                                                <c:when test="${crew.members.stream().anyMatch(e -> e.position.name eq 'RADIO_OPERATOR').orElse(false)}">
                                                    <span class="badge badge-success"><i class="fas fa-check"></i></span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-danger"><i class="fas fa-times"></i></span>
                                                </c:otherwise>
                                            </c:choose>
                                        </li>
                                        <li class="list-group-item d-flex justify-content-between align-items-center">
                                            Стюард/Стюардесса
                                            <c:choose>
                                                <c:when test="${crew.members.stream().anyMatch(e -> e.position.name eq 'STEWARD' || e.position.name eq 'STEWARDESS').orElse(false)}">
                                                    <span class="badge badge-success"><i class="fas fa-check"></i></span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-danger"><i class="fas fa-times"></i></span>
                                                </c:otherwise>
                                            </c:choose>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <div class="col-md-6">
            <div class="card">
                <div class="card-header bg-success text-white">
                    <h5 class="mb-0">Добавить сотрудников в бригаду</h5>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty availableEmployees}">
                            <p class="text-center text-muted">Нет доступных сотрудников для добавления</p>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-striped">
                                    <thead>
                                    <tr>
                                        <th>ФИО</th>
                                        <th>Должность</th>
                                        <th>Опыт</th>
                                        <th>Действия</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="employee" items="${availableEmployees}">
                                        <tr>
                                            <td>${employee.lastName} ${employee.firstName} ${employee.middleName}</td>
                                            <td>${employee.position.name}</td>
                                            <td>${employee.experience} мес.</td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/app?command=crewAddMember&crewId=${crew.id}&employeeId=${employee.id}"
                                                   class="btn btn-sm btn-success">
                                                    <i class="fas fa-user-plus"></i>
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>