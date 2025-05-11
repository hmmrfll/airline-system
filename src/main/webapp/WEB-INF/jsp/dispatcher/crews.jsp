<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div class="container-fluid">
    <div class="row mb-4">
        <div class="col-md-6">
            <h2 class="mb-0"><i class="fas fa-user-friends text-primary me-2"></i> Управление бригадами</h2>
            <p class="text-muted">Управляйте составом летных бригад</p>
        </div>
        <div class="col-md-6 text-end">
            <a href="${pageContext.request.contextPath}/app?command=crewCreate" class="btn btn-primary">
                <i class="fas fa-plus me-1"></i> Создать новую бригаду
            </a>
        </div>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger rounded-pill" role="alert">
            <i class="fas fa-exclamation-circle me-2"></i> ${error}
        </div>
    </c:if>

    <c:if test="${not empty message}">
        <div class="alert alert-success rounded-pill" role="alert">
            <i class="fas fa-check-circle me-2"></i> ${message}
        </div>
    </c:if>

    <div class="row mb-4">
        <div class="col-12">
            <div class="card">
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0">
                            <thead>
                            <tr>
                                <th class="ps-4">ID</th>
                                <th>Название</th>
                                <th>Рейс</th>
                                <th>Состав</th>
                                <th>Статус</th>
                                <th>Создана</th>
                                <th class="text-end pe-4">Действия</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="crew" items="${crews}">
                                <tr>
                                    <td class="ps-4">${crew.id}</td>
                                    <td>
                                        <strong>${crew.name}</strong>
                                    </td>
                                    <td>
                                        <c:if test="${not empty crew.flight}">
                                            <span class="badge bg-primary">${crew.flight.flightNumber}</span>
                                            <span class="small text-muted d-block mt-1">
                                                    ${crew.flight.departureAirport} → ${crew.flight.arrivalAirport}
                                                </span>
                                        </c:if>
                                        <c:if test="${empty crew.flight}">
                                            <span class="badge bg-secondary">Не назначен</span>
                                        </c:if>
                                    </td>
                                    <td>
                                        <div class="d-flex align-items-center">
                                            <div class="crew-count me-2 text-center">
                                                <span class="badge bg-secondary">${crew.members.size()}</span>
                                            </div>
                                            <div class="crew-members">
                                                <c:forEach var="member" items="${crew.members}" end="1">
                                                    <div class="small">
                                                        <span class="fw-bold">${member.position.name}:</span>
                                                            ${member.lastName} ${member.firstName}
                                                    </div>
                                                </c:forEach>
                                                <c:if test="${crew.members.size() > 2}">
                                                    <div class="small text-muted">
                                                        + еще ${crew.members.size() - 2}
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${crewService.checkCrewCompletion(crew)}">
                                                <span class="badge bg-success">Полный состав</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-warning">Неполный состав</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <span class="text-muted">${crew.formattedCreatedAt}</span>
                                    </td>
                                    <td class="text-end pe-4">
                                        <div class="btn-group">
                                            <a href="${pageContext.request.contextPath}/app?command=crewEdit&id=${crew.id}"
                                               class="btn btn-sm btn-outline-primary" title="Редактировать информацию">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/app?command=crewEdit&id=${crew.id}&membersMode=true"
                                               class="btn btn-sm btn-outline-info" title="Управление составом">
                                                <i class="fas fa-users"></i>
                                            </a>
                                            <button type="button"
                                                    onclick="confirmDelete(${crew.id}, '${crew.name}', 'бригаду', 'crewDelete')"
                                                    class="btn btn-sm btn-outline-danger" title="Удалить бригаду">
                                                <i class="fas fa-trash"></i>
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Универсальное модальное окно для подтверждения удаления -->
<div class="modal fade" id="deleteModal" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header bg-danger text-white">
                <h5 class="modal-title">Подтверждение удаления</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="text-center mb-3">
                    <i class="fas fa-exclamation-triangle text-danger fa-3x"></i>
                </div>
                <p class="text-center fs-5">Вы действительно хотите удалить <span id="deleteItemType"></span> <strong id="deleteItemName"></strong>?</p>
                <p class="text-center text-muted small">Это действие нельзя будет отменить</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Отмена</button>
                <a href="#" id="deleteLink" class="btn btn-danger">
                    <i class="fas fa-trash me-1"></i> Удалить
                </a>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>