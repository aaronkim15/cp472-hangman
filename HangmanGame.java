package a2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Random;


public class HangmanGame {

    final static int MAX_TRIES = 6;

    public static void main(String[] args) throws FileNotFoundException {

        LinkedHashMap<String, ArrayList<String>> categories = new LinkedHashMap<>();
        String current_category = "";

        File file = new File("words.txt");
        Scanner fileScanner = new Scanner(file);
        Scanner inputScanner = new Scanner(System.in);

        // Load categories from file (preserves order)
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine().strip();

            if (line.isEmpty()) {
                continue;
            }

            if (line.endsWith(":")) {
                current_category = line.substring(0, line.length() - 1);
                categories.put(current_category, new ArrayList<>());
            } else {
                categories.get(current_category).add(line.toLowerCase());
            }
        }
        fileScanner.close();

        // Play again loop
        while (true) {

            System.out.println("Welcome to the Hangman Game!");
            System.out.println("Select a category:");

            // Convert keys to list 
            ArrayList<String> categoryList = new ArrayList<>(categories.keySet());

            for (int i = 0; i < categoryList.size(); i++) {
                System.out.println((i + 1) + ". " + categoryList.get(i));
            }

            // Category selection loop
            String choice = "";
            while (true) {
                System.out.print("Enter the number of your category choice: ");
                String userInput = inputScanner.nextLine();

                try {
                    int input = Integer.parseInt(userInput);

                    if (input < 1 || input > categoryList.size()) {
                        System.out.println("Invalid choice! Please select a valid category number.");
                        continue;
                    }

                    choice = categoryList.get(input - 1);
                    break;

                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Please enter a valid number");
                }
            }

            ArrayList<String> words = categories.get(choice);
            Random rand = new Random();
            String word = words.get(rand.nextInt(words.size()));

            System.out.println("You selected '" + choice + "'. The word is " + word.length() + " letters long.");

            int wrongGuesses = 0;
            char[] displayState = new char[word.length()];
            for (int i = 0; i < displayState.length; i++) {
                displayState[i] = '_';
            }

            HashSet<Character> guessedSet = new HashSet<>();
            ArrayList<Character> guessedList = new ArrayList<>();

            // Game loop
            while (true) {
                int remaining = MAX_TRIES - wrongGuesses;

                // Display current word
                System.out.print("Current word: ");
                for (int i = 0; i < displayState.length; i++) {
                    System.out.print(displayState[i]);
                    if (i < displayState.length - 1) System.out.print(" ");
                }
                System.out.println();

                // Display guessed letters
                if (guessedList.size() == 0) {
                    System.out.println("Tried letters:");
                } else {
                    System.out.print("Tried letters: ");
                    for (int i = 0; i < guessedList.size(); i++) {
                        System.out.print(guessedList.get(i));
                        if (i < guessedList.size() - 1) System.out.print(", ");
                    }
                    System.out.println();
                }

                System.out.println("Tries left: " + remaining);
                System.out.println("--------------------------------------------------------------------------------");

                // Check win
                boolean won = true;
                for (char c : displayState) {
                    if (c == '_') {
                        won = false;
                        break;
                    }
                }
                if (won) {
                    System.out.println("Congratulations! You've guessed the word: " + word);
                    break;
                }

                // Check lose
                if (remaining == 0) {
                    System.out.println("Game Over! The correct word was: " + word);
                    break;
                }

                // Guess input
                System.out.print("Guess a letter: ");
                String guessInput = inputScanner.nextLine().strip().toLowerCase();

                if (guessInput.length() != 1 || !Character.isLetter(guessInput.charAt(0))) {
                    System.out.println("Invalid input. Please enter a single alphabetical character.");
                    continue;
                }

                char guess = guessInput.charAt(0);

                if (guessedSet.contains(guess)) {
                    System.out.println("You have already guessed that letter.");
                    continue;
                }

                guessedSet.add(guess);
                guessedList.add(guess);

                if (word.indexOf(guess) >= 0) {
                    System.out.println("Good guess! The letter '" + guess + "' is in the word.");
                    for (int i = 0; i < word.length(); i++) {
                        if (word.charAt(i) == guess) {
                            displayState[i] = guess;
                        }
                    }
                } else {
                    System.out.println("Sorry, the letter '" + guess + "' is not in the word.");
                    wrongGuesses++;
                }
            }

            // Play again?
            System.out.print("Play again? (y/n): ");
            String again = inputScanner.nextLine().strip().toLowerCase();
            if (!(again.equals("y") || again.equals("yes"))) {
                break;
            }

            System.out.println();
        }

        inputScanner.close();
    }
}