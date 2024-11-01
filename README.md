# Music Recommendation App

## Overview

This project is a music recommendation application built with Java and Spring Boot. It allows users to manage their music library, 
create playlists, add friends, and receive personalized song recommendations based on playlist similarity. 
This application is designed to operate entirely in-memory, leveraging data structures to store information, with no external databases.

## Features

- **User Management**: Create and manage users within the application.
- **Music Library**: Add and store a collection of songs, allowing users to expand their music library.
- **Playlists**: Users can add songs to their playlists, helping the app generate personalized recommendations.
- **Music Recommendations**: Users receive song recommendations based on the songs in their playlists and similarity to other users' playlists.
- **Friends**: Users can add friends and view their friends' playlists, enhancing recommendation accuracy.

## Data Storage

This application uses only in-memory data structures (such as `HashMap`, `ArrayList`, etc.) to store all data related to users, 
songs, playlists, and friend connections. 
No external database (such as MySQL, PostgreSQL, etc.) is used, meaning all data exists only while the application is 
running and is lost when the application is stopped.

## Design Patterns Used

- **Singleton**
- **Dependency Injection**
- **Facade**
- **Model-View-Controller (MVC)**

## Technologies Used

- **Java 17**
- **Spring Boot**
- **Lombok**
- **JUnit**
