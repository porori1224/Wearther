# ☀️ Wearther ❄️
기상 정보를 고려한 코디 추천 웹사이트

Wearther는 사용자의 현재 위치 정보를 기반으로 기온별 옷차림을 추천해주는 웹사이트입니다.  
또한, Pinterest의 시각적 자료등을 통해 패션 아이디어를 얻을 수 있도록 도와줍니다.


## 🛠️ 기술 스택
<div align="center">
<img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white" />
<img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white" />
<img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black" />
<img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" /> </br>
<img src="https://img.shields.io/badge/AWS-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white" />
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" />
<img src="https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white" />
<img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white" />
<img src="https://img.shields.io/badge/OpenWeather-FF6B00?style=for-the-badge&logo=openweathermap&logoColor=white" />
</div> </br>

- **Frontend**: 프레임워크 사용 안 함
- **Backend**: SpringBoot  
- **Database**: MySQL  
- **Cloud Services**: AWS  
- **Deployment Tools**: Docker, Docker Compose  
- **CI/CD**: GitHub Actions
- **Template Engie**: Thymeleaf
- **API**: Geolocation API, Fetch API, OpenWeatherMap API, KMA API, Pinterst API  


## ✨ 주요 기능

✔️ 현재 위치 기반 날씨 및 미세먼지 정보 제공  
✔️ 미세먼지 농도에 따른 환기 추천/비추천 정보 제공  
✔️ 현재 위치의 기상 상태를 고려한 코디 추천  
✔️ Pinterest 스타일 추천 연동  

## 📁 프로젝트 구조

```
Wearther/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/weather/
│       │       ├── App.java
│       │       ├── controller/
│       │       │   ├── ConfigController.java
│       │       │   ├── HomeController.java
│       │       │   └── WeatherController.java
│       │       └── service/       # (현재 비어 있음)
│       └── resources/
│           ├── static/
│           │   ├── css/
│           │   │   └── style.css
│           │   └── js/
│           │       └── script.js
│           ├── templates/
│           │   └── wearther.html
│           ├── application.properties
│           └── .env
├── .gitignore
├── pom.xml
└── README.md
```

- `controller/`: 사용자 요청 처리 컨트롤러 클래스들  
- `static/`: 정적 리소스(CSS, JS 등)  
- `templates/`: Thymeleaf 템플릿 HTML 파일  
- `application.properties`: 애플리케이션 설정  
- `.env`: 민감한 환경 변수 설정  
- `pom.xml`: Maven 프로젝트 설정 파일