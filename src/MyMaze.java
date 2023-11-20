// Names: Sandy Llapa
// x500s: llapa016

import java.util.Random;
import java.util.Scanner;

public class MyMaze{
    Cell[][] maze;
    int startRow;
    int endRow;

    public MyMaze(int rows, int cols, int startRow, int endRow) {
        maze = new Cell[rows][cols];
        this.startRow=startRow;
        this.endRow=endRow;
        for(int j=0;j< maze[0].length;j++){
            for(int i=0;i<maze.length;i++){
                maze[i][j]=new Cell();
            }
        }
    }

    /* TODO: Create a new maze using the algorithm found in the writeup. */
    public static MyMaze makeMaze() {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter the number of rows for the maze: ");
        int userRow = myObj.nextInt();

        while(userRow<5 || userRow>20){   // Loop for user inputs that are out of bounds.
            System.out.println("Row is out of bounds, pick dimensions between 5 & 20 (inclusive)");
            System.out.println("Enter the number of rows for the maze: ");
            userRow = myObj.nextInt();

        }

        Scanner myObj2 = new Scanner(System.in);
        System.out.println("Input the number of columns for the maze: ");
        int userCol = myObj2.nextInt();

        while(userCol>20 || userCol<5){ // Loop for user inputs that are out of bounds.
            System.out.println("Columns is out of bounds, pick dimensions between 5 & 20 (inclusive)");
            System.out.println("Enter the number of columns for the maze: ");
            userCol=myObj2.nextInt();
        }


        Random random = new Random(); // will be non-inclusive
        int userStart = random.nextInt(userRow); // start row
        int userEnd = random.nextInt(userRow);  // end row
        MyMaze tester = new MyMaze(userRow, userCol,userStart,userEnd);

        Stack1Gen create=new Stack1Gen<>();   // stack to create maze

        create.push(tester.maze[tester.startRow][0]); // starting point
        tester.maze[tester.startRow][0].setVisited(true);

        while(!create.isEmpty()){
            Random choice = new Random();
            Cell current=null;  // current position will be held here

            for(int j=0;j<tester.maze[0].length;j++){
                for(int i=0;i<tester.maze.length;i++){
                    boolean rightTruth=false;  // true - meaning that you can visit this cell. false - otherwise  ---> right direction
                    boolean bottomTruth=false; // ---> bottom direction
                    boolean leftTruth=false;   // ---> left direction
                    boolean upTruth=false;    // ---> up direction


                    int arr[] = new int[4];  // A random element will be picked from array to help with randomly picking the next position.
                    int index=0; // keeps track of an available spot in array
                    int counter=0;  // how many elements there are in array

                    Object first = create.top();

                    if(tester.maze[i][j].equals(first)){
                        current=tester.maze[i][j];

                        if(i+1<tester.maze.length&&!tester.maze[i+1][j].getVisited()){ // first neighbor    Case #1: BOTTOM
                            bottomTruth=true;
                            arr[index]=1;
                            counter++;
                            index++;
                        }
                        if(i-1>=0&&!tester.maze[i-1][j].getVisited()){   // second neighbor   Case #2: UP
                            upTruth=true;
                            arr[index]=2;
                            counter++;
                            index++;
                        }
                        if(j+1<tester.maze[0].length&&!tester.maze[i][j+1].getVisited()){  // third neighbor     Case #3: RIGHT
                            rightTruth=true;
                            arr[index]=3;
                            counter++;
                            index++;
                        }
                        if(j-1>=0&&!tester.maze[i][j-1].getVisited()){  // fourth neighbor     Case #4: LEFT
                            leftTruth=true;
                            arr[index]=4;
                            counter++;
                            index++;
                        }

                        if(counter==0){  // Used for dead-end case--if there is a dead end, there will be no neighbors to go to, so the counter will be zero. But to prevent errors counter will be set to 1
                            counter=1;
                        }
                        counter=arr.length-counter;

                        //Line below is where source #1 was used:
                        int randomCase = choice.nextInt(arr.length-counter); // will randomly choose an element from array that are NOT null.


                        if(arr[randomCase]==1 && bottomTruth && i+1<tester.maze.length){  //    Case #1: Bottom
                            create.push(tester.maze[i+1][j]);
                            tester.maze[i+1][j].setVisited(true);
                            current.setBottom(false);

                        }
                        else if(arr[randomCase]==2&& upTruth && i-1>=0){   // Case #2: Up
                            create.push(tester.maze[i-1][j]);
                            tester.maze[i-1][j].setVisited(true);
                            tester.maze[i-1][j].setBottom(false);
                        }
                        else if(arr[randomCase]==3 && rightTruth&&j+1<tester.maze[0].length){  // Case #3: Right
                            create.push(tester.maze[i][j+1]);
                            tester.maze[i][j+1].setVisited(true);
                            current.setRight(false);
                        }
                        else if(arr[randomCase]==4 && leftTruth && j-1>=0){    // Case #4: left
                            create.push(tester.maze[i][j-1]);
                            tester.maze[i][j-1].setVisited(true);
                            tester.maze[i][j-1].setRight(false);
                        }
                        else{
                            create.pop();
                        }

                    }

                }
            }

        }
        // sets visited cells back to unvisited
        for(int j=0;j<tester.maze[0].length;j++){
            for(int i=0;i<tester.maze.length;i++){
                if(tester.maze[i][j].getVisited()){
                    tester.maze[i][j].setVisited(false);
                }
            }
        }

        return tester;
    }

    /* TODO: Print a representation of the maze to the terminal */
    public void printMaze() {

        String rep=""; // final output will be accumulated here

        for(int i=0;i< maze[0].length;i++){ // string representation for the top border of maze
            rep=rep+"|---";
        }
        rep=rep+"|"+"\n";

        String bottomRep="|";  // the representation for the bottom of each cell will be accumulated here

        maze[endRow][maze[0].length-1].setRight(false); // opening for end row

        String starting=""; // helps to deal with the opening of the starting row

        for(int j=0;j<maze.length;j++){
            for(int i=0;i<maze[0].length;i++){
                if(i==0){
                    starting="|";    // at the start of every row a border will be put at the very beginning.....
                }
                if(j==startRow && i==0 ){  // ...unless the row is the starting row, then no border will be put at the beginning of the row
                    starting=" "; // starting will be overwritten

                }
                rep=rep+starting;
                starting="";


                if(maze[j][i].getBottom() && !maze[j][i].getRight() && maze[j][i].getVisited()){ // Case #1 for visited
                    rep = rep+"  * ";
                    bottomRep=bottomRep +"---|";
                }
                if(maze[j][i].getBottom() && maze[j][i].getRight() && maze[j][i].getVisited()){  // Case #2 for visited
                    rep = rep+" * |";
                    bottomRep=bottomRep +"---|";
                }
                if(!maze[j][i].getBottom() && maze[j][i].getRight() && maze[j][i].getVisited()){ // Case #3 for visited
                    rep = rep+" * |";
                    bottomRep=bottomRep+"   |";
                }
                if(!maze[j][i].getBottom() && !maze[j][i].getRight() && maze[j][i].getVisited()){  // Case #4 for visited
                    rep=rep+"  * ";
                    bottomRep=bottomRep+"   |";
                }
                if(maze[j][i].getBottom() && maze[j][i].getRight() && !maze[j][i].getVisited()){ // Case #1 for unvisited
                    rep = rep+"   |";
                    bottomRep=bottomRep+"---|";

                }
                if(!maze[j][i].getBottom() && maze[j][i].getRight() && !maze[j][i].getVisited()){ //  Case #2 for unvisited
                    rep = rep+ "   |";
                    bottomRep=bottomRep+"   |";
                }
                if(maze[j][i].getBottom() && !maze[j][i].getRight() && !maze[j][i].getVisited()){ // Case #3 for unvisited
                    rep = rep+"    ";
                    bottomRep = bottomRep+"---|";
                }
                if(!maze[j][i].getRight() && !maze[j][i].getBottom()&&!maze[j][i].getVisited()){  // Case #4 for unvisited
                    rep = rep+"    ";
                    bottomRep=bottomRep+"   |";
                }
            }
            rep=rep+"\n";

            rep=rep+bottomRep+"\n";  // adds the bottom portion of the cell to the string rep.
            bottomRep="|";
        }
        System.out.println(rep);

    }

    /* TODO: Solve the maze using the algorithm found in the writeup. */
    public void solveMaze() {
        Q1Gen solver = new Q1Gen<>();

        solver.add(maze[startRow][0]);

        Object element=null; // current cell will be held here
        boolean exit=false;

        while(solver.length()!=0 || !exit){
            element=solver.remove();

            for(int j=0;j< maze[0].length;j++){
                for(int i=0;i<maze.length;i++){

                    if(maze[i][j].equals(element)){  // find current cell
                        maze[i][j].setVisited(true);

                        if(i==endRow && j== maze[0].length-1){
                            exit=true; // exits from while loop
                            break; // breaks from for loops
                        }

                        // Finds reachable neighbors and checks if move is allowed
                        if(i+1< maze.length&&!maze[i + 1][j].getVisited()&& !maze[i][j].getBottom()){  // BOTTOM
                            solver.add(maze[i+1][j]);
                        }
                        if(j+1< maze[0].length&&!maze[i][j+1].getVisited() && !maze[i][j].getRight()){  // RIGHT
                            solver.add(maze[i][j+1]);
                        }
                        if(i-1>=0 && !maze[i-1][j].getVisited() && !maze[i-1][j].getBottom()){  // LEFT
                            solver.add(maze[i-1][j]);
                        }
                        if(j-1>=0 &&!maze[i][j-1].getVisited() && !maze[i][j-1].getRight()){  // UP
                            solver.add(maze[i][j-1]);
                        }
                    }
                }
            }
        }
        printMaze();
    }

    public static void main(String[] args){
        // Introduction to game
        System.out.println("Welcome to the maze game!");
        System.out.println("The rules are simple, when asked to do so, please enter the dimensions for your maze");
        System.out.println("Enter any number that's between 5 and 20 (inclusive)");

        MyMaze test=makeMaze();
        test.solveMaze();
    }
}
