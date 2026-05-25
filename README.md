# 🖨️ PrintXpress Mobile Application

## 📌 Overview
PrintXpress is a native Android mobile application developed for customized printing services. It allows users to browse printing products, upload their own designs, and place orders easily from their mobile devices.

The system is built using Java, XML, and Firebase, following MVVM architecture for better scalability and maintainability.

---

## 🚀 Features

- User Registration and Login (Firebase Authentication)
- Browse printing services (business cards, flyers, banners, mugs, etc.)
- Upload custom design images
- Place and track orders in real time
- Manage user profile and delivery addresses
- Save and reuse previous designs
- FAQ and support system

---

## 🛠️ Technologies Used

- Java (Android Development)
- XML (UI Design)
- Firebase Authentication
- Firebase Realtime Database
- Android Studio

---

## 🏗️ Architecture

- MVVM (Model–View–ViewModel)
- Repository Pattern
- Validation Layer for input checking
- Clean separation of UI, logic, and data

---

## 📂 Project Structure

- Activities → UI screens (Login, Home, Orders, Profile)
- Fragments → Reusable UI components
- Models → Data classes (User, Order, Design)
- Repository → Firebase operations
- Utils → Validation and helper functions

---

## 🔐 Input Validation Rules

- Email must be valid format
- Password must be at least 6 characters
- Phone number must be exactly 10 digits
- Quantity must be greater than 0
- Empty fields are not allowed

---

## 📦 Installation

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle dependencies
4. Connect Firebase project
5. Run on emulator or physical device

---

## 🧪 Testing

The application was tested using multiple test cases:

- Valid and invalid login attempts
- Order placement validation
- Firebase database integration
- Navigation between screens
- Input validation checks

All test cases passed successfully.

---

## 📱 Modules

- Authentication Module
- Product Browsing Module
- Design Upload Module
- Order Management Module
- Profile Management Module
- FAQ & Support Module

---

## 👨‍💻 Developer

PrintXpress Mobile Application  
Android + Firebase Project

---

## 📌 Note

This project is developed for academic purposes and demonstrates a full mobile application built using native Android and Firebase.
