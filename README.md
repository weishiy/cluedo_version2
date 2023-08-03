# SWEN225-Assignment-1

## Outline

We will create a prototype of the game "Hobby Detectives" based on the popular board game Cluedo. As a part of this assignment, we will also need to create CRC cards and use UML where appropriate to plan out our proposal and to aid us with the development of the prototype. 

We will also note that our code solution for this assignment will be used as the basis for assignment 2 later on.

## Specification

True to life, the following specification may be incomplete or ambiguous in parts and in such cases we will have to make reasonable assumptions which we should then explicitly state in the assignment reflection. We must not publicly ask for clarifications on rules, etc., as identifying and resolving open questions is part of the assignment.

## Objective

The game itself is a social deduction detective challenge to be played by 2-4 people using a 24x24 board divided into squares. The board itself has 5 estates and 4 2x2 non-interactable spots. Estates have walls and doors, with players being unable to move through walls but can move through doors to enter an estate or leave an estate respectively. 

When players first start the game, they will be asked how many players are playing in this round and the system will randomly allocate each player a Character. The system will then take one random Character, one random Weapon and one random Estate and declare that in secret to be the final answer of the game. Each player is then given cards that represent the remaining Characters, Estates and Weapons randomly. Each player will then take turns to roll a 2d6 to move on the board. The characters are currently: Lucilla, Bert, Malina, and Percy (names are given in player turn order). 

Once in an estate, players may guess a combination of one character, one estate and one weapon, with the estate being whatever estate that the player is currently in. The weapons that player may currently include in their guess is the following: Broom, Knife, Shovel, iPad, and Scissors. Once a player has made a guess, it then asks each player in order of their character to show one card that they have, if any, that the guess included. Once a card has been shown by any player to the player who made the guess, the player who made the guess has their turn end. 

At any point in the game, a player on their turn may propose a final solution. They may guess any Character, any Estate and any Weapon regardless of where they are currently on the board. At this point, the player may now be shown the cards declared as the solution by the program. If the player was correct, they win the game. If the player is incorrect, the player is now excluded from the game but is still required to show their cards when a player is making their guess. 

## User Interface

As per the specification of the assignment, all I/O for the assignment but be text-based and use the System.out and System.in.

## Basic Control Flow of Game

1. The program begins by asking how many players wish to participate.
   
2. One player is selected to start at random.
   
3. At the start of each turn, the program rolls two (virtual) dice to determine the move distance of the player who’s turn it is by using the sum of the dice values. The current player then moves their token to a desired spot on the grid.
   
4. Once a player has moved, they are presented with the option of making a guess or a solve attempt. All rules of the games must be enforced at all times, e.g., only guesses that involve the estate the current player is in, should be permitted.
  
5. The program then repeats steps 3–5 for the next player, unless a player has won or all players have been eliminated due to incorrect solve attempts


