import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Stack;



//Isaac Withrow
public class ApartmentList {

  //the main method reads the file, sets up the arrays, and calls the socialNetwork() method
  public static void main(String[] args) {
    int argsLength = args.length;
    if (argsLength == 1) {
      String argument = args[0];
      argument = argument.toUpperCase();
      File file = new File("dictionary.txt");
      String [][] a = new String[15][100000];
      //this array will be used to keep track of an increment variable for the 2-D array
      int [] size = new int[16];
      //this loop sets up the size array to start with all zeros as
      for (int k = 0; k < 16; k++) {
        size[k] = 0;
      }


      try {
        Scanner scanner = new Scanner(file);
        int i = 0;
        /*while the txt file is not empty, I want to add the relevant word to the appropriate spot in the 2-D array
         * ex: the first word with a length of 4, will go into the spot [4][0] where the 4 is from the length of the word
         *  and the 0 is from the word being the first of that length */
        while (scanner.hasNextLine()){
          String expression = scanner.nextLine();
          int length = expression.length();
          int location = size[length];
          a[length-1][location] = expression;
          size[length] = size[length] + 1;
          i++;
        }

        /* after I put the words from the txt file, into a logical 2-D array, it will be easier and faster
         * to find the number of friends each word has because each word will only need to be compared to a
         * smaller subset of the whole dictionary */
      }
      catch (FileNotFoundException e) {
        e.printStackTrace();
      }


      /*now that the arrays are set up, I want to do the calculations for the edit distance of "LISTY", assuming "LISTY"
       * is the first input by the user */
      int answer = socialNetwork(argument, size, a);
      System.out.println(answer);
    }
    /*if the user does not input anything or inputs multiple strings, remind them to retry to the program using
     * a single string as input */
    else {
      System.out.println("Please enter a single string as an input");
    }
  }



  /* calculates the social network size for a word by finding all friends that are withing the edit distance of the
   * parameter string, then pushing all of its friends that have not been checked yet to a stack of words to be checked.
   * As long as the stack is not empty, repeat with the top word in the stack */
  public static int socialNetwork(String string, int[] size, String[][] a) {
    int counter = 0;
    String[] alreadyUsedList = new String[100000];
    int manyItems = 0;
    Stack<String> wordsToCheckStack = new Stack<String>();

    wordsToCheckStack.push(string);

    //as long as the stack is not empty, repeat with the next word in the stack
    while (!wordsToCheckStack.isEmpty()) {
      String wordToCheck = wordsToCheckStack.pop();
      int length = wordToCheck.length();
      /*loop through a small subset of the entire dictionary. For a word of length 5, we only need to look at
       * words with length 4, 5, and 6 because those are the only words that can have an edit distance of one.
       * This allows us to only look at a small portion of the dictionary instead of the whole thing every single time */
      for (int i = length - 2; i <= length; i++) {
        int wordSize = size[i+1];
        for (int k = 0; k < wordSize; k++) {
          String wordToCompare = a[i][k];
          if (isEditDistanceOne(wordToCheck, wordToCompare) && !alreadyUsed(wordToCompare, alreadyUsedList, manyItems)) {
            alreadyUsedList[manyItems] = wordToCompare;
            manyItems++;
            wordsToCheckStack.push(wordToCompare);
            counter++;
          }
        }
      }
    }
    return counter;
  }



  // a method that takes two strings and returns true if the edit distance between the two is one
  public static boolean isEditDistanceOne(String stringOne, String stringTwo) {

    int oneLength = stringOne.length();
    int twoLength = stringTwo.length();


    int countOne = 0;
    int countTwo = 0;
    int differenceCounter = 0;

    //while the strings have not been looped through, increment the differenceCounter if the two strings are different
    while (countOne < oneLength && countTwo < twoLength) {
      if (stringOne.charAt(countOne) != stringTwo.charAt(countTwo)) {
        differenceCounter = differenceCounter + 1;
        //if the number of differences is already greater than one, the two strings can't have an edit distance of 1
        if (differenceCounter > 1) {
          return false;
        }
        //if the first string is longer than the second, only increment the counter for the first one
        if (oneLength > twoLength) {
          countOne = countOne + 1;
        }
        //if the second string is longer than the first, only increment the counter for the second one
        else if (twoLength > oneLength) {
          countTwo = countTwo +1;
        }
        //else they are the same length so increment both counters equally
        else {
          countOne = countOne + 1;
          countTwo = countTwo + 1;
        }
      }
      /*if this is reahced, the strings are the same at the character that is being evaluated
       * so we want to increment the counter for each string but not the differenceCounter */
      else {
        countOne = countOne + 1;
        countTwo = countTwo + 1;
      }
    }
    /*if one of the strings has not reached the end, add one to the differenceCounter as there is obviously a difference
     * at that location */
    while (countOne < oneLength || countTwo < twoLength) {
      if (countOne < oneLength) {
        differenceCounter++;
        countOne++;
      }
      else {
        differenceCounter++;
        countTwo++;
      }
    }


    if (differenceCounter == 1) {
     return true;
    }
    else {
     return false;
    }


  }

  /* a boolean method that will return true if the string has already had its freinds searched for */
  public static boolean alreadyUsed(String string, String[] alreadyUsedList, int manyItems) {
    boolean alreadyUsed = false;
    for (int i = 0; i < manyItems; i++) {
      String wordToCompare = alreadyUsedList[i];
      if (string.equals(wordToCompare)) {
        alreadyUsed = true;
       }
    }

    return alreadyUsed;

  }







}
