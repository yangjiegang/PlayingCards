package com.cards.www;

import java.util.HashMap;

public class Cards {

	private HashMap<Integer, String> cards;

	protected Cards() {
		cards = new HashMap<Integer, String>();
//		random = new Random();
		for (int i = 0; i < 13; i++) {
			this.cards.put(i, "");
		}
		// Set<Integer> key = cards.keySet();
		for (int j = 1; j < 10; j++) {
			cards.put(j, String.valueOf(j + 1));
		}
		cards.put(0, "A");
		cards.put(10, "J");
		cards.put(11, "Q");
		cards.put(12, "K");
//		System.out.println(cards);
	}

	public HashMap<Integer, String> getCards() {
		return cards;
	}

	public void setCards(HashMap<Integer, String> cards) {
		this.cards = cards;
	}
	
/*	public List<String> sndCards(Integer cardCount) {
		List<String> myCards = new ArrayList<String>();
		HashSet<Integer> set = new HashSet<Integer>();
		for (int i = 0; i < cardCount; i++) {
			Integer e = random.nextInt(13);
			Boolean flag = set.add(e);
			if (!flag) {
				i -= 1;
			}
		}
		Set<Integer> keys = cards.keySet();
		for (Integer e : set) {
			for (Integer key : keys)
				if (e.equals(key)) {
					myCards.add(cards.get(key));
				}
		}
		return myCards;
	}*/
	public Integer getCardValue(String card){
		Integer value = 0;
		for (Integer v: cards.keySet()) {
			if (card.equals( cards.get(v) )) {
				value = v+1;
			}
		}
		return value;
	}
	
	public static void main(String[] args) {
		Cards cards = new Cards();
		Integer cardValue = cards.getCardValue("10");
		System.out.println(cardValue);
	}
	
}
