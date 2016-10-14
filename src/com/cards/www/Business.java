package com.cards.www;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

public class Business {

	public String referee(LinkedHashMap<Integer, String> allWeights,
			Integer pCount) {
		String winner = "who";
		if (allWeights.size() == pCount) {
			Set<Integer> weightSet = allWeights.keySet();
			Integer min = 0;
			Integer max = 0;
			for (Integer weight : weightSet) {
				if (weight < min) {
					min = weight;
				}
				if (weight > max) {
					max = weight;
					winner = allWeights.get(max);
				}
			}
		}
		return winner;
	}

	public Integer weight(ArrayList<String> myCards) {
		Cards cards = new Cards();
		Integer value = 0;
		Integer weight = 0;
		Integer interval = 0;

		for (String card : myCards) {
			value += cards.getCardValue(card);
		}
/*		System.out.println(value);
		if (value == 21) {
			weight = 5 * value;
		} else*/ if (value <= 21) {
			weight = 4 * value + 80;
		} else if (value > 21) {
			interval = value - 21;
			value *= 4;
			weight = value - 5 * interval;
		} else {
			weight = 0;
		}
		return weight;
	}

	public static void main(String[] args) {
		ArrayList<String> list = new ArrayList<>();
		list.add("10");
		list.add("A");
		list.add("A");
		list.add("5");
		list.add("5");
		Business business = new Business();
		Integer weight = business.weight(list);
		System.out.println(weight);
	}

}
