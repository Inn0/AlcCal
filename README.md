# AlcCal 🍺

Repository for the AlcCal project. AlcCal is a tracker app you can use to track alcohol consumption on a per-drink basis and gain insights about said consumption.

## Installation guide 📖
### To install the latest version 📲
1. Go to the [releases section](https://github.com/Inn0/AlcCal/releases) of the repository.
2. From the latest release, download the `.apk` file on your Android device.
3. When Android prompts you to install the app, click Install.
4. Et voilá!

### To create a new release 🛠️
#### Debug APK 🪲
1. Clone the main branch on your machine.
2. Open the project in [Android Studio](https://developer.android.com/studio).
3. Build the `.apk`. (Build > Build App Bundle(s) / APK(s) > Build APK(s))
4. Locate the `.apk` file, Android Studio should prompt you for the location.
5. Upload the `.apk` as a new release on GitHub with a new version tag.

#### Release APK 📱
1. Make sure you have the keystore file (`alccal-keystore.jks`) on your machine. (and you know the password)
2. Clone the main branch on your machine.
3. Open the project in [Android Studio](https://developer.android.com/studio).
4. Build the `.apk`. (Build > Generate Signed Bundle / APK > Select APK > Provide the keystore file and appropriate passwords (key is `key0`) > Click next > Select `release` > Create)
5. Locate the `.apk` file, Android Studio should prompt you for the location.
6. Upload the `.apk` as a new release on GitHub with a new version tag.

## Project tracking 🗓️
- [Kanban board](https://github.com/users/Inn0/projects/4/views/1)
- [Issues still to be implemented](https://github.com/Inn0/AlcCal/issues)

## Technologies used 🤖
- [Kotlin](https://kotlinlang.org/)
- [Jetpack Compose](https://developer.android.com/compose)
- [Room](https://developer.android.com/jetpack/androidx/releases/room)
- [Android Studio](https://developer.android.com/studio)

## Credit & contact 📞
- Project developed by [Daan Brocatus](mailto:daan.brocatus@outlook.com)
- Conceptual idea by [River Stassen](mailto:riverstassen@gmail.com)

Please reach out to [Daan Brocatus](mailto:daan.brocatus@outlook.com) for bug reports etc. 
