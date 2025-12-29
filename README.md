# Seekho - Anime Discovery App 🎌

A modern Android application for discovering and exploring anime content using the Jikan API (MyAnimeList unofficial API).

## 📱 Features

- Browse top-rated anime from MyAnimeList
- View detailed anime information (synopsis, ratings, episodes, genres)
- Watch anime trailers directly in the app
- Offline support with local database caching
- Clean, modern Material Design interface

## 🛠️ Tech Stack

- **Kotlin** - Programming language
- **MVVM Architecture** - Clean architecture pattern
- **Retrofit** - API calls
- **Room Database** - Local data storage
- **Glide** - Image loading
- **Material Design** - UI components

## 🚀 Setup Instructions

### Prerequisites
- Android Studio
- JDK 11 or higher
- Android SDK (API 33+)

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/seekho.git
   cd seekho
   ```

2. **Configure API Base URL**
   
   **IMPORTANT:** Add the following line to your `gradle.properties` file:
   ```properties
   BASE_URL=https://api.jikan.moe/v4/
   ```
   
   This is required for the app to work properly. The base URL is externalized for easy configuration across different environments.

3. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

4. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```
   Or click "Run" in Android Studio

## 📁 Project Structure

```
app/
├── src/main/java/com/anwin/seekho/
│   ├── data/                    # Data models
│   ├── service/
│   │   ├── local/db/           # Room database
│   │   ├── remote/api/         # Retrofit API
│   │   └── repository/         # Data repository
│   ├── ui/                     # UI screens
│   └── utils/                  # Utility classes
└── src/main/res/               # Resources
```

## 🔧 Configuration

### API Configuration
The app uses the Jikan API v4. Make sure to add the base URL in `gradle.properties`:

```properties
# API Configuration
BASE_URL=https://api.jikan.moe/v4/
```

### Network Security
The app includes network security configuration for HTTP/HTTPS traffic handling.

## 📊 Key Components

### Database
- **AnimeEntity**: Stores anime information locally
- **Room Database**: Provides offline access to anime data

### API Service
- **ApiService**: Defines API endpoints
- **ApiClient**: Configures Retrofit with base URL from BuildConfig

### Architecture
- **Repository Pattern**: Manages data from both local and remote sources
- **MVVM**: Separates UI logic from business logic
- **LiveData**: Reactive data observation

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add the required `BASE_URL` to gradle.properties
5. Test your changes
6. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 📞 Contact

**Developer**: Anwin  
**GitHub**: [@anwin-main](https://github.com/anwin-main)

---

**Note**: Don't forget to add `BASE_URL=https://api.jikan.moe/v4/` to your `gradle.properties` file before building the project.
