package de.memory;


import de.memory.api.ICard;

public class PlayerCard implements ICard {
	
	private boolean frontVisible;
	private boolean unplayable; 
	private final String frontColor;

	public PlayerCard(String frontColor) {
		this.frontColor = frontColor;
		this.frontVisible = false;
		this.unplayable = false;
	}
	
	public String getFrontColor() {
		return this.frontColor;
	}
	
	public void setFrontVisible(boolean value) {
		this.frontVisible = value;
	}
	
	/**
	 * @param value card unplayable yes or no. If yes: flipCard() also called
	 */
	public void setCardUnplayable(boolean value) {
		if(value == true) {
			flipCard();
		}
		this.unplayable = value;
	}
	public boolean isCardUnplayable() {
		return this.unplayable;
	}
	
	public void flipCard() {
		this.frontVisible = !this.frontVisible;
	}
	public boolean isFrontVisible() {
		return this.frontVisible;
	}
	@Override
	public boolean equals(Object o) {
		if(o instanceof PlayerCard ) {
			return ((PlayerCard)o).getFrontColor().equals(this.frontColor);
		}
		else
			return false; // or exception
	}
}
