# MOVIK
MOVIK(Movie Pick)은 영화 및 TV 프로그램 정보를 제공하는 애플리케이션입니다. Kotlin Multiplatform(KMP)을 기반으로 하며 Android, iOS, Desktop 플랫폼을 지원합니다.

## Android & iOS
<img width="225" height="460" alt="image" src="https://github.com/user-attachments/assets/05fa281c-ccfe-4fea-8cf0-560b68c9eb15">   <img width="225" height="460" alt="image" src="https://github.com/user-attachments/assets/b36f7bff-b8e9-4b9b-ae00-6a29c8dfc01d">  <img width="225" height="460" alt="image" src="https://github.com/user-attachments/assets/1eae2457-b999-4934-8442-f24179cac3cd">  <img width="225" height="460" alt="image" src="https://github.com/user-attachments/assets/06c8b0be-e3d2-406c-af7b-93b2b7b4dc64">  <img width="225" height="460" alt="image" src="https://github.com/user-attachments/assets/12f300cf-45bb-4df2-afab-9e7d88f9544d">  <img width="225" height="460" alt="image" src="https://github.com/user-attachments/assets/cbdfc3ea-44f4-4085-896f-4b2a45aa13f4"> 



## Desktop
<img width="400" alt="image" src="https://github.com/user-attachments/assets/edf46877-4699-4d3d-b7f0-b57e756f2d29">  <img width="400" alt="image" src="https://github.com/user-attachments/assets/39457554-2fca-4265-b5fd-f9ab4ec86cc2">  <img width="400" alt="image" src="https://github.com/user-attachments/assets/571130d1-d7bb-4327-ba4b-99948300c796">
<img width="400" alt="image" src="https://github.com/user-attachments/assets/96bd8b4a-579a-4531-a4f3-73c3029b48c4">  <img width="400" alt="image" src="https://github.com/user-attachments/assets/a01b0e7f-fd83-4e48-b479-2187611384e1">  <img width="400" alt="image" src="https://github.com/user-attachments/assets/0d3d8470-cd06-4020-a72c-fb2de6399700"> 


## Non-dark Mode & Dark Mode
<img width="225" height="460" alt="image" src="https://github.com/user-attachments/assets/2b300996-0ae7-4797-850b-c731662ccbdc">   <img width="225" height="460" alt="image" src="https://github.com/user-attachments/assets/1a00d9f7-acbe-40ba-ade9-0171f3f693ab">

## Development environment
- IDE : Android Studio Narwhal 3 Feature Drop & Xcode 16.2
- JDK : 17
- Kotlin : 2.1.0
- AGP : 8.5.2

## Including in your project
- MOVIK은 [TMDB](https://www.themoviedb.org/) API를 사용합니다. TMDB로부터 발급 받은 Access Token을 다음 파일에 추가하여 Root 경로에 위치시켜 주세요.
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
- Pallete
  - [Kmppalette](https://github.com/jordond/kmpalette)
- Test
  - Junit
 
## Architecture
- Clean Architecture
- MVI
- Mutli Module

![Untitled](https://github.com/user-attachments/assets/a3e3bf58-d401-447c-bfcb-be283ce76612)
