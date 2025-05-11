<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div class="container">
    <div class="row mb-3">
        <div class="col-md-8">
            <h2>${formAction eq 'create' ? 'Создание нового рейса' : 'Редактирование рейса'}</h2>
        </div>
        <div class="col-md-4 text-right">
            <a href="${pageContext.request.contextPath}/app?command=flightList" class="btn btn-secondary">
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
                <input type="hidden" name="command" value="${formAction eq 'create' ? 'flightCreate' : 'flightEdit'}">
                <c:if test="${formAction eq 'edit'}">
                    <input type="hidden" name="id" value="${flight.id}">
                </c:if>

                <div class="form-group row">
                    <label for="flightNumber" class="col-sm-3 col-form-label">Номер рейса *</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" id="flightNumber" name="flightNumber"
                               value="${flight.flightNumber}" placeholder="Например: SU1234" required
                               pattern="[A-Z]{2}[0-9]{3,4}" title="Формат: две заглавные буквы и 3-4 цифры">
                        <small class="form-text text-muted">Формат: две заглавные буквы и 3-4 цифры (например, SU1234)</small>
                    </div>
                </div>

                <div class="form-group row">
                    <label for="departureAirport" class="col-sm-3 col-form-label">Аэропорт вылета *</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" id="departureAirport" name="departureAirport"
                               value="${flight.departureAirport}" required>
                    </div>
                </div>

                <div class="form-group row">
                    <label for="arrivalAirport" class="col-sm-3 col-form-label">Аэропорт прилета *</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" id="arrivalAirport" name="arrivalAirport"
                               value="${flight.arrivalAirport}" required>
                    </div>
                </div>

                <div class="form-group row">
                    <label for="departureTime" class="col-sm-3 col-form-label">Время вылета *</label>
                    <div class="col-sm-9">
                        <input type="datetime-local" class="form-control" id="departureTime" name="departureTime"
                               value="${flight.departureTime != null ? flight.departureTime.toString().substring(0, 16) : ''}" required>
                    </div>
                </div>

                <div class="form-group row">
                    <label for="arrivalTime" class="col-sm-3 col-form-label">Время прилета *</label>
                    <div class="col-sm-9">
                        <input type="datetime-local" class="form-control" id="arrivalTime" name="arrivalTime"
                               value="${flight.arrivalTime != null ? flight.arrivalTime.toString().substring(0, 16) : ''}" required>
                    </div>
                </div>

                <div class="form-group row">
                    <label for="aircraftType" class="col-sm-3 col-form-label">Тип самолета *</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" id="aircraftType" name="aircraftType"
                               value="${flight.aircraftType}" required>
                    </div>
                </div>

                <div class="form-group row">
                    <label for="status" class="col-sm-3 col-form-label">Статус *</label>
                    <div class="col-sm-9">
                        <select class="form-control" id="status" name="status" required>
                            <option value="SCHEDULED" ${flight.status eq 'SCHEDULED' ? 'selected' : ''}>Запланирован</option>
                            <option value="DELAYED" ${flight.status eq 'DELAYED' ? 'selected' : ''}>Задержан</option>
                            <option value="CANCELLED" ${flight.status eq 'CANCELLED' ? 'selected' : ''}>Отменен</option>
                            <option value="COMPLETED" ${flight.status eq 'COMPLETED' ? 'selected' : ''}>Выполнен</option>
                        </select>
                    </div>
                </div>

                <div class="form-group row">
                    <div class="col-sm-9 offset-sm-3">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i> ${formAction eq 'create' ? 'Создать' : 'Сохранить'}
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>