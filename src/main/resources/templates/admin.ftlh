<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Панель администратора</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<header th:insert="~{blocks/header :: header}"></header>
<main>
    <table border="1" class="table">
        <tr>
            <th>Номер телефона: </th>
            <th>Имя: </th>
            <th>Email: </th>
            <th>Пол: </th>
            <th>Роль: </th>
            <th>Активность: </th>
            <th>Редактировать: </th>
            <th>Бан: </th>
        </tr>

        <#list users as user>
        <tr>
            <th>${user.number_phone}</th>
            <th>${user.name}</th>
            <th>${user.email}</th>
            <th>${user.gender}</th>
            <th><#list user.roles as role>${role}</#list></th>
            <th><#if user.active>true<#else>false</#if></th>
            <th><a href="/admin/user/edit/${user.number_phone}">Редактировать</a></th>
            <th>
                <form action="/admin/user/ban/${user.number_phone}">

                    <input type="submit" value="Бан">
                </form>
            </th>
        </tr>
        <#else>
        <h3>Пользователей нет</h3>
    </#list>
    </table>
</main>
<div th:insert="~{blocks/footer :: footer}"></div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js" integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy" crossorigin="anonymous"></script>
</body>
</html>