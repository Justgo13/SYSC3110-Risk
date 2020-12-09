# SYSC3110-Risk
Readme file for SYSC3110 Fall 2020 project. 
The RISK board game has been an exciting game to play with family and friends that involves strategy and bit of luck. This project is designed to replicate
the game using Java's Graphical User Interface by implementing Object Oriented style programming. 
The objective of the game is to conquer as many countries and other player's occupied countries by eliminating them. The last player remaining is the victor. 

# Previous Deliverables
- Model for the MVC pattern of Risk game
- View and Controller of MVC pattern
- Code for the text-based Risk game 
- UML diagrams, sequence diagrams
- GUI based version of the game
- Bonus troop placement and reinforce troop placement
- AI player
- JUnit test

# Current Deliverables
- Save/load games
- Load custom maps
  -How to load map
    1. When starting the game, choose the option to select a custom map.
    2. Select a JSON file that represents the map (example maps can be found under example_maps directory).
    3. Click ok and a map will be generated with the custom JSON.

# How to run
1. Download the .JAR file
2. Navigate to where the .JAR file was downloaded
3. Open up command prompt where the .JAR file is
4. Run the command **java -jar SYSC3110-Risk.jar**

# Known issues
- GUI will sometimes resize the text area and button panel when a country button is clicked
- Valid custom map checker might have some edge cases that were missed

# JUnit version
JUnit4

# How to play
The text area on the right hand side will say whose turn it is and will report battle results

# To add bonus troops:
1. Click bonus troops button
2. Click a country to add troops
3. Choose from the dropdown the number of troop to add

# To attack:
1. Click attack button
2. Click one of your owned countries
3. Select from the dropdown how many troops to attack with
4. Choose an adjacent country to attack

# To reinforce: 
1. Click the reinforce button
2. Choose a country to reinforce from
3. Choose from the dropdown how many troops to reinforce with
4. Choose a highlighted country to reinforce

# Authors
Jason Gao, Harjap Gill, Shashaank Srivastava, Albara'a Salem


