Other deliverables:
	- jar file (contains the source code, tests, javadoc documentation, and executables)
	- UML class diagrams
	- Sequence diagrams
	- Design decisions text file
	- User manual text file

Authors:
	- Aleksandar Veselinovic
	- Ali Fahd
	- Xiling Wang
	
Changes:
	- User enters number of troops they want to move into a captured country
	- User gets bonus troops at the start of their turn and must place all troops before attacking
	- Added a fortify button that allows the user to move troops once from one country to another connected country
	- Created an AI player that makes decisions based on the state of their countries

Future roadmap :
	- Enable ability to save and load games
	- Enable ability to load custom maps
	
Issues:
	- The executable jar file for some does not open the game due to the Image reader. 
	If it does does open up, the map might be a little bigger and you need to fullscreen to see the bottom panel.
	The program works as it should if ran in IntelliJ
	- For the test file, model needs a view in order to do all of its funcitonalilty, so the user/TA must input the
	number of players when they run the test class and go through the JOptionPane for the different tests hitting ok
	or entering the number of players (TA should try it with 3 players, passTest is adjusted for that number since
	the test class itself can not set the number of players).
	- When a dialog box with user input appears, if the user closes the box, the game does not function anymore
