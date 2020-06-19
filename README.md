# Instructions For Download
You will need at least Java 8 installed to run this program, which you can find here: https://www.java.com/en/download/

Downloads can be found in the Releases tab.

Windows - Download TDraw.exe and run.

Mac OS - Download TDraw.jar and run. It is possible that you may need to run from terminal with admin privileges to start.

# How to use
Simply left click to draw a high signal, right click to draw a low signal. When you are done, File > Export as PDF and choose a directory and the program will save your diagram as a PNG file.

# Documentation

## Classes:

### TDraw:

This class launches the application, and is the primary class responsible for the layout of the page. It contains a menu bar and some buttons. It also contains a Scene 'diagram' which contains a number of DSignals which can be dynamically added and removed.

### DSignal:

The DSignal class holds all information necessary for an individual signal. This class contains handlers for mouse clicking, dragging, and releasing.

## UML Diagram:
