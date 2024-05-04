# Melody Maven

## ❓Overview
Melody Maven is an Android application designed to enhance musical skills. 
It offers a comprehensive platform for users to practice, learn, and master various music concepts. 
The application is specifically tailored to provide support for Russian language and notaions, addressing a gap in the current market.

## 💡Motivation
The inspiration for Melody Maven arose from discussions with friends who expressed a need for an application that combines comprehensive functionality with tailored support for Russian-speaking users. 
This led to the development of an application that caters to both novice and experienced musicians seeking to refine their musical abilities.

## ⚡Features

- Customized Exercises: Users can easily tailor exercises to suit their individual learning goals and skill level;
- Russian Language Support: The application provides comprehensive support for Russian language and musical notations, making it accessible to a wider audience;
- Real Instrument Support: Users can connect their real instrument to the application via microphone, allowing them to practice and receive feedback on their playing;
- Progress Tracking: The application tracks user progress, and displays the results after completing the exercises;
- Friendly Interface: The application features a user-friendly interface designed to be accessible to users of all ages, including children and aged people.

## 🚀How to Install

### Using APK from GitHub Actions
1. Download the latest APK file from the [GitHub actions page](https://github.com/d-zaytsev/android-app/actions/runs)
2. Transfer the APK file to your Android device
3. Open the APK file on your device to install the application
### Building from Source
1. Clone repository: ```git clone https://github.com/d-zaytsev/android-app.git```
2. Build the project using Gradle: ```./gradlew assembleDebug```
3. Install the APK file on your device: ```adb install -r app/build/outputs/apk/debug/app-debug.apk```

## 🔧Tech Stack

- Android
- Kotlin
- JUnit 5
- Jetpack Compose
- TarsosDSP
- Android Studio
