// ---------------------------
// Удаление валюты
// ---------------------------
function deleteCurrency(id) {
    fetch(`currencies?id=${id}`, {
        method: "DELETE"
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Ошибка при удалении валюты");
            }
            window.location.reload();
        })
        .catch(error => console.error("Ошибка:", error));
}

// ---------------------------
// Обновление курса валюты
// ---------------------------
function updateRate(event, currencyCode) {
    event.preventDefault();

    const rate = event.target.querySelector('input[name="rate"]').value;
    const encodedCurrency = encodeURIComponent(currencyCode.trim());

    fetch(`exchangeRate/${encodedCurrency}`, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: new URLSearchParams({
            rate: rate,
            currencies: currencyCode
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Ошибка при обновлении курса");
            }
            event.target.reset(); // очищаем форму
            window.location.reload();
        })
        .catch(error => alert("Ошибка сети: " + error.message));
}

// ---------------------------
// Удаление обменного курса
// ---------------------------
function deleteExchangeRate(id) {
    fetch(`exchangeRates?id=${id}`, {
        method: "DELETE"
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Ошибка при удалении обменного курса");
            }
            window.location.reload();
        })
        .catch(error => console.error("Ошибка:", error));
}

// ---------------------------
// Добавление валюты (AJAX + JSON)
// ---------------------------
document.addEventListener("DOMContentLoaded", () => {
    const addCurrencyForm = document.getElementById("addCurrencyForm");

    if (!addCurrencyForm) return;

    addCurrencyForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const form = e.target;
        const data = {
            code: form.code.value.trim(),
            fullName: form.fullName.value.trim(),
            sign: form.sign.value.trim()
        };

        try {
            const response = await fetch("currencies", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data)
            });

            if (!response.ok) {
                throw new Error("Ошибка при добавлении валюты");
            }

            const result = await response.json();

            // Добавляем валюту в таблицу без перезагрузки
            const table = document.querySelector(".curList table");
            if (table) {
                const row = table.insertRow(-1);
                row.innerHTML = `
                    <td>${result.code}</td>
                    <td>${result.fullName}</td>
                    <td>${result.sign}</td>
                    <td>
                        <button type="button" onClick="deleteCurrency(${result.id})">Удалить</button>
                    </td>
                `;
            }

            form.reset();
        } catch (error) {
            console.error("Ошибка:", error);
            alert("Не удалось добавить валюту 😢");
        }
    });
});
