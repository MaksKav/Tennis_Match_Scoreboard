const API_URL = '/Tennis_Match_Scoreboard/api/matches';
const PAGE_SIZE = 10;
let currentPage = 1;

const filterInput = document.querySelector('.filter-input');
const resetButton = document.querySelector('.reset-button');
const paginationContainer = document.querySelector('.pagination');
const matchesContainer = document.querySelector('.matches-table tbody');
const matchesBody = document.querySelector('#matches-body');

function fetchMatches(playerName = null) {
    const params = new URLSearchParams();

    params.append('page', currentPage);
    params.append('size', PAGE_SIZE);

    if (playerName) {
        params.append('filter_by_player_name', playerName);
    }
    const url = `${API_URL}?${params.toString()}`;
    return fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('error');
            }
            return response.json();
        })
        .then(data => {
            return {
                data: data.matches,
                total: data.totalMatches
            };
        })
        .catch(error => {
            console.error('Error', error);
            return { data: [], total: 0 };
        });
}


    function renderMatches(matches) {
    matchesContainer.innerHTML = '';
        matches.forEach(match => {
            const row = document.createElement('tr');
            row.innerHTML = `
            <td class="player-row">${match.player1Name}</td>
            <td class="player-row">${match.player2Name}</td>
            <td class ="player-row"><span class="winner-cell">${match.winnerName}</span></td>
        `;
            matchesBody.appendChild(row);
        });
}


function renderPagination(totalMatches) {
    const totalPages = Math.ceil(totalMatches / PAGE_SIZE);
    paginationContainer.innerHTML = '';

    if (totalPages <= 1) return;


    if (currentPage > 1) {
        const prevButton = document.createElement('button');
        prevButton.className = 'pagination-button';
        prevButton.innerText = '←';
        prevButton.addEventListener('click', () => {
            currentPage--;
            loadMatches();
        });
        paginationContainer.appendChild(prevButton);
    }


    for (let i = 1; i <= totalPages; i++) {
        const pageButton = document.createElement('button');
        pageButton.className = `pagination-button ${i === currentPage ? 'active' : ''}`;
        pageButton.innerText = i;
        pageButton.addEventListener('click', () => {
            currentPage = i;
            loadMatches();
        });
        paginationContainer.appendChild(pageButton);
    }

    if (currentPage < totalPages) {
        const nextButton = document.createElement('button');
        nextButton.className = 'pagination-button';
        nextButton.innerText = '→';
        nextButton.addEventListener('click', () => {
            currentPage++;
            loadMatches();
        });
        paginationContainer.appendChild(nextButton);
    }
}


function loadMatches() {
    const playerName = filterInput.value.trim();
    fetchMatches(playerName)
        .then(({ data, total }) => {
            renderMatches(data);
            renderPagination(total);
        });
}


filterInput.addEventListener('input', () => {
    currentPage = 1;
    loadMatches();
});

resetButton.addEventListener('click', () => {
    filterInput.value = '';
    currentPage = 1;
    loadMatches();
});

loadMatches();
