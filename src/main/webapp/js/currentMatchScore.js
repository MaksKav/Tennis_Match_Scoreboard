// Константы и конфигурация
const API_URL = '/Tennis_Match_Scoreboard/match-score';
const REFRESH_INTERVAL = 3000;

// DOM элементы
let player1Row;
let player2Row;
let uuid;

document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    uuid = urlParams.get('uuid');


    if (!uuid) {
        showError('Match ID not found in URL');
        return;
    }

    const playerRows = document.querySelectorAll('.player-row');
    player1Row = playerRows[0];
    player2Row = playerRows[1];

    setupEventHandlers();

    loadMatchData();

    setInterval(loadMatchData, REFRESH_INTERVAL);
});

function setupEventHandlers() {
    document.querySelectorAll('.score-button').forEach(button => {
        button.addEventListener('click', function (event) {
            event.preventDefault();
            const form = this.closest('form');
            const playerId = form.querySelector('input[name="playerId"]').value;
            addPoint(playerId);
        });
    });

    const endMatchButton = document.querySelector('.end-button');
    if (endMatchButton) {
        endMatchButton.addEventListener('click', function (event) {
            event.preventDefault();
            if (confirm('Are you sure you want to end this match?')) {
                endMatch();
            }
        });
    }
}

function loadMatchData() {
    fetch(`${API_URL}?uuid=${uuid}`, {
        method: 'GET'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            updateUI(data);
        })
        .catch(error => {
            console.error('Error loading match data:', error);
        });
}

function updateUI(matchData) {
    if (!matchData || !matchData.player1Name || !matchData.player2Name) {
        showError('Invalid match data received');
        return;
    }

    const player1 = {
        name: matchData.player1Name,
        gameScore: matchData.score.player1GameScore,
        gamesInSet: matchData.score.player1GamesInSet,
        sets: matchData.score.player1Sets
    };

    const player2 = {
        name: matchData.player2Name,
        gameScore: matchData.score.player2GameScore,
        gamesInSet: matchData.score.player2GamesInSet,
        sets: matchData.score.player2Sets
    };

    player1Row.querySelector('.player').textContent = player1.name;
    player2Row.querySelector('.player').textContent = player2.name;

    player1Row.querySelector('.sets').textContent = player1.sets;
    player2Row.querySelector('.sets').textContent = player2.sets;

    player1Row.querySelector('.games').textContent = player1.gamesInSet;
    player2Row.querySelector('.games').textContent = player2.gamesInSet;

    player1Row.querySelector('.points').textContent = formatPoints(player1.gameScore);
    player2Row.querySelector('.points').textContent = formatPoints(player2.gameScore);

    showTiebrakeMessage(matchData.score.isTiebreak);


    if (matchData.score.winner === 1 || matchData.score.winner === 2) {
        const winnerName = matchData.score.winner === 1 ? player1.name : player2.name;
        showMatchResult(winnerName);
        disableScoreButtons();
    }
}

function formatPoints(points) {
    return points;
}

function addPoint(playerId) {
    const data = {
        playerNumber: playerId,
        matchUuid: uuid
    };

    fetch(`${API_URL}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Cache-Control': 'no-cache, no-store, must-revalidate',
            'Pragma': 'no-cache',
            'Expires': '0'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            updateUI(data);
        })
        .catch(error => {
            console.error('Error adding point:', error);
            showError('Failed to add point. Please try again.');
        });
}

// Отправка запроса на завершение матча
function endMatch() {
    fetch(`${API_URL}/${uuid}/end`, {
        method: 'POST'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            window.location.href = '/matches'; // Перенаправление на страницу со списком матчей
        })
        .catch(error => {
            console.error('Error ending match:', error);
            showError('Failed to end match. Please try again.');
        });
}

// Показ сообщения об ошибке
function showError(message) {
    // Создаем элемент для отображения ошибки
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.textContent = message;

    // Добавляем к контейнеру
    const container = document.querySelector('.container');
    container.prepend(errorDiv);

    // Удаляем сообщение через 5 секунд
    setTimeout(() => {
        errorDiv.remove();
    }, 5000);
}

function showTiebrakeMessage(isTiebreak) {
    let existingTiebreakMessage = document.querySelector('.tiebreak-message');

    if (!isTiebreak) {
        if (existingTiebreakMessage) {
            existingTiebreakMessage.remove();
        }
        return;
    }

    if (existingTiebreakMessage) return;

    const tiebreakDiv = document.createElement('div');
    tiebreakDiv.className = 'tiebreak-message';
    tiebreakDiv.textContent = 'Tiebreak in progress!';

    const container = document.querySelector('.container');
    container.appendChild(tiebreakDiv);
}


function showMatchResult(winnerName) {
    let existingResult = document.querySelector('.match-result');
    if (existingResult) return;

    const resultDiv = document.createElement('div');
    resultDiv.className = 'match-result';
    resultDiv.textContent = `Match finished! ${winnerName} wins!`;

    const container = document.querySelector('.container');
    container.appendChild(resultDiv);
}

// Отключение кнопок начисления очков
function disableScoreButtons() {
    document.querySelectorAll('.score-button').forEach(button => {
        button.disabled = true;
        button.classList.add('disabled');
    });
}