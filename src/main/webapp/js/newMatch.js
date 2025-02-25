document.getElementById("newMatchForm").addEventListener("submit", function (event) {
    event.preventDefault();  // Prevent the form from submitting the traditional way

    const player1 = document.getElementById("player1").value;
    const player2 = document.getElementById("player2").value;

    // Создаем строку параметров в формате x-www-form-urlencoded
    const formData = new URLSearchParams();
    formData.append('player1', player1);
    formData.append('player2', player2);

    // Отправляем данные с помощью fetch
    fetch('/Tennis_Match_Scoreboard/new-match', {
        method: 'POST', headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }, body: formData.toString()  // Преобразуем данные в строку для отправки
    })
        .then(response => {
            if (response.ok) {
                return response.json();  // Если ответ успешен, парсим JSON
            }
            throw new Error('Failed to create match');
        })
        .then(responseData => {
            // Если матч успешно создан
            document.getElementById('message').innerHTML = `<p>Match created successfully!</p>`;
            window.location.href = '/match-started';  // Переходим на страницу матча
        })
        .catch(error => {
            // Обрабатываем ошибку
            document.getElementById('message').innerHTML = `<p>Error: ${error.message}</p>`;
        });
});
