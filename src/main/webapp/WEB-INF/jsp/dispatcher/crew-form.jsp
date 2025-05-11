<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div class="container">
    <div class="row mb-3">
        <div class="col-md-8">
            <h2>${formAction eq 'create' ? 'Создание новой бригады' : 'Редактирование бригады'}</h2>
        </div>
        <div class="col-md-4 text-right">
            <a href="${pageContext.request.contextPath}/app?command=crewList" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> Назад к списку
            </a>
        </div>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger" role="alert">
                ${error}
        </div>
    </c:if>

    <div class="card">
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/app" method="post">
                <input type="hidden" name="command" value="${formAction eq 'create' ? 'crewCreate' : 'crewEdit'}">
                <c:if test="${formAction eq 'edit'}">
                    <input type="hidden" name="id" value="${crew.id}">
                </c:if>

                <div class="form-group row">
                    <label for="name" class="col-sm-3 col-form-label">Название бригады *</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" id="name" name="name"
                               value="${crew.name}" required>
                    </div>
                </div>

                <div class="form-group row">
                    <label for="flightId" class="col-sm-3 col-form-label">Рейс</label>
                    <div class="col-sm-9">
                        <select class="form-control" id="flightId" name="flightId">
                            <option value="">Выберите рейс (необязательно)</option>
                            <c:forEach var="flight" items="${flights}">
                                <option value="${flight.id}"
                                    ${crew.flight.id eq flight.id ? 'selected' : ''}>
                                        ${flight.flightNumber} (${flight.departureAirport} - ${flight.arrivalAirport})
                                    | ${flight.departureTime}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-group row">
                    <div class="col-sm-9 offset-sm-3">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i> ${formAction eq 'create' ? 'Создать' : 'Сохранить'}
                        </button>
                        <c:if test="${formAction eq 'edit'}">
                            <a href="${pageContext.request.contextPath}/app?command=crewEdit&id=${crew.id}&membersMode=true"
                               class="btn btn-info">
                                <i class="fas fa-users"></i> Перейти к управлению составом
                            </a>
                        </c:if>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>