# Lost Bicycle Reporting App

## Project Overview

This Android application demonstrates a simulation of reporting and managing lost bicycles in Toronto's bike sharing program. Designed as a proof-of-concept, it utilizes local storage via Room database for simplicity and rapid prototyping. The app allows users to report bicycles left at random locations, with these locations generated randomly within Toronto's geographical bounds. City workers can monitor and manage these reports through an interactive map.

Key features include:

- Local data persistence using Room, showcasing offline-first architecture.
- Simulated bicycle reporting with randomly generated locations.
- Interactive map display of reported bicycles using Google Maps API.
- Efficient state management and UI updates with Kotlin Flows and MVVM architecture.

## Features

- **Interactive Map**: Real-time display of lost bicycles.
- **Bicycle Reporting**: Easy reporting of bicycles found at random locations.
- **Location Services**: Geocoding and address generation.
- **Dynamic Marker Management**: Add and remove bicycle markers based on status.

## Technologies Demonstrated

- **Android Development**
- **Kotlin**
- **MVVM Architecture**
- **Hilt Dependency Injection**
- **Coroutines**
- **Flow**
- **Room Database**
- **Google Maps Integration**

## Key Technical Highlights

- Reactive UI updates using StateFlow.
- Dependency injection with Hilt.
- Geocoding for address generation.
- Room database for local data persistence.
- Marker management on Google Maps.

## Architecture Components

- `MainActivity`: Manages the main UI and map interactions.
- `BicycleViewModel`: Handles business logic and data operations.
- `BicycleRepository`: Abstracts data sources and provides a clean API.
- `AppDatabase`: Manages Room database operations.
- `Bicycle`: Data model representing bicycle entities.

## Unique Implementation Details

- Random bicycle location generation within Toronto's geographical bounds.
- Dynamic marker updates based on bicycle status.

## Potential Improvements

- User authentication for city workers.
- Advanced filtering of bicycle locations.
- Detailed bicycle tracking history.

## Getting Started

2. Open in Android Studio.
3. Sync Gradle dependencies.
4. Provide your own Google Maps API key in the project configuration.
5. Run on emulator or physical device.

## License

This project is licensed under the MIT License - see the LICENSE.md file for details.
