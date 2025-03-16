document.addEventListener("DOMContentLoaded", function () {
    const cityInput = document.querySelector("#cityInput"); // ✅ 수정된 검색창 ID 적용
    const weatherCard = document.querySelector(".weather-card");
    const airQualityCard = document.querySelector(".air-quality-card"); // ✅ 미세먼지 칸 추가

    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            (position) => {
                const lat = position.coords.latitude;
                const lon = position.coords.longitude;
                fetchWeatherByCoords(lat, lon);
            },
            (error) => {
                console.error("❌ 위치 정보를 가져올 수 없습니다.", error);
                alert("위치 정보를 가져올 수 없습니다. 도시를 직접 입력해주세요.");
            }
        );
    } else {
        alert("이 브라우저에서는 위치 서비스를 지원하지 않습니다.");
    }

    cityInput.addEventListener("keypress", function (event) {
        if (event.key === "Enter") {
            fetchWeatherByCity(cityInput.value);
        }
    });

    // ✅ 도시명으로 날씨 정보 가져오기
    function fetchWeatherByCity(city) {
        const encodedCity = encodeURIComponent(city);
        const url = `/api/weather?city=${encodedCity}`;

        fetch(url)
            .then(response => response.json())
            .then(data => updateWeatherCard(data))
            .catch(error => console.error("❌ API 요청 오류:", error));
    }

    // ✅ 위도·경도로 날씨 정보 가져오기
    function fetchWeatherByCoords(lat, lon) {
        const url = `/api/weather?lat=${lat}&lon=${lon}`;

        fetch(url)
            .then(response => response.json())
            .then(data => updateWeatherCard(data))
            .catch(error => console.error("❌ API 요청 오류:", error));
    }

    // ✅ UI 업데이트 (날씨 & 미세먼지)
    function updateWeatherCard(data) {
        if (data.error) {
            weatherCard.innerHTML = `<p>❌ ${data.error}</p>`;
            airQualityCard.innerHTML = `<p>❌ 미세먼지 정보를 가져올 수 없습니다.</p>`;
            return;
        }


        console.log("🌍 업데이트할 데이터:", data);

        // ✅ undefined 방지: 도시 정보가 없을 경우 기본 값 처리
        const locationType = data.locationType ? data.locationType : "위치 정보 없음";
        const cityName = data.city ? data.city : "알 수 없음";
        const temperature = data.temperature ? data.temperature : "정보 없음";
        const weather = data.weather ? data.weather.replace(/\(|\)/g, "") : "정보 없음"; // 괄호 제거

        weatherCard.innerHTML = `
            <i class="fas fa-cloud"></i>
            <h3><strong>${locationType}</strong>: ${cityName}</h3>
            <p>🌡️ <strong>현재 기온:</strong> ${temperature}</p>
            <p>🌥️ <strong>날씨:</strong> ${weather}</p>
        `;

        airQualityCard.innerHTML = `
            <h3>미세먼지</h3>
            <p>${data.airQuality}</p>
            <p>${data.ventilation}</p>
        `;
    }
});
