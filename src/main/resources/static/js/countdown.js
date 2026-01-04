function initCountdown(timestamp) {
    let countDownDate = timestamp;
    let x = setInterval(function() {
        let now = new Date().getTime();
        let distance = countDownDate - now;
        let days = Math.floor(distance / (1000*60*60*24));
        let hours = Math.floor(distance % (1000*60*60*24) / (1000*60*60));
        let minutes = Math.floor(distance % (1000*60*60) / (1000*60));
        let seconds = Math.floor(distance % (1000*60) / 1000);

        document.querySelector("#countdown").innerHTML = `
            <div class="tag">
                <span class="value">${days}</span>
                <span class="label">jours</span>
            </div>
            <div class="tag">
                <span class="value">${hours}</span>
                <span class="label">heures</span>
            </div>
            <div class="tag">
                <span class="value">${minutes}</span>
                <span class="label">minutes</span>
            </div>
            <div class="tag">
                <span class="value">${seconds}</span>
                <span class="label">secondes</span>
            </div>
        `;

        if (distance < 0) {
            clearInterval(x);
            document.querySelector("#countdown").innerHTML = "<span>Terminée</span>";
        }
    }, 1000);
}