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

Future roadmap :
	- Adding additional bonus army placement based on number of territoires and area conquered
	- Adding the fortify phase after attack to move troops
	- Adding AI players
	- Enable ability to save and load games
	- Enable ability to load custom maps
	
Issues:
	- The executable jar file for some does not open the game due to the Image reader. If it does does open up, the jar makes the map a little bigger and you need to 		  fullscreen to see the whole map.
	  The program works as it should if ran in IntelliJ
	- For the test file, model needs a view in order to do all of its funcitonalilty, so the user/TA must input the number of players when they run the test class and go                 through the JOptionPane for the different tests hitting ok or entering the nuber of players (TA should try it with 3 players, passTest is adjusted for that number since           the test class itself can not set the number of players).
