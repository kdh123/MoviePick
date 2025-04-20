# MOVIK
MOVIK(Movie Pick)은 영화 및 TV 프로그램 정보를 제공하는 애플리케이션입니다. Kotlin Multiplatform(KMP)을 기반으로 하며 Android, iOS, Desktop 플랫폼을 지원합니다.

## Android & iOS
<img width="225" alt="image" src="https://github.com/user-attachments/assets/05fa281c-ccfe-4fea-8cf0-560b68c9eb15">   <img width="225" alt="image" src="https://github.com/user-attachments/assets/06c8b0be-e3d2-406c-af7b-93b2b7b4dc64">  <img width="225" alt="image" src="https://github.com/user-attachments/assets/87fd2ee3-db6d-4cfc-a43e-eba3bf3c26d4">  <img width="225" alt="image" src="https://github.com/user-attachments/assets/cbdfc3ea-44f4-4085-896f-4b2a45aa13f4">

## Desktop
<img width="400" alt="image" src="https://github.com/user-attachments/assets/54f0093d-f177-41b5-89d9-1ae8056a26e7">  <img width="400" alt="image" src="https://github.com/user-attachments/assets/12dd4458-3d42-4e06-b5cf-bce8606e90f6">
<img width="400" alt="image" src="https://github.com/user-attachments/assets/ecc9e3ae-1b10-4aa7-8895-0b0d5bb20583">  <img width="400" alt="image" src="https://github.com/user-attachments/assets/f79ce70f-b42d-4c4c-b09a-80eb0a104d92">

## Non-dark Mode & Dark Mode
<img width="225" alt="image" src="https://github.com/user-attachments/assets/2b300996-0ae7-4797-850b-c731662ccbdc">   <img width="225" alt="image" src="https://github.com/user-attachments/assets/1a00d9f7-acbe-40ba-ade9-0171f3f693ab">

## Development environment
- IDE : Android Studio Koala | 2024.1.1 Patch 1 & Xcode 16.2
- JDK : 17
- Kotlin : 2.1.0
- AGP : 8.5.2

## Including in your project
- MOVIC은 [TMDB](https://www.themoviedb.org/) API를 사용합니다. TMDB로부터 발급 받은 Access Token을 다음 파일에 추가하여 Root 경로에 위치시켜 주세요.
- local.properties
```kotlin
TMDB_API_KEY="YOUR_ACCESS_TOKEN"
```

## Libraries
- Kotlin
  - Coroutine
  - Flow
  - Serialization
- UI
  - Compose Multiplatform
- Android Jetpack
  - ViewModel
  - Paging
  - Room
- Network
  - Ktor
- DI
  - Koin
- Image
  - [Landscapist](https://github.com/skydoves/landscapist)
- Media Player
  - [ComposeMultiplatformMediaPlayer](https://github.com/Chaintech-Network/ComposeMultiplatformMediaPlayer)
- Test
  - Junit
 
## Architecture
- Clean Architecture
- MVI
- Mutli Module

![Untitled](https://github.com/user-attachments/assets/a3e3bf58-d401-447c-bfcb-be283ce76612)

