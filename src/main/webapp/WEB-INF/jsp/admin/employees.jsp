<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div class="container-fluid">
    <div class="row mb-3">
        <div class="col-md-8">
            <h2>Управление сотрудниками</h2>
        </div>
        <div class="col-md-4 text-right">
            <a href="${pageContext.request.contextPath}/app?command=employeeCreate" class="btn btn-primary">
                <i class="fas fa-plus"></i> Добавить сотрудника
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

    <div class="table-responsive">
        <table class="table table-striped table-bordered">
            <thead class="thead-dark">
            <tr>
                <th>ID</th>
                <th>ФИО</th>
                <th>Должность</th>
                <th>Дата найма</th>
                <th>Опыт (мес.)</th>
                <th>Паспорт</th>
                <th>Контакты</th>
                <th>Статус</th>
                <th>Действия</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="employee" items="${employees}">
                <tr>
                    <td>${employee.id}</td>
                    <td>${employee.lastName} ${employee.firstName} ${employee.middleName}</td>
                    <td>${employee.position.name}</td>
                    <td><fmt:formatDate value="${employee.hireDate}" pattern="dd.MM.yyyy" /></td>
                    <td>${employee.experience}</td>
                    <td>${employee.passport}</td>
                    <td>${employee.contactInfo}</td>
                    <td>
                        <c:choose>
                            <c:when test="${employee.active}">
                                <span class="badge badge-success">Активен</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-danger">Неактивен</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <div class="btn-group" role="group">
                            <a href="${pageContext.request.contextPath}/app?command=employeeEdit&id=${employee.id}"
                               class="btn btn-sm btn-info">
                                <i class="fas fa-edit"></i>
                            </a>
                            <a href="#" onclick="confirmDelete(${employee.id}, '${employee.lastName} ${employee.firstName}')"
                               class="btn btn-sm btn-danger">
                                <i class="fas fa-trash"></i>
                            </a>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<!-- Модальное окно для подтверждения удаления -->
<div class="modal fade" id="deleteModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Подтверждение удаления</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p>Вы действительно хотите удалить сотрудника <span id="employeeName"></span>?</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Отмена</button>
                <a href="#" id="deleteLink" class="btn btn-danger">Удалить</a>
            </div>
        </div>
    </div>
</div>

<script>
    function confirmDelete(id, employeeName) {
        document.getElementById('employeeName').textContent = employeeName;
        document.getElementById('deleteLink').href = '${pageContext.request.contextPath}/app?command=employeeDelete&id=' + id;
        $('#deleteModal').modal('show');
    }
</script>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>