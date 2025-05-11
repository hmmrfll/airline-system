<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div class="container">
    <div class="row mb-4">
        <div class="col-12">
            <div class="card">
                <div class="card-body p-0">
                    <div class="bg-primary text-white p-4 rounded-top">
                        <h1 class="mb-0"><i class="fas fa-tachometer-alt me-2"></i> Панель диспетчера</h1>
                        <p class="mb-0 mt-2">Добро пожаловать в систему управления авиакомпанией, ${user.firstName} ${user.lastName}</p>
                    </div>
                    <div class="p-4">
                        <p class="lead">
                            Здесь вы можете управлять рейсами и формировать летные бригады.
                            Используйте навигацию слева для доступа к различным разделам системы.
                        </p>
                        <div class="alert alert-info">
                            <i class="fas fa-info-circle me-2"></i> Сегодня в системе доступно
                            <strong>${requestScope.flightsCount}</strong> рейсов и
                            <strong>${requestScope.crewsCount}</strong> бригад.
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-6 mb-4">
            <div class="card h-100">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="mb-0"><i class="fas fa-user-friends text-primary me-2"></i> Управление бригадами</h5>
                    <span class="badge bg-primary">Диспетчер</span>
                </div>
                <div class="card-body">
                    <p>Формируйте летные бригады для рейсов, назначайте экипажи и управляйте составом персонала.</p>
                    <ul class="list-group list-group-flush mb-3">
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <span><i class="fas fa-plus-circle text-success me-2"></i> Создание бригад</span>
                            <i class="fas fa-check text-success"></i>
                        </li>
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <span><i class="fas fa-user-plus me-2"></i> Добавление членов экипажа</span>
                            <i class="fas fa-check text-success"></i>
                        </li>
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <span><i class="fas fa-sync-alt me-2"></i> Управление составом</span>
                            <i class="fas fa-check text-success"></i>
                        </li>
                    </ul>
                    <a href="${pageContext.request.contextPath}/app?command=crewList" class="btn btn-primary w-100">
                        <i class="fas fa-user-friends me-2"></i> Перейти к бригадам
                    </a>
                </div>
            </div>
        </div>

        <div class="col-md-6 mb-4">
            <div class="card h-100">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="mb-0"><i class="fas fa-plane text-primary me-2"></i> Информация о рейсах</h5>
                    <span class="badge bg-secondary">Просмотр</span>
                </div>
                <div class="card-body">
                    <p>Просматривайте информацию о запланированных, текущих и завершенных рейсах авиакомпании.</p>
                    <ul class="list-group list-group-flush mb-3">
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <span><i class="fas fa-calendar-alt me-2"></i> Расписание рейсов</span>
                            <i class="fas fa-check text-success"></i>
                        </li>
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <span><i class="fas fa-info-circle me-2"></i> Детали рейсов</span>
                            <i class="fas fa-check text-success"></i>
                        </li>
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <span><i class="fas fa-search me-2"></i> Поиск рейсов</span>
                            <i class="fas fa-check text-success"></i>
                        </li>
                    </ul>
                    <a href="${pageContext.request.contextPath}/app?command=flightList" class="btn btn-secondary w-100">
                        <i class="fas fa-plane me-2"></i> Просмотреть рейсы
                    </a>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-12">
            <div class="card">
                <div class="card-header bg-light">
                    <h5 class="mb-0"><i class="fas fa-calendar-alt me-2"></i> Ближайшие рейсы</h5>
                </div>
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table mb-0">
                            <thead>
                            <tr>
                                <th>Номер рейса</th>
                                <th>Маршрут</th>
                                <th>Вылет</th>
                                <th>Статус</th>
                                <th>Экипаж</th>
                            </tr>
                            </thead>
                            <tbody>
                            <!-- Здесь должны быть данные о ближайших рейсах -->
                            <tr>
                                <td><span class="badge bg-primary">SU1234</span></td>
                                <td>Москва → Санкт-Петербург</td>
                                <td>14:30, 12 мая</td>
                                <td><span class="badge bg-success">Запланирован</span></td>
                                <td><span class="badge bg-danger">Не назначен</span></td>
                            </tr>
                            <tr>
                                <td><span class="badge bg-primary">SU2345</span></td>
                                <td>Москва → Сочи</td>
                                <td>16:45, 12 мая</td>
                                <td><span class="badge bg-success">Запланирован</span></td>
                                <td><span class="badge bg-danger">Не назначен</span></td>
                            </tr>
                            <tr>
                                <td><span class="badge bg-primary">SU3456</span></td>
                                <td>Москва → Казань</td>
                                <td>18:00, 12 мая</td>
                                <td><span class="badge bg-warning">Задержан</span></td>
                                <td><span class="badge bg-success">Назначен</span></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="card-footer">
                    <a href="${pageContext.request.contextPath}/app?command=flightList" class="btn btn-sm btn-outline-primary">
                        Посмотреть все рейсы <i class="fas fa-arrow-right ms-1"></i>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>