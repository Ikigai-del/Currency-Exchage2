<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Обмен валют</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: linear-gradient(135deg, #4facfe, #00f2fe);
            margin: 0;
            padding: 0;
            display: flex;
            height: 100vh;
            justify-content: center;
            align-items: center;
        }

        .container {
            background: white;
            padding: 30px 40px;
            border-radius: 20px;
            box-shadow: 0 8px 20px rgba(0,0,0,0.2);
            text-align: center;
        }

        h1 {
            margin-bottom: 25px;
            color: #333;
        }

        form {
            margin: 10px 0;
        }

        input[type="submit"] {
            background: #4facfe;
            color: white;
            border: none;
            padding: 12px 25px;
            border-radius: 10px;
            font-size: 16px;
            cursor: pointer;
            transition: 0.3s;
        }

        input[type="submit"]:hover {
            background: #00c6fb;
            transform: scale(1.05);
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Добро пожаловать в CurrencyApp</h1>
    <form action="currencies">
        <input value="Список валют" type="submit">
    </form>
    <form action="exchangeRates">
        <input value="Обменные курсы" type="submit">
    </form>
</div>
</body>
</html>
