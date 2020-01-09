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
This class contains individual signals and their respective elements, such as buttons and labels. Each DSignal object has 2 ArrayLists, edge_type and edge_coords, which record the type (positive or negative) and coordinates of all edges in its corresponding signal. On left click, it will place a positive edge, and on right click, a negative edge. When the mouse is dragged, the signal will be redrawn. Left click will raise the signal and right click will lower it.

### UML Diagrams
