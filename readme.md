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

## Algorithm Explanation

We decided to use a wait/notfiy structure for a lot of the GraphNode/MobileAgent communication to allow for less CPU-intensive computations. Essentially, if action was needed on another Node, it should be notified.

When a node is notified, it typically does the following
- Check for messages to process
- Check for a change in its status

Depending on what it finds from the two previous bulletpoints, it can notify others or message others.

The messaging infrastructure works like the following
- A Mobile Agent puts the message in the mailbox of the attached GraphNode
- The GraphNode processes it and pushes it to another node.
- The nodes repeatedly push the message until either it can't or until it finds the base station.
- Since the packet keeps track of where it has been, loops are prevented.
- When a packet can't go any further. It backtracks with a "Receipt", notifying previous nodes that the original direction was wrong.
- If the packet backtracks all the way to the original sender, and there is no availabled path, then the packet stops sending.

This can be improved with a hop algorithm, but this is one of the better decentralized approaches to message routing that we have attempted

The following is sample output of what our messaging infrastructure looks like. (We only allow the Base station to print, all Nodes/MobileAgents use the base station as a proxy)

```
LOG: MA: 6 1 | Status: GREEN | Unique Packet ID: 9151
LOG: MA: 5 0 | Status: GREEN | Unique Packet ID: 19890
LOG: MA: 7 2 | Status: YELLOW | Unique Packet ID: 18498
LOG: MA: 4 1 | Status: GREEN | Unique Packet ID: 13712
LOG: MA: 7 0 | Status: YELLOW | Unique Packet ID: 10432
LOG: MA: 3 0 | Status: GREEN | Unique Packet ID: 12790
LOG: MA: 2 2 | Status: GREEN | Unique Packet ID: 3518
LOG: MA: 7 0 | Status: RED | Unique Packet ID: 11835
```

I hope you enjoy viewing our game and we wish you the best!

## Bugs and Assumptions

The initial game settings are up to specifications set by professor Chenoweth and if modifications are made, the risk is inheritable upon the changer. However, here are a list of assumptions that we have made for creating this project:
* We have assumed that there must be an initial way for the Mobile Agent in the first walk to navigate to the fire. Without an initial way, the walking would fail and cease to stop.
* We have assumed that the user would not try to run the program without selecting a file. Disregarding this will lead to an error message notifying the user of their mistake, but will not cause any adverse effects on the program
* A receipt can attempt to be returned, but the node that it might have returned to would have already been consumed by fire. However, this might be a feature due to the disruption that the fire causes being more properly emulated.


## Built With
This was made using Java SDK 1.8
* [JavaFX](https://openjfx.io/) - The GUI framework used

## Authors

* **Connor Frost** - *Developing work* - [CS351 Project 4](https://csgit.cs.unm.edu/frostc/)
* **Gavin McGuire** - *Developing work* - [CS351 Project 4](https://csgit.cs.unm.edu/mcguireg/)

