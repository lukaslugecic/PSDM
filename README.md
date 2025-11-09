[PSDM_README_ROOT.md](https://github.com/user-attachments/files/23439199/PSDM_README_ROOT.md)
# 🧩 PSDM – Problem Solving and Decision-Making System

## 📖 Overview
**PSDM** is a software solution designed to support **collaborative problem solving and group decision-making**. It consists of a **mobile client** and a **REST API backend**, which together enable structured teamwork using well-known creative and analytical techniques.

The system supports several **problem-solving** and **decision-making** methods, allowing users to define problems, collaborate on generating ideas, evaluate solutions, and reach decisions.

## 🧱 Architecture
- **Mobile Client:** Kotlin + Jetpack Compose  
- **Web API:** Java + Spring Boot + JPA  
- **Authentication:** Keycloak (OAuth2 / OpenID Connect)  
- **Database:** PostgreSQL  
- **Architecture pattern:** Three-layer design (client, server, database)  
- **Communication:** REST over HTTP using JSON

## 🔗 Repositories
- 📱 [PSDMClientApp](https://github.com/lukaslugecic/PSDMClientApp)
- 🌐 [PSDMWebApi](https://github.com/lukaslugecic/PSDMWebApi)

## ⚙️ Key Features
- User registration and login through Keycloak  
- Real-time multi-user collaboration in sessions  
- Structured process of defining problems and generating ideas  
- Multiple **problem-solving techniques**:
  - Brainstorming  
  - Brainwriting  
  - Speedstorming  
  - Nominal Group Technique  
- Multiple **decision-making methods**:
  - Average Winner  
  - Weighted Average  
  - Borda Ranking  
  - Majority Rule  
- Persistent data storage with relational database model  
- Role-based access (Administrator, Moderator, Participant)

## 🧠 Core Concept
The system implements a meta-model that formalizes relationships between **problems**, **sessions**, **solutions**, **attributes**, and **votes**, allowing flexible application of diverse techniques within a unified framework.
