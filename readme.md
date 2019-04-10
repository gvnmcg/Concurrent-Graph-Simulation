# Mobile Agents Simulation

This java program is to simulate the spread of a fire across many interconnected nodes, and to test the "Mobile Agents" that monitor and attach themselves to each node. 

## Getting Started

The game's source code is located in the corresponding archives, and the jars are available for download. Either using the jars or the sorce code, will you be able to reproduce a working game.

### Prerequisites

This application was built using java SDK 1.8 using JavaFx

### Command Line Arguments

All settings can be configured either in the code or the GUI version
- If the user needs to slow down / modify the speed, it can be manipulated within the top of GraphNode
- Spread of the "fire" currently has no random effect (For easy viewing), but can still be configured in the top of GraphNode

For any other information that you might need. Please feel free to consult the documentation or the annotated code.

## Graphical User Interface

Running the jar, the first window has a couple options:

- The List of different graphs included in the program
- "Open" allows you to use a file that is not included
- "Start"  starts the simulation using the chosen file

As you can see from the following screenshots, you have the option of choosing a configuration file (Like those that Professor Chenoweth distributed on learn), or specifying your own.

You can also click on "Info" for information regarding the application

![Selection Screen](https://i.imgur.com/4t1Vw8H.png)

Selecting a file consists of hitting the "Open" button, and from there, you can navigate and select a file to be loaded into the Mobile Agents emulations!

![Selecting a file](https://i.imgur.com/oMezFV8.png)

Once you hit the "Start" button, you can relax and monitor the Mobile Agents game in Action. The log appears in the botton half of the screen and is limited to 300px. You can also find the log in the console for ease of use.

![Emulation 1](https://i.imgur.com/IjK2fzR.png)

Once the emulation is done, you can exit out of the program by clicking the red button on the top right corner of the screen

![Emulation 2](https://i.imgur.com/LEMKzvA.png)

## Header

Blah Blah Blah Blah Blah Blah Blah Blah 

```
Graveyard is at 4 tiles!
Board:	[3:3] 
Please select one to play! (index L|R)
[4:4][2:2][4:3][3:1][1:0]
5 R
```

Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah 

```
Graveyard is at 4 tiles!
Board:	[2:3] [3:3] [0:1] 
Please select one to play! (index L|R)
[4:4][2:2][4:3][3:1]
0
Parsing Invalid! Try again!
```

Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah 

## GUI Simulation!

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