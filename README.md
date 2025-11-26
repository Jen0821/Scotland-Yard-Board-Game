Sure, here is the README file structure and content, written in English and Markdown format, integrating your successful run screenshots and project details.

-----

## 🎲 Scotland Yard AI Board Game

[](https://www.google.com/search?q=LICENSE)

This repository contains the source code for an interactive digital version of the classic board game **Scotland Yard**, featuring an **advanced MiniMax AI implementation**.

### 📌 Project Highlights

  * **Core Game Model:** Implemented all core game components including player moves (single, double, secret), ticket management, and win conditions.
  * **AI Extension:** Developed an open-ended AI extension using the **MiniMax algorithm** with **Alpha-Beta pruning** to enhance performance by reducing the number of nodes evaluated in the game tree.
  * **Design Patterns:** Utilized the **Visitor design pattern** to handle `SingleMove` and `DoubleMove` cases separately, leveraging encapsulation.

-----

## 🖼️ Project Screenshots

### 1\. Game Setup Screen

The initial screen for configuring players, setting Mr.X and Detectives to either AI or Human control.

> **Description:** Allows configuration of players (Mr.X and Detectives) with options for AI or Human control. Move history (traces) are automatically enabled when an AI is active.

### 2\. Game In Progress

The main game interface showing the London map, player positions, and ticket information.

> **Description:** Displays the game board, ticket counts, and the travel log for Mr.X. The current player's available moves are highlighted on the map.

### 3\. AI Performance Demonstration (Recommended)

This screenshot emphasizes the project's focus on the AI by capturing the calculation state and, if available, performance metrics.

> **Description:** Captures the moment the AI is calculating its move (Status: Waiting for move [MRX]). If debug logs were included, the IntelliJ console would display **search depth, number of nodes explored, and calculation time**, showcasing the efficiency of the MiniMax algorithm.

-----

## 🛠️ Setup and Execution Guide

### 1\. Prerequisites

  * **Java Development Kit (JDK):** **Java 17** is required for all operating systems.
  * **JavaFX:** The **JavaFX SDK** is needed for the GUI.

| Component | Version | Architecture |
| :--- | :--- | :--- |
| **JDK** | **Java 17.0.14** | `homebrew OpenJDK 17.0.14 - aarch64` |
| **JavaFX SDK** | **17.0.17 LTS** | `macOS aarch64 SDK` |

### 2\. IntelliJ IDEA Configuration (macOS / Apple Silicon Fix)

To successfully run the GUI (JavaFX), the following steps are mandatory:

1.  **Add JavaFX Libraries:**

      * Navigate to `File` \> `Project Structure` \> `Libraries`.
      * Add the **`lib` folder** from your downloaded `javafx-sdk-17.0.17` directory.

2.  **Set VM Options (Crucial for GUI):**

      * Go to `Run` \> `Edit Configurations...` and select the **`Main`** Application configuration.
      * In the **VM options** field, enter the following command, using your specific path to the JavaFX `lib` folder:

    <!-- end list -->

    ```bash
    --module-path /Users/Jeehyun/Downloads/javafx-sdk-17.0.17/lib --add-modules javafx.controls,javafx.fxml
    ```

### 3\. Running the Game

  * Locate the **`Main.java`** class in the project.
  * Click the **Run button** (green play button) in IntelliJ IDEA to execute the program.

-----

## 💡 Limitations and Improvements

The project meets all requirements and passes tests, but the following areas could be improved:

  * **Turn Handling:** Player turn and move availability logic can be complex, particularly when detectives are blocked or out of tickets.
  * **Modularity:** Game state update logic could be made more modular to simplify future changes or extensions.
  * **Maintainability:** Reducing the overhead from copying immutable data and improving the clarity of error messages would help maintainability and flexibility.
