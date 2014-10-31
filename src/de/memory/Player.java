package de.memory;

import java.util.ArrayList;
import java.util.List;

import de.memory.api.ICard;
import de.memory.api.IPlayer;

public class Player implements IPlayer {

	private int playerNumber;
	private MemoryModel model;
	private int amountCardPairs;
	private State state = null;
	
	//List of active selected player cards
	private List<ICard> selectedCards = new ArrayList<ICard>();
	
	public Player(int playerNumber,MemoryModel model) {
		this.playerNumber = playerNumber;
		this.amountCardPairs = 0;
		this.model = model;
		initialPlayerState();
	}
	
	private void initialPlayerState() {
		if(playerNumber == 1) 
			nextState(new IdleState());
		else
			nextState(new NotInTurn());
	}
	
	public boolean isActive() {
		return !(this.state instanceof NotInTurn);
	}
	
	public Player setActive() {
		this.state = new IdleState();
		return this;
	}

	public int getAmountCardPairs() {
		return this.amountCardPairs;
	}
	private void increaseCardPairAmount() {
		this.amountCardPairs +=1;
	}
	public int getPlayerNumber() {
		return this.playerNumber;
	}
	/**
	 * Resets state and amount of card pairs
	 * call on startNewGame()!
	 */
	public void resetPlayer() {
		this.amountCardPairs = 0;
		initialPlayerState();
	}
	
	private abstract class State {
		void flipCard(int index) {}
		void collectPair() {}
	};
	
	/**
	 * IdleState State
	 * @description Initial state for player. Wait for card selection
	 * and transit into Card1Selected state.
	 *
	 */
	private class IdleState extends State {
		@Override
		void flipCard(int index) {
			ICard card = model.getCard(index);
			card.flipCard();
			selectedCards.add(0, card); //save first card
			nextState(new Card1Selected());
		}
	}
	/**
	 * Card1Selected State
	 * @description Player has already selected one card and state handles selection 
	 * of the second one.
	 *
	 */
	private class Card1Selected extends State {
		@Override
		void flipCard(int index) {
			ICard card = model.getCard(index);
			if(card.isFrontVisible()) //old card selected
			{
		//debug code remove @release
				//selectedCards.remove(card);
				//card.flipCard();
				//nextState(new IdleState());
			}
			else // new card selected
			{
				selectedCards.add(1,card);
				if(selectedCards.get(0).equals(card)) { // cards are equal
					card.flipCard();
					nextState(new PairFound());
				}
				else { // different card selected
					card.flipCard();
					nextState(new WaitForCleanup());
				}
			}
		}
	}
	
	/**
	 * PairFound State
	 * @description Player found a valid card pair and is able to collect them.
	 * Cards are marked as unplayable and players card pair amount increases.
	 *
	 */
	private class PairFound extends State {
		@Override
		void collectPair() {
			increaseCardPairAmount();
			selectedCards.remove(1).setCardUnplayable(true);
			selectedCards.remove(0).setCardUnplayable(true); //cleanup and flip cards
			nextState(new IdleState()); //player remains active
		}
	}
	/**
	 * WaitForCleanup State
	 * @description Player has selected two different "colors" and must
	 * deselected them now. He remains in this state until all two cards have
	 * been deselected. Afterwards he's in IdleState and active player changes.
	 *
	 */
	private class WaitForCleanup extends State {
		@Override
		void flipCard(int index) {
			ICard card = model.getCard(index);
			if(selectedCards.contains(card)) {  
				if(selectedCards.size() > 1) { // 2 cards open
					selectedCards.remove(card);
					card.flipCard();
					nextState(new WaitForCleanup());
				} else { // 1 card open
					selectedCards.clear();
					card.flipCard();
					switchPlayer();//change player
				}
			}
		}
	}
	
	private class NotInTurn extends State {
	}
	
	private void switchPlayer() {
		model.switchPlayer();
		nextState(new NotInTurn());
	}
	
	private void nextState(State state) {
		System.out.println("player: "+this.playerNumber +": next state is "+state.getClass().getName());
        this.state = state;
        model.observableStateChanged();
    }

	@Override
	public void flipCard(int index) {
			state.flipCard(index); 
	}

	@Override
	public void collectPair() {
			state.collectPair();
	}
	
}
