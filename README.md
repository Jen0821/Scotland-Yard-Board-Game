# 🔎 Scotland Yard AI Board Game

[![GitHub Repository](https://img.shields.io/badge/GitHub-Scotland%20Yard%20AI-blue?logo=github)](https://github.com/Philjae-Shin/Scotland-Yard-AI-Board-Game)
[![Language](https://img.shields.io/badge/Language-Java%2017-E0A42E?logo=openjdk)](https://www.java.com/en/)
[![UI/UX](https://img.shields.io/badge/GUI-JavaFX-green?logo=javafx)](https://openjfx.io/)
[![License](https://img.shields.io/github/license/Philjae-Shin/Scotland-Yard-AI-Board-Game?color=B31B1B)](./LICENSE)

This repository contains the source code for an interactive digital version of the classic board game **Scotland Yard**. The core focus of this project is the implementation of a sophisticated **Artificial Intelligence (AI)** for both the fugitive, **Mr. X**, and the **Detectives**, leveraging advanced search algorithms.

---

## Overview: Scotland Yard Game

**Scotland Yard** is a classic detective board game set on a map of London, where players collaboratively try to capture a hidden fugitive, Mr. X.

* **Mr. X:** Moves secretly across the board, only revealing their position at specific turns (3, 8, 13, 18, 24). Mr. X's goal is to evade capture until the last turn.
* **Detectives:** Use deduction based on Mr. X's announced transport types (Taxi, Bus, Underground) to corner and arrest them. The detectives win by landing on Mr. X's current station.
* **Transportation:** Players use various tickets (Taxi, Bus, Underground, and Mr. X's Black Ticket) to navigate the board.

This digital version faithfully recreates the complex rules, enhanced with a modern UI and powerful AI agents.

---

## 🎯 Key Features & AI Strategy

The project's design is centered around the implementation of two major components and the extension of game play through AI.

### 1. Core Game Model (`Model` Implementation)
* **GUI with JavaFX:** Provides a modern, interactive user interface built with **JavaFX**, displaying the London map, movement paths, ticket status, and turn progression in real-time.
    > [Game Main Screen: The main screen of the Scotland Yard board game](https://github.com/Philjae-Shin/Scotland-Yard-AI-Board-Game/raw/main/doc/Screenshot%20from%202024-08-25%2013-33-50.png)
* **Game Logic:** Accurate implementation of all core Scotland Yard rules, including node (station) connectivity, player movement, ticket consumption, and Mr. X's mandatory location reveals.

### 2. Advanced AI Extension (`AI` Implementation)
The AI is designed to act as both a strategic Mr. X (Minimizing Capture) and a coordinated team of Detectives (Maximizing Capture).

* **MiniMax Algorithm:** The movement decisions for both Mr. X and the Detectives are determined using the **MiniMax search algorithm** to evaluate future game states.
* **Alpha-Beta Pruning:** To handle the extensive search space and ensure high performance, **Alpha-Beta Pruning** is integrated, significantly reducing the number of nodes evaluated in the game tree.
* **Strategic Planning:** The AI assesses all possible moves, with Mr. X prioritizing paths that minimize the probability of capture, and Detectives choosing moves that maximize their chances of encircling or landing on Mr. X.

---

## 🛠️ Technology Stack

| Category | Technology / Library | Usage |
| :--- | :--- | :--- |
| **Primary Language** | **Java 17** | Core application logic and AI implementation. |
| **GUI** | **JavaFX** | Cross-platform graphical user interface and game visualization. |
| **Data Structures** | **Guava Library** | Utilizes `Graph` and `ValueGraph` data structures for efficient representation of the board map connections. |
| **Development** | **IntelliJ IDEA** | Recommended primary development environment. |

---

## ▶️ Setup and Running

### Prerequisites

To run and develop this project, you must have the following software installed:

* **Java Development Kit (JDK) 17 or higher:** It is **important** to use **Java 17** across all operating systems.
* **JavaFX SDK:** The necessary JavaFX libraries.
* **IntelliJ IDEA:** The recommended IDE.

### Installation Guide

1.  **Java JDK 17+:** Download and install the latest version from the [official Java website](https://www.java.com/en/).
2.  **JavaFX SDK:** Obtain the JavaFX SDK from the [Gluon website](https://gluonhq.com/products/javafx/).

### Project Setup and Execution

1.  **Clone the Repository:**
    ```bash
    git clone [https://github.com/Philjae-Shin/Scotland-Yard-AI-Board-Game.git](https://github.com/Philjae-Shin/Scotland-Yard-AI-Board-Game.git)
    ```
    Open the cloned directory in IntelliJ IDEA (`File > Open`).

2.  **Configure JavaFX Libraries:**
    * In IntelliJ IDEA, go to `File > Project Structure > Libraries`.
    * Click `+` and select `Java`.
    * Add the contents of the **`lib` folder** from your downloaded JavaFX SDK directory.

3.  **Run the Project:**
    * Locate the main class file at:
        ```
        src/main/java/uk/ac/bris/cs/scotlandyard/Main.java
        ```
    * Open `Main.java` and click the **Run** button (green play icon) to execute the program.

---

## 🖥️ In-Game Screenshots

Visual examples demonstrating the project's interface and game state.

### Game In Progress
Shows the dynamic situation on the London map as detectives pursue the hidden Mr. X.
> [Game Map In The Game Progress: Game Map In The Game Progress](https://github.com/Philjae-Shin/Scotland-Yard-AI-Board-Game/raw/main/doc/Screenshot%20from%202024-08-25%2013-34-01.png)

### End Game Screen
Displays the final game result and state.
> [The Game Screen: End Game Screen](https://github.com/Philjae-Shin/Scotland-Yard-AI-Board-Game/raw/main/doc/Screenshot%20from%202024-08-25%2013-34-18.png)

---

## ⚠️ Troubleshooting

If you encounter GUI crashes or execution errors:

* **Verify Java Version:** Ensure your Project SDK is explicitly set to **Java 17**.
* **JavaFX Setup:** Double-check that all `.jar` files within the JavaFX SDK `lib` folder are correctly added as project libraries.
* **Apple Silicon Fix:** If running on a Mac with an M1/M2 chip and experiencing GUI crashes, consult the provided fix documentation which may require setting specific JVM options like `-Dprism.order=sw`.
    * [FIX FOR APPLE SILICON, when GUI crashes](/Philjae-Shin/Scotland-Yard-AI-Board-Game/blob/main/applefix.md)

---

## 📚 Reference Materials and Documentation

These resources are essential for understanding the codebase, the game model, and the AI implementation.

* **Project API Documentation:** Detailed review of the project's functions and classes.
    * [Scotland Yard Project API Documentation](https://seis.bristol.ac.uk/~sh1670/SY/apidocs2022/index.html)
* **Guava Library:** Documentation for the advanced data structures used.
    * [Guava Graphs Explained](https://github.com/google/guava/wiki/GraphsExplained#valuegraph)
    * [Guava Immutable Collections Explained](https://github.com/google/guava/wiki/ImmutableCollectionsExplained)
* **Official Game Rules:** Context for the rules implemented in the `Model`.
    * [Scotland Yard Official Rulebook](https://init-games.blogspot.com/2020/02/scotland-yard-1983.html)
    * [Scotland Yard (board game) on Wikipedia](https://en.wikipedia.org/wiki/Scotland_Yard_(board_game))

---

**Enjoy the game!**
