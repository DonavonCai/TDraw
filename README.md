# TDraw
## Overview
This software is used to draw timing diagrams consisting of multiple named signals.

## Specifications / Features
* Can edit a signal to add positive and negative edges.
* Can add and remove signals
* Can rename signals

## Documentation / Classes

### TDraw
This class contains the main() function and is responsible for launching the program. In addition, this class handles the layout of all other classes on the page.

### DSignal
This class contains individual signals and their respective elements, such as buttons and labels. On left click, it will place a positive edge, and on right click, a negative edge. When the mouse is dragged, the respective high or low signal will be extended.

The DSignal class has 3 event listeners for mouse events: press, drag, and release.
* On press: A positive or negative edge is placed if applicable.
* On drag: The respective high or low signal is extended from the initial click in the direction of the mouse drag. The "closing" edge is continuously redrawn at the current coordinates of the mouse. Previous drawings of the closing edge, now obsolete, are drawn over with a white rectangle.
* On release: The closing edge is finalized and any variables needed for direction checking are reset if necessary.

### UML Diagrams
