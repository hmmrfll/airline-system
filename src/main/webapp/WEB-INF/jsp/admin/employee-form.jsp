<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div class="container">
    <div class="row mb-3">
        <div class="col-md-8">
            <h2>${formAction eq 'create' ? 'Создание нового сотрудника' : 'Редактирование сотрудника'}</h2>
        </div>
        <div class="col-md-4 text-right">
            <a href="${pageContext.request.contextPath}/app?command=employeeList" class="btn btn-secondary">
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
                <input type="hidden" name="command" value="${formAction eq 'create' ? 'employeeCreate' : 'employeeEdit'}">
                <c:if test="${formAction eq 'edit'}">
                    <input type="hidden" name="id" value="${employee.id}">
                </c:if>

                <div class="form-group row">
                    <label for="lastName" class="col-sm-3 col-form-label">Фамилия *</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" id="lastName" name="lastName"
                               value="${employee.lastName}" required>
                    </div>
                </div>

                <div class="form-group row">
                    <label for="firstName" class="col-sm-3 col-form-label">Имя *</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" id="firstName" name="firstName"
                               value="${employee.firstName}" required>
                    </div>
                </div>

                <div class="form-group row">
                    <label for="middleName" class="col-sm-3 col-form-label">Отчество</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" id="middleName" name="middleName"
                               value="${employee.middleName}">
                    </div>
                </div>

                <div class="form-group row">
                    <label for="positionId" class="col-sm-3 col-form-label">Должность *</label>
                    <div class="col-sm-9">
                        <select class="form-control" id="positionId" name="positionId" required>
                            <option value="">Выберите должность</option>
                            <c:forEach var="position" items="${positions}">
                                <option value="${position.id}"
                                    ${employee.position.id eq position.id ? 'selected' : ''}>
                                        ${position.name} - ${position.description}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-group row">
                    <label for="hireDate" class="col-sm-3 col-form-label">Дата найма</label>
                    <div class="col-sm-9">
                        <input type="date" class="form-control" id="hireDate" name="hireDate"
                               value="${employee.hireDate != null ? employee.hireDate : ''}">
                    </div>
                </div>

                <div class="form-group row">
                    <label for="experience" class="col-sm-3 col-form-label">Опыт (мес.)</label>
                    <div class="col-sm-9">
                        <input type="number" class="form-control" id="experience" name="experience"
                               value="${employee.experience}" min="0">
                    </div>
                </div>

                <div class="form-group row">
                    <label for="passport" class="col-sm-3 col-form-label">Паспорт</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" id="passport" name="passport"
                               value="${employee.passport}">
                    </div>
                </div>

                <div class="form-group row">
                    <label for="contactInfo" class="col-sm-3 col-form-label">Контактная информация</label>
                    <div class="col-sm-9">
                        <textarea class="form-control" id="contactInfo" name="contactInfo" rows="3">${employee.contactInfo}</textarea>
                    </div>
                </div>

                <div class="form-group row">
                    <div class="col-sm-3">Статус</div>
                    <div class="col-sm-9">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="active" name="active"
                            ${empty employee.active || employee.active ? 'checked' : ''}>
                            <label class="form-check-label" for="active">
                                Активен
                            </label>
                        </div>
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