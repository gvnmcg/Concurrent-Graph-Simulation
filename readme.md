# Mobile Agents Simulation

This java program is to simulate the spread of a fire across many interconnected nodes, and to test the "Mobile Agents" that monitor and attach themselves to each node. 

## Getting Started

The game's source code is located in the corresponding archives, and the jars are available for download. Either using the jars or the sorce code, will you be able to reproduce a working game.

### Prerequisites

Java SDK 10 is the primary version of Java that this application was written in.

### Command Line Arguments

All settings are configured inside the code, as there is not much left up to the user to decide upon.

##Graphical User Interface

Running the jar, the first window has a couple options:

- The List of different graphs included in the program.
- "Open" allows you to use a file that is not included.
- "Start"  starts the simulation using the chosen file.

As you can see from the following screenshots, you have the option of choosing a configuration file (Like those that Professor Chenoweth distributed on learn), or specifying your own.

You can also click on "Info" for information regarding the application.

![Selection Screen](https://i.imgur.com/4t1Vw8H.png)

Selecting a file consists of hitting the "Open" button, and from there, you can navigate and select a file to be loaded into the Mobile Agents emulations!

![Selecting a file](https://i.imgur.com/oMezFV8.png)

Once you hit the "Start" button, you can relax and monitor the Mobile Agents game in Action. The log appears in the botton half of the screen and is limited to 300px. You can also find the log in the console for ease of use.

![Emulation 1](https://i.imgur.com/IjK2fzR.png)

Once the emulation is done, you can exit out of the program by clicking the red button on the top right corner of the screen.

![Emulation 2](https://i.imgur.com/LEMKzvA.png)

## GUI Simulation!

In the simulation GUI the nodes are connected by lines to shoe there neighbor relation. They change color as the fire effects each running GraphNode thread.
- Nodes Start as Blue meaning they are fine and awaiting for another node to notify them.
- Once notified they turn Yellow, which means one of their neighbors is on fire.
- Red Nodes have sent messages to each of their neighbors and their thread has ended.

## Assumptions

The initial game settings are up to specifications set by professor Chenoweth and if modifications are made, the risk is inheritable upon the changer.

* Point 1
* Point 2

## Built With
This was made using Java SDK 10.0.2
* [JavaFX](https://openjfx.io/) - The GUI framework used

## Authors

* **Connor Frost** - *Developing work* - [CS351 Project 4](https://csgit.cs.unm.edu/frostc/)
* **Gavin McGuire** - *Developing work* - [CS351 Project 4](https://csgit.cs.unm.edu/mcguireg/)

## TODO

* **Create customizable gui** - *Start button, graph generation, graph picker, etc..* - 
* 