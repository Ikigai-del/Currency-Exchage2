<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Exchange Rates</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: linear-gradient(135deg, #4facfe, #00f2fe);
            margin: 0;
            padding: 0;
        }

        .container {
            width: 95%;
            max-width: 1400px;
            margin: 40px auto;
            display: grid;
            grid-template-columns: 2fr 1fr;
            gap: 30px;
        }

        .card {
            background: #fff;
            border-radius: 20px;
            padding: 25px 30px;
            box-shadow: 0 8px 20px rgba(0,0,0,0.15);
            transition: transform .3s ease, box-shadow .3s ease;
        }

        .card:hover {
            transform: translateY(-6px);
            box-shadow: 0 12px 28px rgba(0,0,0,0.25);
        }

        h1 {
            text-align: center;
            margin-bottom: 20px;
            font-size: 22px;
            font-weight: 600;
            color: #333;
        }

        /* Таблица */
        table {
            width: 100%;
            border-collapse: collapse;
            border-radius: 12px;
            overflow: hidden;
            font-size: 15px;
        }

        th, td {
            padding: 14px;
            text-align: center;
            border-bottom: 1px solid #eee;
        }

        th {
            background: #4facfe;
            color: #fff;
            font-weight: 600;
        }

        tr:hover td {
            background: #f1faff;
        }

        /* Формы */
        form {
            margin: 15px 0;
            text-align: center;
        }

        input, button {
            padding: 10px 14px;
            border-radius: 10px;
            border: 1px solid #ccc;
            font-size: 14px;
            outline: none;
            transition: 0.3s;
        }

        input:focus {
            border-color: #4facfe;
            box-shadow: 0 0 6px rgba(79,172,254,0.4);
        }

        button, input[type="submit"] {
            background: #4facfe;
            color: white;
            font-weight: 500;
            border: none;
            cursor: pointer;
            transition: 0.3s;
        }

        button:hover, input[type="submit"]:hover {
            background: #00c6fb;
            transform: scale(1.05);
        }

        /* Блок с формами */
        .forms {
            display: flex;
            flex-direction: column;
            gap: 25px;
        }

        .forms .card {
            margin: 0;
        }

        output {
            font-weight: bold;
            color: #0077cc;
            font-size: 18px;
        }

        .btn {
            display: inline-block;
            padding: 10px 16px;
            border-radius: 12px;
            margin-top: 10px;
        }

        .full-width {
            grid-column: span 2;
        }
    </style>
    <script src="js/scripts.js"></script>
</head>
<body>
<div class="container">
    <!-- Курсы валют -->
    <div class="card">
        <h1>Обменные курсы</h1>
        <table>
            <tr>
                <th>Currency</th>
                <th>Exchange Rate</th>
                <th>Update</th>
            </tr>
            <c:forEach var="exRate" items="${exList}">
                <tr>
                    <td>${exRate.currencies}</td>
                    <td>${exRate.rate}</td>
                    <td>
                        <form onsubmit="updateRate(event, '${fn:trim(exRate.currencies)}')">
                            <input type="number" name="rate" step="0.0001" placeholder="Новый курс" required>
                            <button type="submit">Update</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>

    <!-- Колонка с формами -->
    <div class="forms">
        <!-- Добавление валюты -->
        <div class="card">
            <h1>Добавить валюту</h1>
            <form id="addCurrencyForm">
                <p>Код валюты <input name="code" type="text" placeholder="USD" required></p>
                <p>Название валюты <input name="fullName" type="text" placeholder="Dollar" required></p>
                <p>Символ валюты <input name="sign" type="text" placeholder="$" required></p>
                <p><button type="submit" class="btn">Добавить</button></p>
            </form>
        </div>

        <!-- Добавить валютную пару -->
        <div class="card">
            <form name="AddExForm" action="exchangeRates" method="post">
                <h1>Добавить валютную пару</h1>
                <p>Base currency <input list="currencies" name="baseCurrency" required></p>
                <p>Target currency <input list="currencies" name="targetCurrency" required></p>

                <datalist id="currencies">
                    <c:forEach var="currency" items="${currencies}">
                        <option value="${currency.code}">${currency.fullName}</option>
                    </c:forEach>
                </datalist>

                <p>Rate <input name="rate" type="number" step="0.0001" required></p>
                <p><input value="Добавить" type="submit"></p>
            </form>
        </div>

        <!-- Обмен валюты -->
        <div class="card">
            <form name="exchangeForm" action="exchange" method="get">
                <h1>Обмен валюты</h1>
                <p>Base currency <input list="currencies" name="from" value="${fromValue}" required></p>
                <p>Target currency <input list="currencies" name="to" value="${toValue}" required></p>
                <p>Amount <input name="amount" type="number" step="0.01" value="${amountValue}" required></p>
                <p><input value="Посчитать" type="submit"></p>

                <p>Результат:
                    <output id="result">
                        <c:if test="${not empty result}">${result}</c:if>
                    </output>
                </p>
            </form>
        </div>
    </div>

    <!-- Кнопка возврата -->
    <div class="card full-width" style="text-align:center;">
        <form action="currencies">
            <input value="⬅ Список валют" type="submit" class="btn">
        </form>
    </div>
</div>
</body>
</html>

