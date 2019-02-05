# ArenaMapMaker
## About
ArenaMapMaker is a project aiming to add a graphical authoring and playing tool to the INFORM story language. 

## Installation
To install ArenaMapMaker, clone this Git repository, or copy these files onto your local filesystem. Load the project into Eclipse, then locate a commons-io-2.6.jar and add it to the project in Eclipse as an external jar.

## Usage
To run ArenaMapMaker, run the main method in AuthorWindow. You will begin with the Author view. From here you can draw rooms, place the player start point, add doors, split rooms with transparent walls, and change descriptions for rooms and doors. When you are done authoring and would like to play, select the "Start Playing" option, and it will translate the map to INFORM source, compile the source (on Mac and Windows), and present a side-by-side play mode with the map and story panel. Here you can use the arrow keys to navigate on the map side, or type into the story panel to play the game like any INFORM game.
It should be noted that to complete a room, one must click back onto the starting point. So, even if a wall is existing for another room, it will still need to be drawn again for a new room.

## Architecture

![Architecture Diagram](architecture.jpg "Architecture Diagram")
This image illustrates the relationship between ArenaMapMaker and the INFORM compilers and interpreters. It has been designed to be extensible in the future, so that more complex functionality can be added to the project. This project internally uses an MVP structure, which makes organization and regression testing much more straightforward.


## Libraries Used
* Java Swing - included in standard JDK
* JavaFx - included in standard JDK
* Apache Commons IO - https://commons.apache.org/proper/commons-io/download_io.cgi
* JUnit5 - included in standard JDK

## Tips
* You will need to become familiar with the INFORM language to complete this project.

[for introductory information look here](http://inform7.com/)

[And for examples look here](http://web.cse.ohio-state.edu/~cline.4/INFORM/) 
* If there is no story output then it is likely the INFORM source at Project/Source/story.ni did not compile properly. You can run the generated source through the external inform application and it will tell you what went wrong.
* You can verify INFORM source compiles by opening Project/Release/play.html in eclipse or in a web browser. 

## Known Outstanding Bugs
* INFORM side player movement - If you give input to go north, east, south, or west the player should move in that direction but it should only send the command to inform if the player is entering a new room on the graphics side. Currently commands always go to inform.
* Transparent Wall on Angles - A transparent wall represented by a dotted line should split one room into two along that line. In cases where the opaque walls of the room are not vertical or horizontal then the rooms are not split properly. The line is drawn but you will see that the rooms created do not have the right shape. 
* INFORM compiler failures - Inform will detect contradictions in the directional relationships between rooms. In some cases, room locations will be interpreted into a INFORM map which will fail to compile. There is a particular issue with detecting rooms as NE, NW, SE, SW when they should not be according to the map.

### Authors
ArenaMapMaker was written by the Spring 2018 Capstone team "TTD", Daniel Bond, Tom Burnett, and Tommy Delgado.
