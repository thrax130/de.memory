package de.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.memory.api.ICard;

/**
 * PlayCardDeck class
 * @author edmundsenkleiter
 * @description Can handle card decks up to 16 cards! Creates list of card pairs with individual colors
 */
public class PlayerCardDeck {
	private final String[] cardColors = {"green","blue","red","yellow","orange","brown","pink","purple"};
	private List<ICard> cards = new ArrayList<ICard>();
	
	/**
	 * Constructor of PlayerCardDeck
	 * @param amountOfCards must be even and <= 16
	 */
	public PlayerCardDeck(int amountOfCards) {
		int color = 0;
		for(int x = 0; x < amountOfCards; x+=2) { //makes pairs
			cards.add(new PlayerCard(cardColors[color]));
			cards.add(new PlayerCard(cardColors[color]));
			color +=1;
		}
	}
	
	/**
	 * getRandomCard method
	 * @return random card from deck and removes it from own array
	 */
	public ICard getRandomCard() {
		Random rand = new Random();
		if(cards.size() > 0) {
			int randomNum = rand.nextInt(cards.size());
			return cards.remove(randomNum);
		}
		return null;
	}
}
