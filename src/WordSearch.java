/**
 * Created by Harrison on 4/8/2015.
 */

/**
 IIIII       GGGGGGGGGGGGGGGG             NNNNNNNNNN         NNNNNN
 IIIII       GGGGGGGGGGGGGGGG             NNNNNN NNNN        NNNNNN
 IIIII       GGGGGGGGGGGGGGGG             NNNNNN  NNNN       NNNNNN
 IIIII       GGGGGGG                      NNNNNN   NNNN      NNNNNN
 IIIII       GGGGGGG                      NNNNNN    NNNN     NNNNNN
 IIIII       GGGGGGG     GGGGGGGGG        NNNNNN     NNNN    NNNNNN
 IIIII       GGGGGGG     GGGGGGGGG        NNNNNN      NNNN   NNNNNN
 IIIII       GGGGGGG       GGGGGGG        NNNNNN       NNNN  NNNNNN
 IIIII       GGGGGGGGGGGGGGGGGGGGG        NNNNNN        NNNN NNNNNN
 IIIII       GGGGGGGGGGGGGGGGGGGGG        NNNNNN         NNNNNNNNNN

 */


import java.io.FileNotFoundException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WordSearch {

    public static void main(String[] args) throws Exception {

        //declare string line to record each line in text file
        String gridLine;
        //count characters
        int chars = 0;

        //try catch block to locate the file on the system.
        //You need to catch FileNotFoundException, you could instead just write Exception
        //but then you'd catch every exception
        try {
            //arg[0] takes the argument for the file location
            //if you don't have the argument set-up, use the direct path e.g. "C:\\Users\\Harrison\\Desktop\\word-search.txt".
            fileScanner = new Scanner(new File(args[0]));
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(1);
        }

        //while there is something in the next line in the text file make it equal to variable gridLine
        while (true) {
            gridLine = fileScanner.nextLine();
            //if the nextLine is empty then end the while loop
            if (gridLine.equals("")) {
                break;
            }
            //add each line to showGrid
            displayGrid.add(gridLine);
            //add line and trim the line and remove spaces to help the program parse
            letterGrid.add(gridLine.trim().replaceAll("\\s+", ""));
        }


        //print the puzzle to console
        int rows = rowCount();
        for (int r = 0; r < rows; r++) {
            if(r<10){
                System.out.print(r+ "    ");
            } else {System.out.print(r+ "   "); }
            System.out.println(displayGrid.get(r));
        }

        //print blank line to create space
        System.out.println();

        //displays the way degrees should be read while reading the solution for each word
        degrees();

        //search and find the words in the puzzle
        searchWords();

        //// OPTOINAL /////
        //print out list of words
        //System.out.println(wordList.toString());

        //Count all the characters for all the words that need to be found
        //this will compare the amount of coordinates to the amount of characters
        for (int i = 0; i < wordList.size(); i++) {
            String word = wordList.get(i);
            int wordLength = word.length();
            chars = chars + wordLength;
        }

        //make sure we have found all the coordinates for all the characters
        //then print the master solution
        if (masterList.size() == chars) {
            printSolution(masterList);
        }

    }



    //declare grid to parse through letters
    private static ArrayList<String> letterGrid = new ArrayList<String>();

    //declare grid to show on the console
    private static ArrayList<String> displayGrid = new ArrayList<String>();

    //declare the master list of coordinates to display final solution
    private static List<Coordinates> masterList = new ArrayList<Coordinates>();

    //declare the list of words that need to be found
    private static List<String> wordList = new ArrayList<String>();

    //declare scanner file
    private static Scanner fileScanner;

    //count the rows in the grid
    public static int rowCount() {
        return letterGrid.size();
    }

    //count the columns in each row
    public static int colCount() {
        return letterGrid.get(0).length();
    }

    //function to search for the word on the puzzle grid
    //nested for loops. Loop through each row, within each row go through each column
    //while going though each row check check the previous, current, and next row
    //and then check each column in the row
    public static void searchWords() {
        int rows = rowCount();
        int columns = colCount();
        while (fileScanner.hasNext()) {
            String word = fileScanner.next();
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    for (int dr = -1; dr <= 1; dr++) {
                        for (int dc = -1; dc <= 1; dc++) {
                            locate(word, r, c, dr, dc);
                        }
                    }
                }
            }
        }
    }

    //class to take in the row and column coordinates to find the characters
    static class Coordinates {
        public int row, column;

        Coordinates(int row, int column) {
            this.row = row;
            this.column = column;
        }

        //override the equals bool so we can check coord to coord
        @Override
        public boolean equals(Object object) {
            Coordinates coord = (Coordinates) object;
            return coord.row == row && coord.column == column;
        }

        //override the toString() function to print the row and column coord
        @Override
        public String toString() {
            return ("row:" + this.row + " col: " + this.column);
        }
    }

    //print the word found on the puzzle grid
    //if the letter does not belong to the word that was found
    //then replace that character with a period.
    public static void printSolution(List<Coordinates> coords) {
        int rows = rowCount();
        int cols = colCount();

        for (int r = 0; r < rows; r++) {
            System.out.println();
            for (int c = 0; c < cols; c++) {
                if((coords.contains(new Coordinates(r, c)))){
                    System.out.print(letterGrid.get(r).charAt(c) + " ");
                } else {
                    System.out.print('.' + " ");
                }
                //System.out.print((coords.contains(new Coordinates(r, c)) ? letterGrid.get(r).charAt(c) : '.') + " ");
            }
        }
    }

    //function to locate the word in the grid
    public static void locate(String word, int r, int c, int dr, int dc) {
        int rows = rowCount();
        int columns = colCount();
        List<Coordinates> coords = new ArrayList<Coordinates>();
        String direction;

        //loop through the word's char length to test all
        //possible locations to find the word
        for (int j = 0; j < word.length(); j++) {
            //the first character is found we check how many
            //rows or columns after the first letter we need to go
            //and in what direction
            int findCharRow = r + (j * dr);
            int findCharCol = c + (j * dc);

            //determine if we are still on the puzzle grid.
            //if we run out of rows or columns we return the statement.
            if ((findCharRow < 0) || (findCharRow >= rows) || (findCharCol < 0) || (findCharCol >= columns)) {
                return;
            }

            //declare the puzzle grid letter and the word letter
            char gridLetter = letterGrid.get(findCharRow).charAt(findCharCol);
            char wordLetter = word.charAt(j);

            //append coordinate class to list
            coords.add(new Coordinates(findCharRow, findCharCol));

            //append to the master list of coordinates for the word search
            //only append if the amount of coordinates equals the length of the word
            if (coords.size() == word.length()) {
                masterList.addAll(coords);
                wordList.add(word);

                /*  /////// OPTIONAL ///////////////
                    //print current coordinate solution
                    System.out.println(coords.toString());

                    //print to see where each word is individually on the puzzle grid
                    PrintSolution(coords);

                    //print the master list of coordinates as it grows
                    System.out.println(nick.toString());
                */
            }

            //if there are no more matches end the search
            if (gridLetter != wordLetter)
                return;
        }

        //if statements to determine which lines to print to tell the user where the word is
        //and what direction the user should move.
        if (dr == 0 && dc == -1) {
            direction = "stay in the same line and move backwards. 180 degrees";
            System.out.println(word + " starts at row " + r + " and column " + c + ". Direction: " + direction);
        }

        if (dr == 0 && dc == 1) {
            direction = "stay in the same line and move forward. 0 degrees";
            System.out.println(word + " starts at row " + r + " and column " + c + ". Direction: " + direction);
        }


        if (dr == 1 && dc == -1) {
            direction = "move down and backawards at a diagonal. 135 degrees";
            System.out.println(word + " starts at row " + r + " and column " + c + ". Direction: " + direction);
        }

        if (dr == 1 && dc == 0) {
            direction = "move directly down. 90 degrees";
            System.out.println(word + " starts at row " + r + " and column " + c + ". Direction: " + direction);
        }

        if (dr == 1 && dc == 1) {
            direction = "move down and forward at a diagonal. 45 degrees";
            System.out.println(word + " starts at row " + r + " and column " + c + ". Direction: " + direction);
        }

        if (dr == -1 && dc == 0) {
            direction = "move directly up. 270 degrees";
            System.out.println(word + " starts at row " + r + " and column " + c + ". Direction: " + direction);
        }

        if (dr == -1 && dc == 1) {
            direction = "move up and forward diagonal. 315 degrees";
            System.out.println(word + " starts at row " + r + " and column " + c + ". Direction: " + direction);
        }

        if (dr == -1 && dc == -1) {
            direction = "move up and backward at a diagonal. 225 degrees";
            System.out.println(word + " starts at row " + r + " and column " + c + ". Direction: " + direction);
        }

    }

    //function to display the way I am using degrees to illustrate the direction I want
    //the user to move to find the answer
    public static void degrees(){
        System.out.println("    225      270       315");
        System.out.println("      -       |        -");
        System.out.println("        -     |     -");
        System.out.println("          -   |  -");
        System.out.println("180-----------B-----------0 degrees");
        System.out.println("           -  |   -");
        System.out.println("        -     |      -");
        System.out.println("     -        |        -");
        System.out.println("   135       90         45");
        System.out.println();
    }

}
