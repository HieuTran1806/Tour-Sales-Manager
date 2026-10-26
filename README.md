# Tour Sales Manager

# Introduction 
TSM is a Java application that allows customers and staff members to interact directly to book tours and manage tour transactions. Tours can be edited flexibly by staff members with the appropriate permissions. 

# Technologies 
Programming language: Java 
User interface: Java Swing
Database: MySQL 


# Project structure
java/
...

# Installing and Setup 
git clone <repository-url>
cd <repository-folder>

## Database configuration

Copy `src/main/resources/config.properties.example` to
`src/main/resources/config.properties`, then set the MySQL username and password
for your local database. The real `config.properties` is ignored by Git; commit
changes to the example file only when the required configuration keys change,
and never put a real password in the example.

PowerShell:

```powershell
Copy-Item src\main\resources\config.properties.example src\main\resources\config.properties
```

# Developer
Trần Viết Hiếu 

# Planned Improvements 

# Git Workflow  
git branch --show-current
git status

git add . / git add |files|
git commit -m "comment(what I done in these files)"
git push origin main/your-feture-name

# Design 

# Demo features 
1. 
2. 
3. 

# Contact 
Email: trhi4100@gmail.com
