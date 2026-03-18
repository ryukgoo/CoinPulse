# CoinPulse 🪙

A Kotlin Multiplatform cryptocurrency monitoring app built with Compose Multiplatform.

## Platforms

| Platform | Status |
|----------|--------|
| Android | ✅ |
| iOS | ✅ |
| Desktop (JVM) | ✅ |
| Web (WasmJS) | ✅ |

## Features

- 📊 Real-time cryptocurrency prices via CoinGecko API
- 📈 7-day price chart with gradient visualization
- ⭐ Favorites management with persistent storage
- 🔍 Real-time search
- 🌙 Light / Dark / System theme
- 💱 Multi-currency support (USD, KRW, EUR, JPY, BTC)
- 🔄 Auto-refresh with configurable intervals
- 💾 Platform-specific caching
- 📱 Responsive UI (Mobile / Tablet / Desktop)

## Tech Stack

### Core
- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)

### Networking
- [Ktor](https://ktor.io/) - HTTP client
- [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization) - JSON parsing

### DI
- [Koin](https://insert-koin.io/) - Dependency injection

### Storage
- [Multiplatform Settings](https://github.com/russhwolf/multiplatform-settings) - Key-value storage
- File-based cache for JVM

### Image Loading
- [Coil](https://coil-kt.github.io/coil/) - Async image loading

### Navigation
- [Navigation Compose](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-navigation-routing.html)

### Code Quality
- [Detekt](https://detekt.dev/) - Static analysis
- [Ktlint](https://ktlint.github.io/) - Code formatting

## Architecture

```
commonMain/
├── data/
│   ├── model/          # Coin, ChartData, PricePoint
│   ├── remote/         # CoinGeckoApi, CoinRemoteDataSource
│   ├── AppSettings.kt
│   ├── CoinCacheStorage.kt
│   ├── CoinRepositoryImpl.kt
│   └── FavoriteStorage.kt
├── domain/
│   ├── repository/     # CoinRepository
│   └── usecase/        # GetCoinsUseCase, GetMarketChartUseCase, ...
├── di/
│   └── AppModule.kt
└── presentation/
    ├── navigation/     # MobileNavigation, AdaptiveNavigation
    ├── ui/
    │   ├── component/  # SideNavBar
    │   ├── screen/     # CoinListScreen, CoinDetailScreen, ...
    │   └── theme/      # CoinPulseTheme
    └── viewmodel/      # CoinViewModel
```

## Responsive Layout

| Screen Width | Layout |
|-------------|--------|
| < 600dp | Mobile (BottomNavBar) |
| 600 ~ 960dp | Medium (SideNavBar + Master) |
| > 960dp | Expanded (SideNavBar + Master + Detail) |

## CI/CD

GitHub Actions를 통한 자동화된 CI 파이프라인:

- **Lint** - Detekt + Ktlint
- **Build Android** - Debug APK
- **Build Desktop** - JVM Jar
- **Build Web** - WasmJS Distribution
- **Build iOS** - iOS Framework (main branch only)
- **Unit Tests** - All platform tests

## Getting Started

### Requirements

- Android Studio Ladybug or later
- JDK 17
- Xcode 15+ (iOS only)

### Run

```bash
# Android
./gradlew :composeApp:assembleDebug

# Desktop
./gradlew :composeApp:run

# Web
./gradlew :composeApp:wasmJsBrowserDevelopmentRun

# iOS
# Open iosApp/iosApp.xcodeproj in Xcode
```

### Lint

```bash
# Check
./gradlew detekt ktlintCheck

# Auto fix
./gradlew detektAutoCorrect ktlintFormat
```

## API

This app uses the [CoinGecko API](https://www.coingecko.com/en/api) (free tier, no API key required).

| Endpoint | Usage |
|----------|-------|
| `/coins/markets` | Coin list with prices |
| `/coins/{id}/market_chart` | 7-day price history |

## License

```
MIT License

Copyright (c) 2024 CoinPulse

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
