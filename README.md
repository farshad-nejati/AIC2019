## Sharif AI Challenge 2019 – Java Client

This repository contains a Java client AI for the **Sharif AI Challenge 2019**, an annual programming competition organized by the Scientific Society of the Computer Engineering Department (SSC) at Sharif University of Technology.

In this competition, participants design and implement a powerful artificial intelligence agent that competes against other teams’ AIs in a strategic battle game. Each AI controls a team of heroes on a grid-based map, makes decisions about movement and abilities, and tries to outplay opponents through better strategy and coordination.

The goal of the event is to encourage students to:
- **Design and implement non‑trivial game AIs**
- **Reason about strategy, pathfinding, targeting, and resource management**
- **Compete in a real tournament environment** where their AI automatically plays many matches against others.

This project is one such AI client. The main components are:
- `Main` – entry point that connects to the game server using environment variables (`AICHostIP`, `AICHostPort`, `AICToken`, `AICRetryDelay`).
- `Controller` / `Network` – handle communication with the competition server.
- `AI` – high-level decision loop that is called by the game engine each turn (`preProcess`, `pickTurn`, `moveTurn`, `actionTurn`).
- `RandomAI` package – baseline/random strategy for movement and actions.
- `NewAI` package – more advanced, heuristic-based strategy for selecting targets, coordinating heroes, and casting abilities.
- `client.model` and `common` packages – data models and utilities provided by the organizers (world state, heroes, abilities, events, networking helpers, etc.).

### How it works (high level)
- On startup, `Main` reads connection parameters from environment variables and starts the `Controller`.
- The server sends the current **world state** (map, heroes, visibility, etc.) to the client every turn.
- The `AI` class:
  - Picks heroes during the drafting (pick) phase.
  - Moves heroes toward objective zones in the movement phase.
  - Uses either the random or the advanced (`NewAI`) logic in the action phase to select abilities and targets based on visibility, range, and potential damage.

You can customize the strategy by modifying the logic in `AI`, `NewAI`, and the helper classes in `client.NewAI` and `client.RandomAI`.