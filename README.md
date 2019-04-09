# Chronos
## About
Chronos is an exciting application used to create and play adventure games! The project consists of two parts: QuestMaster and Explorer. QuestMaster is used by authors to make maps for adventure games, and Explorer is used by players to play the adventure games.

## Installation
To install Chronos, clone this Git repository, or copy these files onto your local filesystem. Load the project into your favorite Java IDE, then locate a commons-io-2.6.jar and add it to the project as an external jar.

## Usage
To run Chronos, run the main method in AuthorWindow. You will begin with the Author view. From here you can draw walls and rooms, place the player start point, add doors, archways, and ladders, and change descriptions for rooms and doors. When you are done authoring and would like to play, select the "Start Playing" option, and it will start your adventure on the map. Here you can use the arrow keys or WASD to navigate.

## Architecture

![Architecture Diagram](architecture.jpg "Architecture Diagram")
This image illustrates the relationship between the internal components of Chronos. It has been designed to be extensible in the future, so that more complex functionality can be added to the project. This project internally uses an MVP structure, which makes organization and regression testing much more straightforward.


## Libraries Used
* Java Swing - included in standard JDK
* Apache Commons IO - https://commons.apache.org/proper/commons-io/download_io.cgi
* JUnit5 - included in standard JDK

## Tips
* Have fun!

## Known Outstanding Bugs
* Transparent Wall on Angles - A transparent wall represented by a dotted line should split one room into two along that line. In cases where the opaque walls of the room are not vertical or horizontal then the rooms are not split properly. The line is drawn but you will see that the rooms created do not have the right shape. 

### Authors
Chronos was started as ArenaMapMaker and was written by the Spring 2018 Capstone team "TTD", Daniel Bond, Tom Burnett, and Tommy Delgado.  The project was then renamed to Chronos and worked on by the Spring 2019 Capstone team "Praise the Sun," Danny Diesel, David Egbert, Becca Danik, Ryan Wires, and Allen Lin.
