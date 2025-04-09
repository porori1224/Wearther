document.addEventListener("DOMContentLoaded", function () {
    const cityInput = document.querySelector("#cityInput"); // ✅ 검색창 ID 적용
    const weatherCard = document.querySelector(".weather-card");
    const airQualityCard = document.querySelector(".air-quality-card");

    // ✅ 현재 위치 날씨 가져오기 (로딩 메시지 표시)
    function fetchCurrentLocationWeather() {
        weatherCard.innerHTML = "<p>📍 현재 위치 정보를 가져오는 중...</p>";

        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    const lat = position.coords.latitude;
                    const lon = position.coords.longitude;
                    fetchWeatherByCoords(lat, lon);
                },
                (error) => {
                    console.error("❌ 위치 정보를 가져올 수 없습니다.", error);
                    alert("위치 정보를 가져올 수 없습니다. 기본 위치(서울)로 설정됩니다.");
                    fetchWeatherByCity("서울");
                }
            );
        } else {
            alert("이 브라우저에서는 위치 서비스를 지원하지 않습니다.");
            fetchWeatherByCity("서울"); // 기본 위치 설정
        }
    }

    // ✅ 검색창에서 Enter 입력 시 API 요청
    cityInput.addEventListener("keypress", function (event) {
        if (event.key === "Enter") {
            fetchWeatherByCity(cityInput.value);
        }
    });

    // ✅ 도시명으로 날씨 정보 가져오기
    function fetchWeatherByCity(city) {
        if (!city.trim()) {
            alert("도시명을 입력해주세요.");
            return;
        }

        const encodedCity = encodeURIComponent(city);
        const url = `/api/weather?city=${encodedCity}`;

        fetch(url)
            .then(response => response.json())
            .then(data => {
                updateWeatherCard(data);
            })
            .catch(error => {
                console.error("❌ API 요청 오류:", error);
                alert("날씨 정보를 불러오는 중 오류가 발생했습니다.");
            });
    }

    // ✅ 위도·경도로 날씨 정보 가져오기
    function fetchWeatherByCoords(lat, lon) {
        const url = `/api/weather?lat=${lat}&lon=${lon}`;

        fetch(url)
            .then(response => response.json())
            .then(data => {
                updateWeatherCard(data);
            })
            .catch(error => {
                console.error("❌ API 요청 오류:", error);
                alert("날씨 정보를 불러오는 중 오류가 발생했습니다.");
            });
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
        const cityName = data.city ? data.city : "";
        const temperatureRaw = data.temperature || "0";
        const temperature = parseFloat(temperatureRaw); // 문자열에서 숫자 추출
        const weather = data.weather ? data.weather.replace(/\(|\)/g, "") : "정보 없음";
        const dust = parseInt(data.pm10Value) || 40;

        // ✅ 날씨 상태 아이콘 추가
        const weatherIcons = {
            "맑음": "☀️",
            "흐림": "☁️",
            "온흐림": "🌥️",
            "비": "🌧️",
            "눈": "❄️",
            "번개": "⛈️"
        };
        const weatherIcon = weatherIcons[weather] || "🌦️";

        // ✅ 미세먼지 아이콘 설정
        const airQualityIcons = {
            "좋음": "🟢",
            "보통": "🟡",
            "나쁨": "🟠",
            "매우 나쁨": "🔴",
            "위험": "🟣"
        };
        const airQualityIcon = airQualityIcons[data.airQuality] || "";

        // ✅ HTML 업데이트
        weatherCard.innerHTML = `
            <h3><strong>${locationType}</strong> ${cityName}</h3>
            <p>🌡️ <strong>현재 기온:</strong> ${temperature}</p>
            <p>${weatherIcon} <strong>날씨:</strong> ${weather}</p>
        `;

        airQualityCard.innerHTML = `
            <h3>미세먼지</h3>
            <p>${airQualityIcon} ${data.airQuality}</p>
            <p>${data.ventilation}</p>
        `;

        // ✅ 배경 클래스 초기화
        document.body.classList.remove("sunny", "rainy", "cloudy", "snowy");

        if (weather.includes("맑음")) {
            document.body.classList.add("sunny");
        } else if (weather.includes("비")) {
            document.body.classList.add("rainy");
        } else if (weather.includes("흐림") || weather.includes("온흐림")) {
            document.body.classList.add("cloudy");
        } else if (weather.includes("눈")) {
            document.body.classList.add("snowy");
        }

        fetchOutfitRecommendation(temperature, weather, dust);
    }

    // ✅ 옷차림 추천 함수 추가
    function fetchOutfitRecommendation(temperature, weather, dustLevel) {
        if (
            temperature === undefined || weather === undefined || dustLevel === undefined ||
            temperature === null || weather === null || dustLevel === null ||
            isNaN(temperature) || isNaN(dustLevel)
        ) {
            console.warn("❗ 추천 요청 생략: 유효하지 않은 입력값입니다.", { temperature, weather, dustLevel });
            return;
        }

        const url = `/recommend?temperature=${temperature}&weather=${encodeURIComponent(weather)}&dust=${dustLevel}`;
        fetch(url)
            .then(response => response.text())
            .then(html => {
                document.querySelector("#outfitRecommendation").innerHTML = html;
            })
            .catch(error => {
                console.error("❌ 옷차림 추천 오류:", error);
            });
    }

    // ✅ 페이지 로드 시 현재 위치 날씨 자동 가져오기
    fetchCurrentLocationWeather();
});