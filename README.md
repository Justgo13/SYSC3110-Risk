# SYSC3110-Risk
Readme file for SYSC3110 Fall 2020 project. 
The RISK board game has been an exciting game to play with family and friends that involves strategy and bit of luck. This project is designed to replicate
the game using Java's Graphical User Interface by implementing Object Oriented style programming. 
The objective of the game is to conquer as many countries and other player's occupied countries by eliminating them. The last player remaining is the victor. 

# Current Deliverables
- Model for the MVC pattern of Risk game
- Code for the Risk game 
- UML diagrams, sequence diagrams

# Road map
- GUI based version of the game
- View and Controller of the MVC pattern
- JUnit test
- Additional features of Risk game such as bonus army, troup placement, AI, and reinforcements
- Save / Load features
- Custom maps

# How to run
1. Download the .JAR file
2. Navigate to where the .JAR file was downloaded
3. Open up command prompt where the .JAR file is
4. Run the command **java -jar SYSC3110-Risk.jar**

# Known issues
- Attack method in RiskGame class should do more delegation to the Board class to reduce coupling in RiskGame.

# How to play
**Commands:**
> Note: All commands are **case sensitive** please run the help command if your unsure of how to run the commands.
<ul>
  <li>Help</li>
  Prints out all the valid commands that can be run for each player which include attack, map, endTurn, and help.
  <li>Map</li>
  Prints out text representing the current map status showing each player's countries owned and troop count in that respective country.
  <li>End Turn</li>
  Ends the player turn and passes it to the next player
  <li>Attack</li>
    <ol>
      <li>Choose a country to attack from</li>
      <li>Choose a country to attack</li>
      <li>Choose the number of troops to attack with</li>
    </ol>
</ul>

# Authors
Harjap Gill, Jason Gao, Shashaank Srivastava, Albara'a Salem


