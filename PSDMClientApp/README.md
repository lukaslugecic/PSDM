# 📱 PSDMClientApp – Android Mobile Application

## 🔍 Overview
The **PSDM Client App** is an Android mobile application that enables users to participate in collaborative problem-solving and decision-making sessions. Built with **Kotlin** and **Jetpack Compose**, it provides a modern, reactive, and intuitive user experience.

## 🧩 Technologies Used
- **Language:** Kotlin  
- **Framework:** Jetpack Compose  
- **Architecture:** MVVM (Model-View-ViewModel)  
- **Dependency Injection:** Hilt  
- **Networking:** Retrofit + Coroutines  
- **Authentication:** Keycloak via OAuth2  
- **Data Serialization:** JSON (Gson/Moshi)

## 📋 Main Functionalities
- 🔐 **User Authentication** through Keycloak  
- 🧑‍💻 **Problem Definition:** create and describe new problems with title, description, and attributes  
- 🤝 **Session Management:** create and join collaborative sessions  
- 💡 **Idea Generation:** input and view ideas based on chosen method  
- 🧮 **Voting and Evaluation:** perform evaluations and rankings of proposed solutions  
- 🗂️ **Session History:** browse problems, sessions, and past decisions  

## 🧠 Supported Methods
### Problem-Solving
- Brainstorming (collaborative idea generation)  
- Brainwriting (sequential written idea sharing)  
- Speedstorming (pair-based collaboration)  
- Nominal Group Technique (individual + group refinement)

### Decision-Making
- Average Winner  
- Weighted Average  
- Borda Ranking  
- Majority Rule  

## 🔗 Related Repository
➡️ **Backend API:** [PSDMWebApi](https://github.com/lukaslugecic/PSDMWebApi)

## ⚙️ Installation
```bash
# Clone repository
git clone https://github.com/lukaslugecic/PSDMClientApp.git

# Open in Android Studio
# Build and run on an Android device or emulator
```

## 🔒 Authentication
The app connects to the Keycloak identity server.  
Users must log in using Keycloak credentials before accessing functionalities.
