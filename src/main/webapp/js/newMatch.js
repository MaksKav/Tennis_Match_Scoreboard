document.getElementById("newMatchForm").addEventListener("submit", function (event) {
    event.preventDefault();

    const player1 = document.getElementById("player1").value;
    const player2 = document.getElementById("player2").value;

    const formData = new URLSearchParams();
    formData.append('player1', player1);
    formData.append('player2', player2);

    fetch('/Tennis_Match_Scoreboard/api/new-match', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: formData.toString()
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(errorText => {
                    const cleanedError = errorText.replace(/\"/g, '');
                    throw new Error(cleanedError);
                });
            }
            return response.json();
        })
        .then(responseData => {
            const matchId = responseData.matchId;
            window.location.href=`/Tennis_Match_Scoreboard/match-score.html?uuid=${matchId}`;
        })
        .catch(error => {
            showError(error.message);
        });
});

function showError(message) {
    const errorDiv = document.getElementById("error-message");
    errorDiv.innerHTML = `<p class="error">${message}</p>`;
    errorDiv.style.display = "block";
}

