import random

MAX_TRIES = 6 # Constant for maximum amount of tries

# main game loop
def main():
    categories = {} #dictionary for categories
    current_category = None
    guessed_letters = set() #set for guesses
    wrong_guesses = 0 
    display_state = [] # displays the spaces with the correct answers
    
    with open("words.txt") as f: #opens text file
    #add words from text file and add them to the dictionary
        for word in f:
            word = word.strip()
            
            if word == "":
                continue
            
            if word.endswith(":"):
                current_category = word[:-1] # hold current category
                categories[word[:-1]] = [] # set the current category as a key
            else:
                categories[current_category].append(word.lower()) # add all the words after the category
            
            
    while True:  # play again loop
        guessed_set = set()    # used for duplicate checking
        guessed_list = []      # used to display letters in the order guessed
        wrong_guesses = 0
        display_state = []

        print("Welcome to the Hangman Game!")
        print("Select a category:")

        menu = {} 
        # display the categories with numbered order
        for index, category in enumerate(categories, start=1): # iterable dictionary with a counter index
            print(f"{index}. {category}") # displays types of categories
            menu[index] = category # stores category options onto menu set

        while True: # loop for asking user input, and checking for invalid options
            try:
                choice = int(input("Enter the number of your category choice: "))
                if choice not in menu:
                    print("Invalid choice! Please select a valid category number.")
                    continue

                selected_category = menu[choice]
                break
            except ValueError:
                print("Invalid input! Please enter a valid number")
                
        #randomly select a word from the chosen category
        word = random.choice(categories[selected_category])

        print("You selected '" + selected_category + "'. The word is " + str(len(word)) + " letters long.")

        for _ in range(len(word)):
            display_state.append("_")

        # while loop for game
        while True:
            remaining = MAX_TRIES - wrong_guesses

            print("Current word: " + " ".join(display_state))

            if len(guessed_list) == 0:
                print("Tried letters:")
            else:
                print("Tried letters: " + ", ".join(guessed_list))

            print("Tries left: " + str(remaining))
            print("--------------------------------------------------------------------------------")

            if "_" not in display_state:
                print("Congratulations! You've guessed the word: " + word)
                break

            if remaining == 0:
                print("Game Over! The correct word was: " + word)
                break

            guess = input("Guess a letter: ").strip().lower()

            if len(guess) != 1 or not guess.isalpha():
                print("Invalid input. Please enter a single alphabetical character.")
                continue

            if guess in guessed_set:
                print("You have already guessed that letter.")
                continue

            guessed_set.add(guess)
            guessed_list.append(guess)

            if guess in word:
                print("Good guess! The letter '" + guess + "' is in the word.")
                for i in range(len(word)):
                    if word[i] == guess:
                        display_state[i] = guess
            else:
                print("Sorry, the letter '" + guess + "' is not in the word.")
                wrong_guesses += 1

        again = input("Play again? (y/n): ").strip().lower()
        if again not in ("y", "yes"):
            break
        print()
        
if __name__ == "__main__":
    main()