//Title: 'Lost in the woods': A random walking simulator
//FileName: Main.java
//External Files Necessary to Run Program: N/A
//External File Created By Program: N/A
//Names of Programmers: Andrew Riebow
//Email Address of Programmers: asrm8d@mail.umsl.edu
//Course and Section Number: SP20-CMPSCI4500-002
//Date Finished and Submitted: February 2nd, 2020
//Explanation of Program: This program initially simulates 2 explorers on a grid (or 'people' lost in a 'forest'), and allows the user to input a grid size and see how long it takes for them to find each other.
//                        The explorers move randomly in one of 8 directions until they both land on the same square. Then, the program will run further simulations using more explorers and display averages and rates of growth for the simulations.
//Resources Used: https://docs.oracle.com/en/java/ for checking syntax and coding standards.
//                https://www.wikihow.com/Calculate-Growth-Rate for growth rate calculation.

//Importing necessary libraries
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

//Class that holds our entry point for the program, the 'main' function.
public class Main {

    static Random random = new Random(); //Create a Random object to do our random calculations for the whole program. We create this as a static object outside the main method scope so it can be used by our explorer class

    public static void main(String[] args) { //Entry point for our program
        Scanner userInput = new Scanner(System.in); //Create a Scanner object to handle user input

        int userWidth, userHeight; //Declare variables to hold our width and height of the grid

        System.out.println("'Lost in the woods'\n" +
                "This is a simulation where two people are put on a grid with both a length and height between 2 and 50 units.\n" +
                "They are placed on opposite corners of the grid (north west and south east), and will move randomly in one of eight directions each turn. If the move would place them out side the grid, they will stay still.\n" +
                "This program will determine the number of turns it takes for them to meet each other on the grid, or exit if it has taken over a million turns\n");

        System.out.println("Enter an integer between 2 and 50 for the width of the grid: ");

        //Loop to get width of the grid from the user
        do{
            if(userInput.hasNextInt()) { //When the user enters into the console, check if it is an integer
                userWidth = userInput.nextInt(); //If it is, store that value
                if(userWidth < 2 || userWidth > 50){ //Check that the value is within the limits. If not, repeat the loop
                    System.out.println("Your integer must be between 2 and 50, try again.");
                    userInput.nextLine(); //Clear input again
                }
                else{
                    userInput.nextLine(); //If it is within the limits, ignore everything else input by the user after the integer (a way to clear the buffer for the next input), and break out of the loop
                    break;
                }
            }
            else{ //If not an intger
                userInput.nextLine();//Clear the user input
                System.out.println("Your input must be an integer between 2 and 50, try again."); //Display message prompting different input
            }
        }while(true); //Repeat until we break out of the loop with a valid integer

        System.out.println("Enter an integer between 2 and 50 for the height of the grid: ");

        //Loop to get height of the grid from the user, same as the above loop but for height
        do{
            if(userInput.hasNextInt()) { //When the user enters into the console, check if it is an integer
                userHeight = userInput.nextInt();
                if(userHeight < 2 || userHeight > 50){ //If it is an integer, check it is within the limits. If not, prompt user to try again
                    System.out.println("Your integer must be between 2 and 50, try again.");
                    userInput.nextLine(); //Clear input again
                }
                else { //If it is within limits, clear the buffer and exit the loop
                    userInput.nextLine();
                    break;
                }
            }
            else{//If not an integer
                userInput.nextLine(); //Clear the buffer
                System.out.println("Your input must be an integer between 2 and 50, try again."); //Prompt user to try again
            }
        }while(true); //Loop until breaking out with a valid integer


        //These classes are defined at the end of the project outside of the WalkingSimulator class

        Grid forest = new Grid(userWidth - 1,userHeight - 1); //Create our grid object based on the user input.
        //We subtract 1 from each because our grid is based at (0,0), so if the user wants a 2x2 grid, possible x values would be { 0, 1 } which is 2 different values. The same goes for the height.

        //Create out explorers and set one at the top left and one at the bottom right
        Explorer chris = new Explorer("Chris", 0, 0, forest, random);//Chris is at the top left of the grid
        Explorer pat = new Explorer("Pat", forest.getWidth(), forest.getHeight(), forest, random);//Pat is at the bottom right of the grid

        int turnCounter = 0; //Setup a variable to hold our number of turns

        //This loop runs a 'turn' of our simulation and will exit if the explorers are on the same point on the grid, or 1 million turns has been exceeded
        while(true){

            //Call a method on our explorers to have them move randomly in one of 8 directions (as long as it would be a valid move)
            chris.moveRandomly();
            pat.moveRandomly();

            turnCounter++;//Increment our turn counter

            if(turnCounter > 1000000){ //If it has been 1 million turns, exit
                System.out.println("The simulation has run for over a million turns, exiting...");
                break;
            }

            if(chris.getX() == pat.getX() && chris.getY() == pat.getY()){ //If the explorers are on the same point
                //Output information on the simulation
                System.out.println("They found each other on grid location: x = " + chris.getX() + " and y = " + chris.getY());
                System.out.println("It took " + turnCounter + " turns");
                break; //Exit the loop
            }
        }

        //This is the beginning of the 'extra' part of the program
        //Allow our user to read the simulation data before continuing
        System.out.println("\nPress enter to continue...");
        userInput.nextLine();
        System.out.println("\nNow for some extra simulation data. The following simulations are on a 4x4 grid. We first will test with 2 explorers, run that simulation 100 times, and then display the average.\n" +
                "We will increase the number of explorers each time up until 6 explorers. The rules are the same, except the simulation only ends when every explorer is on the same grid location and their starting locations are randomized.\n" +
                "Failed simulations are not counted in the average.\n");

        System.out.println("\nPress enter to continue...");
        userInput.nextLine();

        //We run multiple simulations on a 4x4 grid with an increasing number of explorers. We then calculate the growth rate from the previous simulation 'group'
        int firstSim = runSimulation(2, 4, 4, 100); //Using 2 explorers on a 4x4 grid, run 100 simulations and output the average number of turns taken
        System.out.println(""); //Used to format our output in a more clean way

        int secondSim = runSimulation(3, 4, 4, 100);//Using 3 explorers on a 4x4 grid, run 100 simulations and output the average number of turns taken, and so on
        System.out.println("Growth rate from previous: " + (float)(secondSim - firstSim)/firstSim * 100.0 + "%\n"); //Perform a growth rate calculation and display it

        int thirdSim = runSimulation(4, 4, 4, 100);
        System.out.println("Growth rate from previous: " + (float)(thirdSim - secondSim)/secondSim * 100.0 + "%\n");

        int fourthSim = runSimulation(5, 4, 4, 100);
        System.out.println("Growth rate from previous: " + (float)(fourthSim - thirdSim)/thirdSim * 100.0 + "%\n");

        int fifthSim = runSimulation(6, 4, 4, 100);
        System.out.println("Growth rate from previous: " + (float)(fifthSim - fourthSim)/fourthSim * 100.0 + "%\n");

    }

    //A method used to run multiple simulations and allowing configuration of the number of explorers, grid width, grid height, and number of simulations
    //Also outputs the information on the runs at the end
    static int runSimulation(int numOfExplorers, int gridWidth, int gridHeight, int numOfSimulations){
        Grid grid = new Grid(gridWidth - 1, gridHeight - 1);//Creates a grid from the method arguments
        //We subtract 1 from each because our grid is based at (0,0), so if we want a 2x2 grid, possible x values would be { 0, 1 } which is 2 different values. The same goes for the height.

        ArrayList<Explorer> listOfExplorers = new ArrayList<Explorer>(); //Initialize an ArrayList of explorers that we will iterate over each 'turn' in order to move them all randomly

        //Loop that will create a number of explorers and add them to our listofExplorers a number of times based on the method arguments
        for(int i = 0; i < numOfExplorers; i++){
            listOfExplorers.add(new Explorer("SimulationExplorer" + i, 0, 0, grid,  random)); //Create a new explorer object and add it to our list
        }

        int totalTurns = 0; //Variable to hold the total turns taken by all the simulations
        int failedCounter = 0; //Variable to hold the number of simulations that exceeded 1 million turns

        //Loop that runs each simulation.
        for(int i = 0; i < numOfSimulations; i++){

            //Loop that will reset each explorer to a random spot on the grid at the start of each simulation
            for(Explorer explorer : listOfExplorers){
                explorer.setX(random.nextInt(gridWidth));
                explorer.setY(random.nextInt(gridHeight));
            }

            int turnCounter = 0;//Set our turn counter for the current simulation to 0

            //Loop that will move the explorers randomly each turn for the current simulation, and exit if the turn count is greater than 1 million
            while(true){

                //Move each explorer in our list randomly
                for(Explorer explorer : listOfExplorers){
                    explorer.moveRandomly();
                }

                turnCounter++;
                if(turnCounter > 1000000){ //Exit simulation if it has been greater than 1 million turns
                    failedCounter++;
                    break;
                }

                int checkX = listOfExplorers.get(0).getX(); //Set a variable equal to the first explorer's x coordinate in the list
                int checkY = listOfExplorers.get(0).getY(); //Set a variable equal to the first explorer's y coordinate in the list
                boolean explorersOnSameSqaure = true; //Assume the explorers are on the same point on the grid unless proven false

                //Loop to check each explorer's coordinates
                for(Explorer explorer : listOfExplorers){
                    if(explorer.getX() != checkX || explorer.getY() != checkY){ //If the current explorer's coordinates are not equal to the first explorer's coordinates
                        explorersOnSameSqaure = false; //Then we know not all explorer's are on the same point, so we set our boolean to false and continue the simulation
                    }
                }
                if (explorersOnSameSqaure){ //If they are all on the same point, then we break out of the loop
                    break;
                }
            }
            totalTurns += turnCounter; //We add the number of turns this simulation took to our total turns variable
        }
        //Output information on all of the simulations that were run with these settings
        System.out.println(numOfSimulations + " simulations ran with " + numOfExplorers + " explorers on a " + gridWidth + "x" + gridHeight + " grid. Average number of turns for successful simulations: " + totalTurns/numOfSimulations + "\n" +
                "Failed simulations: " + failedCounter);

        return totalTurns/numOfSimulations; //Return the average number of turns taken with these settings
    }
}

//This is a class that represents a 'person' in the 'forest' or how it is commonly referred to in this program, an explorer on a grid
class Explorer{
    private String name; //Name of the explorer
    private int x, y; //Two variables, x and y for the coordinates they are on the grid
    private Grid grid; //A grid object, used to check itself to make sure it doesn't leave the boundaries
    private Random random; //A reference to our static Random object created at the start of the program

    //A simple constructor used to initialize our fields
    public Explorer(String name, int x, int y, Grid grid, Random randomNumberGen) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.grid = grid;
        this.random = randomNumberGen;
    }

    //A method that generates a random number from 0 to 7, and moves tries to move in one of the 8 possible directions based off of that number
    public void moveRandomly(){
        switch (random.nextInt(8)){ //Begin switch statement and generate number

            //North
            case 0:
                if(y > 0){ //If it would be in bounds after moving in the direction
                    y--; //Move in that direction
                }
                break;

            //NorthEast
            case 1:
                if(x < grid.getWidth() && y > 0){ //If it would be in bounds after moving in the direction
                    x++; //Move in that direction
                    y--; //Move in that direction. And so on down the list
                }
                break;

            //East
            case 2:
                if(x < grid.getWidth()){
                    x++;
                }
                break;

            //SouthEast
            case 3:
                if(x < grid.getWidth() && y < grid.getHeight()){
                    x++;
                    y++;
                }
                break;

            //South
            case 4:
                if(y < grid.getHeight()){
                    y++;
                }
                break;

            //SouthWest
            case 5:
                if(y < grid.getHeight() && x > 0){
                    x--;
                    y++;
                }
                break;

            //West
            case 6:
                if(x > 0){
                    x--;
                }
                break;

            //NorthWest
            case 7:
                if(y > 0 && x > 0){
                    x--;
                    y--;
                }
                break;
        }
    }

    //Getters and Setters for our private fields
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}

//Represents a 'forest' or grid that our explorers are on
class Grid{

    private int width, height; //A width and height of the grid

    //A simple constructor to initialize our fields
    public Grid(int width, int height){
        this.width = width;
        this.height = height;
    }

    //Getters for our width and height
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
