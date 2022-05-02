

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [About Project](#About Project)

## General info
This project is Chess game created in Java 16 with Java Swing.
  ![image](https://user-images.githubusercontent.com/74488031/166233303-c030bdc9-136a-41fc-a152-b8a84815d8a2.png)

## Technologies
Project is created with:
* Java SE Development Kit 16
* Java Swing 
        
## About Project
My main purpose of creating this project was to learn the basic’s of OOP programing while creating something im passionate about - chess. I’ve used Java Swing as it is already built in Java’s SDK, the main gameboard contains Jpanel with 8x8 grid of Jbutton’s for chess Square’s, aswell as two Jlabel’s one on the left for rows numbers and one at the bottom for colums letters.


Application allow us to play vs simple computer, undoing last move, aswell as loading chess positions from FEN String.

The most difficult part in this project was implementing King’s check function – not only by recognizing when king is in danger, supressing his movement to only safe squares, but also allowing other pieces to block check – like breaking line of sight of enemy rook to our king, aswell as dissalowing pinned figures to move if the move would leave our king in check.

In the future I'm planning to add proper AI aswell as position analyzer.

