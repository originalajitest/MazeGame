# My Personal Project -- A Game

## Current project

<br>
References:
<ul>
<li> website for maze6 (https://keesiemeijer.github.io/maze-generator/#generate) with inputs: (thickness-10; Columns-25; Rows-20; Entries-none)</li>
<li></li>
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

*I will  add more based on program completion and completion of stretch goals.*


<!--An example of text with **bold** and *italic* fonts.>>  