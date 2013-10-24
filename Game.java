import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

/**
 * Lexicant is a game where you play against a AI in order to form words.
 *
 * @author  Thomas Coe
 * @author  Chris Deese
 * @version 1.2 Oct 14, 2013
 *
 */
public class Game {
    /**
     * @param ALPHABET   The alphabet, used for choosing random letter
     * @param word       The string being built in the game
     * @param difficulty Stores the difficulty level selected by the user
     * @param allwords   Stores all the words imported from the text file
     * @param player     True if the player makes the first move
     * @param wordchoice An ArrayList storing the possible words the computer
     *                   can choose to build towards
     */
    static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String word;
    private static int difficulty = 10000000;
    private static String[] allwords;
    private static boolean player;
    private static ArrayList<String> wordchoice = new ArrayList<String>();

    /**
     * Imports the freebsd2.txt wordlist and sets it as
     * the allwords String array.
     *
     * Then, runs the start method to begin the game.
     */
    public static void main(String[] piratenoises) {
        allwords = importWordList("freebsd2.txt");
        start();
    }

    /**
     * Starts the game. Prompts the player for his/her name and difficulty level
     * Sets the difficulty level as a chance (1 in 6, 1 in 10, or 1 in
     * 10,000,000) that the computer will randomly add a letter to the string
     * being created instead of playing towards a word.
     */
    public static void start() {
        word = ""; //reset word to blank when game starts/restarts
        player = true; //default player to true when game starts/restarts
        Scanner input = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String name = input.next();
        System.out.println("Choose a difficulty (easy, medium, hard): ");
        String diff = input.next().toUpperCase();
        if (diff.equals("EASY")) {
            difficulty = 6;
        } else if (diff.equals("MEDIUM")) {
            difficulty = 10;
        } else if (diff.equals("HARD")) {
            difficulty = 10000000;
        }
        play(name);
    }

    /**
     * Imports a .txt file into a String array. Imports each word in all caps
     *
     * @param  path The path to the .txt file containing the words
     * @return      A String array containing each word in the .txt file
     */
    public static String[] importWordList(String path) {
        ArrayList<String> lines = new ArrayList<String>();
        try {
            Scanner file = new Scanner(new File(path));
            while (file.hasNextLine()) {
                lines.add(file.nextLine().trim());
            }
        } catch (IOException e) {
            System.err.println("Unable to read from file, \"" + path + "\".");
            System.exit(-1);
        }
        String[] list = new String[lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            list[i] = lines.get(i).toUpperCase();
        }
        return list;
    }

    /**
     * Plays the game. Decides if the computer or player should start. While the
     * string created (word) is not a whole word and is a part of a word, it
     * alternates prompting the computer and player for valid input. Prints
     * error messages if a whole word is created, or if the string created is
     * not part of a real word.
     *
     * After the game has ended, it prompts the user if they want to play again.
     * If so, it runs the start() method.
     */
    public static void play(String name) {
        if ((int) (2 * Math.random()) == 1) {
            player = false; //set computer as first player
            word += "" + ALPHABET.charAt((int) (26 * Math.random()));
            System.out.println("COMPUTER: " + word);
        } else { //have player input first character
            firstCharacter();
        }

        while (!isWholeWord() && isPartOfWord()) {
            if (player) {
                if (word.length() % 2 == 0) {
                    playerPlay(name);
                } else {
                    computerPlay();
                }
            } else {
                if (word.length() % 2 == 0) {
                    computerPlay();
                } else {
                    playerPlay(name);
                }
            }
        }

        if (isWholeWord()) {
            if ((player && word.length() % 2 != 0)
                    || (!player && word.length() % 2 == 0)) {
                System.out.println("You lose! Sorry, but " + word
                        + " is a word longer than 3 letters.");
            } else {
                System.out.println("You win! " + word + " is a word!");
            }
        }

        if (!isPartOfWord()) {
            if ((player && word.length() % 2 != 0)
                    || (!player && word.length() % 2 == 0)) {
                System.out.println("Oops! Looks like you're bluffing! No word "
                        + "exists that contains " + word + ".");
                System.out.println("The words you could have guessed were: "
                        + wordchoice);
            } else {
                System.out.println("You win! Looks like the computer was "
                        + "trying to bluff you.");
            }
        }

        Scanner input = new Scanner(System.in);
        System.out.println("Would you like to play again? (Y/N) ");
        String again = input.next().toUpperCase();
        if (again.equals("Y")) {
            start();
        }
    }

    /**
     * Prompts the player for a letter to start off the game with. Checks to
     * make sure the imput is a capital letter, and it is only one character
     * long.
     */
    public static void firstCharacter() {
        int length = word.length();
        while (length == word.length()) {
            Scanner input = new Scanner(System.in);
            System.out.println("Enter a letter: ");
            String newchar = input.next().toUpperCase();
            int newchar1 = newchar.charAt(0);
            if (newchar.length() == 1 && newchar1 > 64 && newchar1 < 91) {
                word += newchar;
                System.out.println(word);
            } else {
                System.out.println("Error. You must input one letter.");
            }
        }
    }

    /**
     * Checks if the current string is a whole word longer than three characters
     *
     * @return True if the string (word) is greater than three character and is
     *         a word found in the allwords array. False if the string is not a
     *         full word.
     */
    public static boolean isWholeWord() {
        if (word.length() > 3) {
            for (int i = 0; i < allwords.length; i++) {
                if (word.equals(allwords[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the current string is part of a real word.
     *
     * @return True if the string (word) is found in one of the words in the
     *         allwords array. False if the string is not contained within any
     *         word in the allwords array.
     */
    public static boolean isPartOfWord() {
        for (int i = 0; i < allwords.length; i++) {
            if (allwords[i].contains(word)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Handles the player input. Called when it is the players turn to provide
     * input upon the string. Keeps prompting a player for input until the
     * string length has changed. Sends the player's input off to validInput to
     * check if the input is valid.
     *
     * @param name The name of the player. Used to prompt the player for input.
     */
    public static void playerPlay(String name) {
        int length = word.length();
        while (length == word.length()) {
            Scanner input = new Scanner(System.in);
            System.out.println(name + ", add a letter: ");
            String newinput = input.next().toUpperCase();
            if (validInput(newinput)) {
                System.out.println(name + ": " + word);
            } else {
            System.out.println("Error. You must only add one letter to either"
                    + " the beginning or end.");
            }
        }
    }

    /**
     * Handles the computer input when it is the computers turn to add a letter
     * to the string. An arraylist is created that contains every word that
     * contains the current string. Then a random word from that string is
     * chosen. A guess is created with the current string plus the letter in the
     * word before the string, and another guess is created with the current
     * string plus the letter in the word after the string.
     *
     * The computer then randomly chooses a guess to play.
     *
     * The probability that the computer will just add a random letter is also
     * controlled here. There is a one in [difficulty] chance that the computer
     * will simply add a random letter to the end of the string.
     */
    public static void computerPlay() {
        wordchoice = new ArrayList<String>();
        for (int i = 0; i < allwords.length; i++) {
            if (allwords[i].contains(word)) {
                wordchoice.add(allwords[i]);
            }
        }
        int computerrand = (int) (Math.random() * difficulty);
        if (computerrand > 0) {
            int random = (int) (Math.random() * wordchoice.size());
            String nextguess = wordchoice.get(random);
            ArrayList<String> possibleguesses = new ArrayList<String>();
            int loc = nextguess.indexOf(word);
            if (loc > 0) {
                possibleguesses.add(nextguess.charAt(loc - 1) + word);
            }
            if (nextguess.length() > loc + word.length()) {
                possibleguesses.add(word
                        + nextguess.charAt(loc + word.length()));
            }
            int randguess = (int) (Math.random() * possibleguesses.size());
            word = possibleguesses.get(randguess);
            System.out.println("COMPUTER: " + word);
        } else {
            word += "" + ALPHABET.charAt((int) (26 * Math.random()));
            System.out.println("COMPUTER: " + word);
        }
        wordchoice = new ArrayList<String>();
        for (int i = 0; i < allwords.length; i++) {
            if (allwords[i].contains(word)) {
                wordchoice.add(allwords[i]);
            }
        }
    }

    /**
     * Checks to see if an input provided is valid. If the input is one letter
     * longer than the string, the input contains the string, and the input is
     * only capital letters, then the current string is set to input and true is
     * returned.
     *
     * @param input The possible input that is passed to the method to check
     * @return      True if the input is valid, false if the input is not valid
     */
    public static boolean validInput(String input) {
        if (input.length() - 1 != word.length()) {
            return false;
        } else if (input.indexOf(word) == -1) {
            return false;
        }
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) > 90 || input.charAt(i) < 65) {
                return false;
            }
        }
        word = input;
        return true;
    }

}
