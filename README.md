# Chronos
## About
Chronos is an exciting application used to create and play adventure games! The project consists of two parts: QuestMaster and Explorer. QuestMaster is used by authors to make maps for adventure games, and Explorer is used by players to play the adventure games.

## Installation
To install Chronos, clone this Git repository, or copy these files onto your local filesystem. Load the project into your favorite Java IDE, then locate a commons-io-2.6.jar and add it to the project as an external jar.

## Usage
To run Chronos, run the main method in AuthorWindow. You will begin with the Author view. From here you can draw walls and rooms, place the player start point, place keys, add doors, archways, and stairs, and change descriptions for rooms and doors. When you are done authoring and would like to play, select the "Start Playing" option, and it will start your adventure on the map. Here you can use the arrow keys or WASD to navigate. Press 'k' to pick up keys, 'd' to drop keys, and 'l' to interact with locked doors.

## Architecture

![Architecture Diagram](https://github.com/dieseld2015/Chronos/blob/master/architecture%20diagram%20sp19.png)
This image illustrates the relationship between the internal components of Chronos. It has been designed to be extensible in the future, so that more complex functionality can be added to the project. This project internally uses an MVP structure, which makes organization and regression testing much more straightforward.


## Libraries Used
* Java Swing - included in standard JDK
* Apache Commons IO - https://commons.apache.org/proper/commons-io/download_io.cgi
* JUnit5 - included in standard JDK

## Tips
* Have fun!

## Known Outstanding Bugs
* Restoring the map - sometimes there are odd logical states that don't get reset correctly when restoring the map. This sometimes prevents the player from being able to move if the author presses play, closes the player window, then tries to play again.
* Combining Collinear Walls - Sometimes collinear walls are not combined correctly.

### Authors
Chronos was started as ArenaMapMaker and was written by the Spring 2018 Capstone team "TTD;" Daniel Bond, Tom Burnett, and Tommy Delgado.  The project was then renamed to Chronos and worked on by the Spring 2019 Capstone team "Praise the Sun;" Danny Diesel, David Egbert, Becca Danik, Ryan Wires, and Allen Lin. The project is sponsored by Alan Cline.
