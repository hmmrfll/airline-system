<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div class="container-fluid">
    <div class="row mb-3">
        <div class="col-md-8">
            <h2>Управление рейсами</h2>
        </div>
        <div class="col-md-4 text-right">
            <c:if test="${userRole eq 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/app?command=flightCreate" class="btn btn-primary">
                    <i class="fas fa-plus"></i> Добавить рейс
                </a>
            </c:if>
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
                <th>Номер рейса</th>
                <th>Откуда</th>
                <th>Куда</th>
                <th>Дата вылета</th>
                <th>Дата прилета</th>
                <th>Тип самолета</th>
                <th>Статус</th>
                <c:if test="${userRole eq 'ADMIN'}">
                    <th>Действия</th>
                </c:if>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="flight" items="${flights}">
                <tr>
                    <td>${flight.id}</td>
                    <td>${flight.flightNumber}</td>
                    <td>${flight.departureAirport}</td>
                    <td>${flight.arrivalAirport}</td>
                    <td>${flight.formattedDepartureTime}</td>
                    <td>${flight.formattedArrivalTime}</td>
                    <td>${flight.aircraftType}</td>
                    <td>
                        <c:choose>
                            <c:when test="${flight.status eq 'SCHEDULED'}">
                                <span class="badge badge-primary">Запланирован</span>
                            </c:when>
                            <c:when test="${flight.status eq 'DELAYED'}">
                                <span class="badge badge-warning">Задержан</span>
                            </c:when>
                            <c:when test="${flight.status eq 'CANCELLED'}">
                                <span class="badge badge-danger">Отменен</span>
                            </c:when>
                            <c:when test="${flight.status eq 'COMPLETED'}">
                                <span class="badge badge-success">Выполнен</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-secondary">${flight.status}</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <c:if test="${userRole eq 'ADMIN'}">
                        <td>
                            <div class="btn-group" role="group">
                                <a href="${pageContext.request.contextPath}/app?command=flightEdit&id=${flight.id}"
                                   class="btn btn-sm btn-info">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <a href="#" onclick="confirmDelete(${flight.id}, '${flight.flightNumber}')"
                                   class="btn btn-sm btn-danger">
                                    <i class="fas fa-trash"></i>
                                </a>
                            </div>
                        </td>
                    </c:if>
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
                <p>Вы действительно хотите удалить рейс <span id="flightNumber"></span>?</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Отмена</button>
                <a href="#" id="deleteLink" class="btn btn-danger">Удалить</a>
            </div>
        </div>
    </div>
</div>

<script>
    function confirmDelete(id, flightNumber) {
        document.getElementById('flightNumber').textContent = flightNumber;
        document.getElementById('deleteLink').href = '${pageContext.request.contextPath}/app?command=flightDelete&id=' + id;
        $('#deleteModal').modal('show');
    }
</script>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>