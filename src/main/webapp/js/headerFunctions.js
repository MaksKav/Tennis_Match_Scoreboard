function navigateTo(page) {
    const currentPage = window.location.pathname.split('/').pop();

    if (
        (page === 'home' && currentPage !== 'index.html' && currentPage !== '') ||
        (page === 'matches' && currentPage !== 'matches.html')
    ) {

        if (page === 'home') {
            window.location.href = 'index.html';
        } else if (page === 'matches') {
            window.location.href = 'matches.html';
        }
    }

    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
    });

    document.getElementById(`${page}-link`).classList.add('active');
}

function showAlert(message) {
    const alert = document.getElementById('alert');
    alert.textContent = message;
    alert.style.display = 'block';

    setTimeout(() => {
        alert.style.display = 'none';
    }, 3000);
}

function loadHeader() {
    fetch('header.html')
        .then(response => response.text())
        .then(html => {
            document.getElementById('header-container').innerHTML = html;

            const currentPage = window.location.pathname.split('/').pop();

            if (currentPage === 'matches.html') {
                document.getElementById('matches-link').classList.add('active');
            } else {
                document.getElementById('home-link').classList.add('active');
            }
        })
        .catch(error => console.error('Ошибка загрузки хедера:', error));
}

document.addEventListener('DOMContentLoaded', loadHeader);