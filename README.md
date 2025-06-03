📘 Quiz Application – Java + MySQL
A desktop-based Quiz Application built using Java (Swing) for the front-end and MySQL for the backend. The app includes user registration/login, level-based questions, scoring, lifelines, and a scoreboard.

✨ Features
🧑‍💻 User Login & Registration

🎯 Three Difficulty Levels – Easy, Medium, Hard

📶 Sub-level progression (Level 1 to Level 3 per difficulty)

💾 MySQL database for storing users and scores

🏆 Scoreboard for top performers

⏳ Timer per question (optional)

🎁 Lifelines – e.g., Skip Question

🛠 Technologies Used
Tech	Purpose
Java (Swing)	GUI Interface
MySQL	Data Storage
JDBC	Java–MySQL Connector
VS Code	Development Environment

📂 Project Structure
css
Copy
Edit
quiz-app/
├── src/
│   ├── Main.java
│   ├── LoginFrame.java
│   ├── QuizFrame.java
│   ├── ScoreboardFrame.java
│   └── DatabaseConnection.java
├── README.md
├── .gitignore
└── LICENSE
⚙️ How to Run
📦 Install MySQL and create a database:

sql
Copy
Edit
CREATE DATABASE quizapp;
📋 Create required tables: users, scores, questions.

🔌 Set your DB credentials in DatabaseConnection.java.

💻 Compile and run the app using:

bash
Copy
Edit
javac *.java
java Main
📌 Database Schema (Example)
users table:
sql
Copy
Edit
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    password VARCHAR(50)
);
scores table:
sql
Copy
Edit
CREATE TABLE scores (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    score INT
);


📜 License
This project is licensed under the MIT License.

🙋‍♂️ Author
Vishnu Prasath.S
📧 prasathv347@gmail.com

🌐 GitHub Repo
🔗 View on GitHub
