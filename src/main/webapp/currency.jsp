<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Currencies</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: linear-gradient(135deg, #4facfe, #00f2fe);
            margin: 0;
            padding: 0;
        }

        .container {
            width: 90%;
            margin: 30px auto;
            display: flex;
            gap: 20px;
            flex-wrap: wrap;
            justify-content: space-between;
        }

        .curList, .addForm {
            flex: 1;
            min-width: 400px;
            background: #fff;
            border-radius: 20px;
            padding: 20px 30px;
            box-shadow: 0 6px 16px rgba(0,0,0,0.15);
        }

        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 20px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            border-radius: 10px;
            overflow: hidden;
        }

        th, td {
            padding: 12px;
            text-align: center;
            border-bottom: 1px solid #ddd;
        }

        th {
            background: #4facfe;
            color: white;
        }

        tr:hover {
            background: #f1faff;
        }

        button {
            padding: 6px 12px;
            border-radius: 8px;
            border: none;
            background: #ff4d4d;
            color: white;
            cursor: pointer;
            transition: 0.3s;
        }

        button:hover {
            background: #e60000;
            transform: scale(1.05);
        }

        input {
            padding: 8px 12px;
            border-radius: 8px;
            border: 1px solid #ccc;
            font-size: 14px;
        }

        input[type="submit"] {
            background: #4facfe;
            color: white;
            border: none;
            cursor: pointer;
            transition: 0.3s;
            padding: 10px 18px;
        }

        input[type="submit"]:hover {
            background: #00c6fb;
            transform: scale(1.05);
        }

        form {
            margin: 10px 0;
            text-align: center;
        }

        .addForm h1 {
            font-size: 20px;
            margin-bottom: 15px;
        }

        .addForm p {
            margin: 12px 0;
        }
    </style>
    <script src="js/scripts.js"></script>
</head>
<body>
<div class="container">
    <div class="curList">
        <h1>Список валют</h1>
        <table>
            <tr>
                <th>Код валюты</th>
                <th>Полное название</th>
                <th>Обозначение</th>
                <th>Действие</th>
            </tr>
            <c:forEach var="currency" items="${curList}">
                <tr>
                    <td>${currency.code}</td>
                    <td>${currency.fullName}</td>
                    <td>${currency.sign}</td>
                    <td>
                        <button type="button" onClick="deleteCurrency(${currency.id})">Удалить</button>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>

    <div class="addForm">
        <form name="AddCurForm" action="" method="post">
            <h1>Добавить валюту</h1>
            <p>Код валюты <input name="code" type="text" placeholder="USD" required></p>
            <p>Название валюты <input name="fullName" type="text" placeholder="Dollar" required></p>
            <p>Символ валюты <input name="sign" type="text" placeholder="$" required></p>
            <p><input value="Добавить" type="submit"></p>
        </form>
        <form action="exchangeRates">
            <p><input value="Обменные курсы ➝" type="submit"></p>
        </form>
    </div>
</div>
</body>
</html>
