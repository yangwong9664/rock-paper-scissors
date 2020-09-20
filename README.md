# Rock paper scissors game in Scala, with a simple Play UI

To run:

> `sbt run`

> Navigate to `localhost:9000/menu`


To run unit and integration tests, and code coverage report:

> `sbt clean coverage test it:test coverageReport`

Features:

- Player vs Computer
- Computer vs Computer
- Ensure Computer doesn't follow a set of choices, make it random every time the game is played.

Technical criteria:

- UI not important
- MVP, elegant and simple > feature rich
- Scalable code to make adding features easy, such as e.g Rock, Paper, Scissors, Lizard, Spock, etc.

Potential features after MVP:

- Better UI
- Main Menu navigation
- Difficulty levels for Computer, looks at patterns in your strategy to predict how to beat you
- DLC addons, different game modes
