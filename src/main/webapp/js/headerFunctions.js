function navigateTo(page) {
    // Получаем текущую страницу
    const currentPage = window.location.pathname.split('/').pop();

    // Проверяем, переходим ли мы на другую страницу
    if (
        (page === 'home' && currentPage !== 'index.html' && currentPage !== '') ||
        (page === 'matches' && currentPage !== 'matches.html')
    ) {
        // Имитация удаления матча из списка идущих матчей
        if (page === 'home' || page === 'matches') {
            removeCurrentMatch();
        }

        // Переход на нужную страницу
        if (page === 'home') {
            window.location.href = 'index.html';
        } else if (page === 'matches') {
            window.location.href = 'matches.html';
        }
    }

    // Установка активного пункта меню
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
    });

    document.getElementById(`${page}-link`).classList.add('active');
}

// Функция для удаления текущего матча из списка идущих
function removeCurrentMatch() {
    // В реальном приложении здесь будет код для отправки запроса на сервер
    // Например:
    // fetch('/api/current-match', {
    //     method: 'DELETE',
    //     headers: {
    //         'Content-Type': 'application/json',
    //     }
    // })
    // .then(response => response.json())
    // .then(data => console.log('Матч удален:', data))
    // .catch(error => console.error('Ошибка при удалении матча:', error));

    console.log('Удаление текущего матча из списка идущих');
}

// Функция для отображения уведомления
function showAlert(message) {
    const alert = document.getElementById('alert');
    alert.textContent = message;
    alert.style.display = 'block';

    // Скрываем уведомление через 3 секунды
    setTimeout(() => {
        alert.style.display = 'none';
    }, 3000);
}

// Загрузка хедера в страницу
function loadHeader() {
    fetch('header.html')
        .then(response => response.text())
        .then(html => {
            document.getElementById('header-container').innerHTML = html;

            // Установка активного пункта меню после загрузки
            const currentPage = window.location.pathname.split('/').pop();

            if (currentPage === 'matches.html') {
                document.getElementById('matches-link').classList.add('active');
            } else {
                document.getElementById('home-link').classList.add('active');
            }
        })
        .catch(error => console.error('Ошибка загрузки хедера:', error));
}

// Запуск загрузки хедера при загрузке страницы
document.addEventListener('DOMContentLoaded', loadHeader);