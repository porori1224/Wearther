document.addEventListener("DOMContentLoaded", function () {
    const cityInput = document.querySelector("#cityInput"); // ✅ 수정된 검색창 ID 적용
    const weatherCard = document.querySelector(".weather-card");
    const airQualityCard = document.querySelector(".air-quality-card"); // ✅ 미세먼지 칸 추가

    cityInput.addEventListener("keypress", function (event) {
        if (event.key === "Enter") {
            fetchWeather(cityInput.value);
        }
    });

    function fetchWeather(city) {
        const encodedCity = encodeURIComponent(city);
        const url = `/api/weather?city=${encodedCity}`;

        fetch(url)
            .then(response => response.json())
            .then(data => {
                updateWeatherCard(data);
                console.log("✅ API 응답 데이터:", data);
            })
            .catch(error => console.error("❌ API 요청 오류:", error));
    }

    function updateWeatherCard(data) {
        if (data.error) {
            weatherCard.innerHTML = `<p>❌ 도시를 찾을 수 없습니다.</p>`;
            airQualityCard.innerHTML = `<p>❌ 미세먼지 정보를 가져올 수 없습니다.</p>`;
            return;
        }

        console.log("🌍 업데이트할 데이터:", data);

        weatherCard.innerHTML = `
            <i class="fas fa-cloud"></i>
            <h3>${data.city}</h3>
            <p>${data.temperature}°C ${data.weather}</p>
        `;

        airQualityCard.innerHTML = `
            <h3>미세먼지</h3>
            <p>${data.airQuality}</p>
        `;
    }
});
