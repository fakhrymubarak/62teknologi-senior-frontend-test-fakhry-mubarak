<h1 align="center">Business Apps</h1>

## Features

- Show Default List Business
- Search Business by String
- Pagination List Business
- Filter List Business
- Filter List Business with Nearby Location

- Show Detail Business
- Slideshow Business Photos
- Show List Business Reviews
- Open Business Location on Google Maps

## Tech stack & Open-source libraries

- Minimum Android SDK level 21
- [Kotlin](https://kotlinlang.org/)
  based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
    + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/)
      for asynchronous.
- Architecture
    - MVVM Design Pattern
    - Repository Pattern Implementation
    - Android Architecture Components (Data - Domain - Presentation
- Jetpack
    - [Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle): Observe
      Android lifecycles and handle UI states upon the lifecycle changes.
    - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel): Manages
      UI-related data holder and lifecycle aware. Allows data to survive
      configuration changes such as screen rotations.
    - [ViewBinding](https://developer.android.com/topic/libraries/view-binding): Binds UI components
      in your layouts to data sources in your app using a declarative format rather than
      programmatically.
      database access.
    - [Dagger-Hilt](https://dagger.dev/hilt/): for dependency injection.)
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit): Construct the REST APIs, and interface
  to handle network payload for Android.
- [Timber](https://github.com/JakeWharton/timber): A logger with a small, extensible API.

## Architecture

**Business Apps** is based on the MVVM architecture and the Repository pattern, which follows
the [Google's official architecture guidance](https://developer.android.com/topic/architecture).

The overall architecture of **Business Apps** is composed of three layers; the UI layers,
domain layers, and the data layers.
Each layer has dedicated components and they have each different responsibilities.
