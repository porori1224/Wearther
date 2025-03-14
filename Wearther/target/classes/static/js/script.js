document.addEventListener("DOMContentLoaded", function () {
    const cityInput = document.querySelector("header input");
    const weatherCard = document.querySelector(".weather-card");
    
    cityInput.addEventListener("keypress", function (event) {
        if (event.key === "Enter") {
            fetchWeather(cityInput.value);
        }
    });
    
    function fetchWeather(city) {
        const apiKey = "YOUR_API_KEY";
        const url = `https://api.openweathermap.org/data/2.5/weather?q=${city}&units=metric&appid=${apiKey}`;
        
        fetch(url)
            .then(response => response.json())
            .then(data => {
                updateWeatherCard(data);
            })
            .catch(error => console.error("Error fetching weather data:", error));
    }
    
    function updateWeatherCard(data) {
        if (data.cod === 200) {
            weatherCard.innerHTML = `
                <i class="fas fa-cloud"></i>
                <h3>${data.name}</h3>
                <p>${data.main.temp}°C ${data.weather[0].description}</p>
            `;
        } else {
            weatherCard.innerHTML = `<p>도시를 찾을 수 없습니다.</p>`;
        }
    }
});
