package de.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.memory.api.*;

public class MemoryModel implements IMemoryModel {
	
	public final static int amountOfCards = 16;
	
	private List<ICard> cardDeck = new ArrayList<ICard>();
	private IPlayer activePlayer;
	private Player player1;
	private Player player2;
	
	private List<IMemoryView> views =
            Collections.synchronizedList(new ArrayList<IMemoryView>());
	
	public MemoryModel() {
		player1 = new Player(1,this);
		player2 = new Player(2,this);
		initCardDeck();
		activePlayer = player1;
	}
	
	@Override
	public void startNewGame() {
		System.out.println("Start new game");
		initCardDeck();
		player1.resetPlayer();
		player2.resetPlayer();
		activePlayer = player1;
		//startNewGame on all views
		if (views.isEmpty())
            return;
        List<IMemoryView> viewsCopy;
        synchronized (views) {
            viewsCopy = new ArrayList<IMemoryView>(views);
        }
        for (IMemoryView v : viewsCopy)
            v.startNewGame();
        //update views
		observableStateChanged();
	}

	/**
	 * Inits card deck list with 16 random cards
	 */
	public void initCardDeck() {
		cardDeck.clear();
		PlayerCardDeck pcd = new PlayerCardDeck(amountOfCards);
		for(int i = 0; i < amountOfCards; i++) {
			cardDeck.add(pcd.getRandomCard());
		}
	}

	@Override
	public void addView(IMemoryView view) {
		views.add(view);
        view.update(this);		
	}

	@Override
	public void removeView(IMemoryView view) {
		views.remove(view);
	}
	
	public void observableStateChanged() {
        if (views.isEmpty())
            return;
        List<IMemoryView> viewsCopy;
        synchronized (views) {
            viewsCopy = new ArrayList<IMemoryView>(views);
        }
        for (IMemoryView v : viewsCopy)
            v.update(this);
    }
	
	public List<ICard> getCards() {
		return this.cardDeck;
	}

	@Override
	public ICard getCard(int index) {
		return cardDeck.get(index);
	}

	public void switchPlayer() {
		System.out.println("switch player");
		activePlayer = (activePlayer == player1) ? player2.setActive() : player1.setActive();
	}

	@Override
	public IPlayer getActivePlayer() {
		return activePlayer;
	}
	
	public IPlayer getPlayer(int number) {
		switch(number) {
		case 1 : return this.player1;
		case 2 : return this.player2;
		default: return null;
		}
	}
	
	public IPlayer getWinner() {
		if(player1 != null && player2!= null) {
			int p1Amount = player1.getAmountCardPairs();
			int p2Amount = player2.getAmountCardPairs();
			if(p1Amount + p2Amount == amountOfCards/2) {
				return p1Amount > p2Amount ? player1 : player2;
			}
		}
		return null;
	}
	
	
}
