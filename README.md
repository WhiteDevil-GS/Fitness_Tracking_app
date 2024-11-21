# Fitness Tracking App 🏃‍♂️🏋️‍♀️

Welcome to the **Fitness Tracking App**! This Android app helps users monitor their daily physical activities like steps taken, calories burned, and distance covered. It provides an easy-to-use interface to set and track fitness goals, motivating users to stay active every day.

## 🌟 Features

- **Real-Time Step Tracking**: Leverages Android's built-in step counter sensor to track daily steps.
- **Goal Setting**: Allows users to set daily step objectives and monitors progress visually.
- **Calories & Distance Tracking**: Calculates estimated calories burned and distance covered based on step count.
- **Progress Indicators**: Provides a progress bar to visually represent step goals.
- **Daily Reset**: Automatically resets step count at the start of a new day.
- **Customizable Goals**: Users can update their step goals through an intuitive interface.

## 📸 Screenshots
_Add screenshots of your app's UI here._

## 🚀 Getting Started

### Prerequisites
- Android Studio installed on your computer.
- A device or emulator with a step counter sensor.

### Installation
1. Clone this repository:
   ```bash
   git clone https://github.com/WhiteDevil-GS/Fitness_Tracking_app.git
   ```
2. Open the project in Android Studio.
3. Build and run the app on your device or emulator.

### Permissions
The app may require the following permissions:
- **Activity Recognition**: To track steps accurately.

## 🛠️ Architecture
The app follows **MVVM (Model-View-ViewModel)** architecture for clean separation of concerns and better scalability.

- **Model**: Manages data and business logic (e.g., `DailyFitnessData`).
- **ViewModel**: Connects the Model to the UI (e.g., `FitnessViewModel`).
- **View**: Handles user interface and interactions (e.g., `Home`, `Setting` fragments).

## 📂 File Structure
```
├── model/
│   └── DailyFitnessData.kt
├── ui/
│   ├── Home.kt
│   ├── Setting.kt
│   └── fragments/
├── viewmodel/
│   └── FitnessViewModel.kt
└── res/
    ├── layout/
    ├── values/
    └── drawable/
```

## 💡 How It Works
1. **Step Counting**: The app uses the device's `SensorManager` to fetch real-time step data.
2. **Data Persistence**: Step data and user preferences are stored using `SharedPreferences`.
3. **UI Updates**: LiveData and ViewModel ensure that the UI updates automatically as the data changes.

## 🤝 Contributing
Contributions are welcome! Feel free to fork the repo and submit a pull request with improvements or bug fixes.

### Steps to Contribute
1. Fork the repository.
2. Create a new branch:
   ```bash
   git checkout -b feature-name
   ```
3. Make changes and commit:
   ```bash
   git commit -m "Add feature description"
   ```
4. Push to your branch:
   ```bash
   git push origin feature-name
   ```
5. Open a pull request.

## 📜 License
This project is licensed under the MIT License. See the `LICENSE` file for details.

## 🙌 Acknowledgements
- Android Developers Documentation
- Stack Overflow community for troubleshooting help.

## 📬 Contact
For any questions or suggestions, feel free to reach out via [GitHub](https://github.com/WhiteDevil-GS).
