<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div class="container-fluid">
    <div class="row mb-3">
        <div class="col-md-8">
            <h2>Управление летными бригадами</h2>
        </div>
        <div class="col-md-4 text-right">
            <a href="${pageContext.request.contextPath}/app?command=crewCreate" class="btn btn-primary">
                <i class="fas fa-plus"></i> Создать бригаду
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
                <th>Название</th>
                <th>Рейс</th>
                <th>Кол-во членов</th>
                <th>Состав</th>
                <th>Статус</th>
                <th>Создан</th>
                <th>Действия</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="crew" items="${crews}">
                <tr>
                    <td>${crew.id}</td>
                    <td>${crew.name}</td>
                    <td>
                        <c:if test="${not empty crew.flight}">
                            ${crew.flight.flightNumber} (${crew.flight.departureAirport} - ${crew.flight.arrivalAirport})
                        </c:if>
                    </td>
                    <td>${crew.members.size()}</td>
                    <td>
                        <ul class="list-unstyled mb-0">
                            <c:forEach var="member" items="${crew.members}" end="2">
                                <li>${member.position.name}: ${member.lastName} ${member.firstName}</li>
                            </c:forEach>
                            <c:if test="${crew.members.size() > 3}">
                                <li>...</li>
                            </c:if>
                        </ul>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${crewService.checkCrewCompletion(crew)}">
                                <span class="badge badge-success">Полный состав</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-warning">Неполный состав</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td><fmt:formatDate value="${crew.createdAt}" pattern="dd.MM.yyyy HH:mm" /></td>
                    <td>
                        <div class="btn-group" role="group">
                            <a href="${pageContext.request.contextPath}/app?command=crewEdit&id=${crew.id}"
                               class="btn btn-sm btn-info" title="Редактировать информацию">
                                <i class="fas fa-edit"></i>
                            </a>
                            <a href="${pageContext.request.contextPath}/app?command=crewEdit&id=${crew.id}&membersMode=true"
                               class="btn btn-sm btn-primary" title="Управление составом">
                                <i class="fas fa-users"></i>
                            </a>
                            <a href="#" onclick="confirmDelete(${crew.id}, '${crew.name}')"
                               class="btn btn-sm btn-danger" title="Удалить бригаду">
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
                <p>Вы действительно хотите удалить бригаду <span id="crewName"></span>?</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Отмена</button>
                <a href="#" id="deleteLink" class="btn btn-danger">Удалить</a>
            </div>
        </div>
    </div>
</div>

<script>
    function confirmDelete(id, crewName) {
        document.getElementById('crewName').textContent = crewName;
        document.getElementById('deleteLink').href = '${pageContext.request.contextPath}/app?command=crewDelete&id=' + id;
        $('#deleteModal').modal('show');
    }
</script>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>