# BibleYearTrackerAndroid

An app for tracking your progress reading through the Bible in a year using the plan outlined by Meg Hunter Kilmer on her blog, [Held By His Pierced Hands](https://www.piercedhands.com/). The plan can be found in her article ["Reading the Bible through in a Year!"](https://www.piercedhands.com/reading-bible-year/).

![App Screenshots](https://github.com/dladukedev/BibleYearTrackerAndroid/assets/24884122/2d7931e6-4c11-4930-9e45-deb87f7809f5)


## Features
- Track your progress reading through the Bible
- Choose your start date and see how many days you need to read to catch up if you are behind
- Full support of dynamic theming with Material You, a Themed Icon, and Dark/Light themes
- No Internet Required! All data is handled locally
- Focus on Accessibility and meaningful Screen Reader Support

## Architecture
- Single Module with folders representing features which are then divided in a [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) style (data, domain, and display)
- Compose Views leverage the [State Holder Pattern](https://dladukedev.com/articles/006_stateholders/)
- Full testing suite including Unit, End-to-End UI Integration, and State Holder (headless UI) tests
- Unit tests leverage Fakes exclusively, no mocking library in use

## Notable Libraries
- Jetpack Compose
- Dagger/Hilt
- DataStore
- KotlinX Serialization
- Turbine
