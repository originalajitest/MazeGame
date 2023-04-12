# My Personal Project -- A Game

## Current project

Current functionality:
<ul>
<li>Can load a new game or an old save</li>
<li>Can go into any maze, but does not go into one that has been completed before.</li>
<li>There is an ongoing timer that calculates how long to complete the mazes, it does not account for previous solves.</li>
<li>You can change the color and this color change persists throughout the mazes.</li>
<li>You can change the visibility so that you are limited to a set number of blocks around you, for below 3, it assumes the visibility for the see-able path print, but greater than 3 then the program only allows 3 blocks around the path</li>
<li>You can save the game state at any one point.</li>
<li>A cheat code has been implemented to solve a maze quickly and beat the game.</li>
<li>You can easily change the mazes with new pictures which meat a set of requirements, they myst be one a grid layout and each block must have the same width and height as the rest. It must also have two cyan blocks to indicate the start and end points. The you just change the link in AssignMaze and also indicate how many rows and columns it has then the program should do the rest.</li>
</ul>

<br>
References:
<ul>
<li> website for maze6 (https://keesiemeijer.github.io/maze-generator/#generate) with inputs: (thickness-10; Columns-25; Rows-20; Entries-none)</li>
</ul>

## Answering questions

What will this application do:
- It is a simple game made up of 6 mazes for the user to complete and get to the end room.
- Based on completion of stretch goals this may change.

Who will use it:
- For any casual gamer or just as a simple game to de-stress after work.

Why is this project of interest to me:
- I like playing games and this would be an interesting jump into making games for hopefully and fun journey.

## Basic Functionality:

Primary goals:
- There will be total of 6 mazes in increasing difficulty for the user to complete.
- The screen will also have medals shown, greyed out if not yet obtained and colorful if obtained.
- They will be shown on the main screen numbered 1 to 6 with the number corresponding to the difficulty (1 being the
easiest).
- After completing each maze, the user will collect a medal at the end (easily see able), it will be a collectible and
will add to their data.
- The medals will be numbered randomly so that this can be linked to *Extra stretch goals*.
- Then they will come back to the start by a teleport circle.
- Once all maps are complete, the game will check based on the number of medals, another
gate will appear to lead to the end credits/room.

Stretch Goals:
- To cover the map in black with only the start and the end being see-able as the user progresses, they will be able to
- see more of the map (it unlocks a fixed number of blocks around them each time) with it being permanently see-able.
- This data about how much of the map can be seen should be sent to the data file so that it can be called upon resuming. 
- I will have to add a function on the start screen (this will not be there unless this code has been implemented),
where the user will be able to switch this difficulty off.

Extra Stretch Goals:
- I will make 3 different mini puzzles like those adds you see on YouTube about flowing water etc. to get to some
treasure. These will be assigned to specific numbers so that they are random and can be seen is any maze based on luck.
- This will mean that the medal is given after the puzzle is complete.
- A different extra stretch goal is also making variations of mazes for each level to keep it fresh.

## User Stories:

<h3>Phase 0:</h3>
<h4>Story 1</h4>
<ul>
<li> As a user I want to be able to start a game</li>
<li> I should be able to pick a specific level to play out of the 6</li>
<li> I should be able to add a medal to my collection</li>
<li> I should be able to teleport out of a maze with the medal</li>
<li> I should be able to quit and come back to the state I left the game in</li>
<li> I should be able to add all medals by commands, and then hence get to end credits</li>
</ul>


<!-- The number in list-style is the emoji unicode -->

<h3>Phase 1:</h3>
<h4>Story 2</h4>
<ul style = "list-style:'\1F3CE'">
<li> The user will get into the program</li>
<li> They will pick a maze one of 1 or 2</li>
<li> They will go through the maze using provided inputs</li>
<li> Once they are done the program will end</li>
</ul>

<h4>Story 3</h4>
<ul style = "list-style:'\1F3CE'">
<li> The user will run the program</li>
<li> The user will pick maze 1</li>
<li> They will try to move up and will get an 'Input Out of Bound' error</li>
<li> They will try to move left and get an 'Invalid Input' error</li>
<li> They can now finish the maze if they want</li>
</ul>

<h4>Story 4</h4>
<ul style = "list-style:'\1F3CE'">
<li> The user will run the program</li>
<li> The user will then enter each maze and run through them</li>
<li> Answering not to all requests to do the maze again later</li>
<li> Once mazes 1-3 are all finished and solved, the program will output 
'Congratulations, all mazes have been completed.' </li>
<li> The program will then end</li>
</ul>

<h4>Story 5</h4>
<ul style = "list-style:'\1F3CE'">
<li> The user will run the program</li>
<li> They can enter the maze and do some of it if they want</li>
<li> They will stop the program by quiting it</li>
</ul>


<h3>Phase 2:</h3>
<h4>Story 6</h4>
<ul style = "list-style:'\1F3CD'">
<li> The user will start the program</li>
<li> They will opt to quit at the point</li>
<li> They will exit the program at that point.</li>
</ul>

<h4>Story 7</h4>
<ul style = "list-style:'\1F3CD'">
<li> The user will start the program</li>
<li> They will opt to start a new game</li>
<li> They will enter the first maze by entering 1</li>
<li> They will move the user down by one move</li>
<li> Then refuse to quit or save</li>
<li> They move down again</li>
<li> Save the game state by pressing s</li>
<li> Quit the game by pressing q</li>
</ul> 

<h4>Story 8</h4>
<ul style = "list-style:'\1F3CD'">
<li> The user will start the program</li>
<li> They will opt to load a game</li>
<li> The previous game from user story 7 will be loaded</li>
<li> The user will enter maze 1 again</li>
<li> They player should be where it was left, 2 places below S</li>
<li> They user will finish the maze by going right once</li>
<li> Refuse to save</li>
<li> Refuse to quit</li>
<li> Type 'no' so they do not have to enter the maze again</li>
<li> Then save and quit</li>
</ul>

## Phase 4: Task 2

Fri Apr 07 18:21:02 PDT 2023: 	Initializing mazes:

Fri Apr 07 18:21:02 PDT 2023: 	&emsp;	 Maze 4 Initialized.

Fri Apr 07 18:21:02 PDT 2023: 	&emsp;&emsp;		 Maze start and end points initialized.

Fri Apr 07 18:21:02 PDT 2023: 	&emsp;	 Maze 2 Initialized.

Fri Apr 07 18:21:02 PDT 2023: 	&emsp;&emsp;		 Maze start and end points initialized.

Fri Apr 07 18:21:02 PDT 2023: 	&emsp;	 Maze 1 Initialized.

Fri Apr 07 18:21:02 PDT 2023: 	&emsp;&emsp;		 Maze start and end points initialized.

Fri Apr 07 18:21:02 PDT 2023: 	&emsp;	 Maze 6 Initialized.

Fri Apr 07 18:21:02 PDT 2023: 	&emsp;&emsp;		 Maze start and end points initialized.

Fri Apr 07 18:21:02 PDT 2023: 	&emsp;	 Maze 3 Initialized.

Fri Apr 07 18:21:02 PDT 2023: 	&emsp;&emsp;		 Maze start and end points initialized.

Fri Apr 07 18:21:02 PDT 2023: 	&emsp;	 Maze 5 Initialized.

Fri Apr 07 18:21:02 PDT 2023: 	&emsp;&emsp;		 Maze start and end points initialized.

Fri Apr 07 18:21:02 PDT 2023: 	New Game Instance made.

Fri Apr 07 18:21:02 PDT 2023: 	Initializing player for Maze 3

Fri Apr 07 18:21:02 PDT 2023: 	&emsp;	 Player initialized at (40,19)

Fri Apr 07 18:21:02 PDT 2023: 	Initializing player for Maze 1

Fri Apr 07 18:21:02 PDT 2023: 	&emsp;	 Player initialized at (0,0)

Fri Apr 07 18:21:02 PDT 2023: 	Initializing player for Maze 0

Fri Apr 07 18:21:02 PDT 2023: 	&emsp;	 Player initialized at (1,0)

Fri Apr 07 18:21:02 PDT 2023: 	Initializing player for Maze 5

Fri Apr 07 18:21:02 PDT 2023: 	&emsp;	 Player initialized at (1,5)

Fri Apr 07 18:21:02 PDT 2023: 	Initializing player for Maze 2

Fri Apr 07 18:21:02 PDT 2023: 	&emsp;	 Player initialized at (0,0)

Fri Apr 07 18:21:02 PDT 2023: 	Initializing player for Maze 4

Fri Apr 07 18:21:02 PDT 2023: 	&emsp;	 Player initialized at (0,21)

Fri Apr 07 18:21:06 PDT 2023: 	Maze 1 solved.

Fri Apr 07 18:21:10 PDT 2023: 	Maze Wall color changed to blue

Fri Apr 07 18:21:12 PDT 2023: 	Maze 2 solved.

Fri Apr 07 18:21:15 PDT 2023: 	Maze Wall color changed to cyan

Fri Apr 07 18:21:19 PDT 2023: 	Maze Wall color changed to gray

Fri Apr 07 18:21:20 PDT 2023: 	Maze 3 solved.

Fri Apr 07 18:21:28 PDT 2023: 	Maze Wall color changed to pink

Fri Apr 07 18:21:57 PDT 2023: 	Maze 4 solved.

Fri Apr 07 18:22:01 PDT 2023: 	&emsp;	 Cheat Code used.

Fri Apr 07 18:22:01 PDT 2023: 	Maze 5 solved.

Fri Apr 07 18:22:04 PDT 2023: 	&emsp;	 Cheat Code used.

Fri Apr 07 18:22:04 PDT 2023: 	Maze 6 solved.

Fri Apr 07 18:22:04 PDT 2023: 	All mazes solved.


## Phase 4: Task 3

The design I made for the UML Class diagram is right, but it does not reflect the dependencies fully.
Especially how the functions relate with each other and which they call in effect. 

Now, in regards to improving my design, I would first try to get full code coverage for my tests (I think that it is currently a server side error and as such I am not able to get the ful 99.5 {the last 0.5 is not worth it}).
Another improvement would be to move the visibility stuff to the model so you would save it and then call it and it would also be testable.
Another big thing would be to decrease the various method line as I have one that is 106 line and try to keep them low by factoring out duplicate buttons (there are two but they are in entirely different functions), this would move all buttons outside and will increase overall code length byt also increase readability.

I should also remove all debugging functions I used to ensure reliability and security of the program. I should also make all functions private and only accessible as the EventLog functions to improve security.
It would also be helpful to break down the Picking frame class into two separate classes to increase readability.
Another one would be to save the timer so that we can add it later.

Another interesting thing would be to make the walls with better textures instead of one solid color, and I plan to do this, but just don't have time currently.

<br>
*I will  add more based on program completion and completion of stretch goals.*


<!--An example of text with **bold** and *italic* fonts.>>  