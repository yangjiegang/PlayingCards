package com.cards.www;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

public class Game {

	private ArrayList<Player> players;
	private Random random;
	private Integer pCount;
	private static Cards cards;
	private LinkedHashMap<Integer, String>allWeights;
	private Integer finishCount;

	public Game(Integer pCount) {
		this.pCount = pCount;
		random = new Random();
		Game.cards = new Cards();
		players = new ArrayList<Player>();
		for (int i = 0; i < pCount; i++) {
			players.add(new Player());
		}
		this.finishCount = 0;
		this.allWeights = new LinkedHashMap<Integer, String>();
	}
	
	public Integer getpCount() {
		return pCount;
	}

	public void setpCount(Integer pCount) {
		this.pCount = pCount;
	}

	public Integer getFinishCount() {
		return finishCount;
	}

	public void setFinishCount(Integer finishCount) {
		this.finishCount = finishCount;
	}
	
	public LinkedHashMap<Integer,String> getAllWeights() {
		return allWeights;
	}

	public void setAllWeights(LinkedHashMap<Integer, String> allWeights) {
		this.allWeights = allWeights;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public ArrayList<String> sndCards(Integer cardCount) {
		HashMap<Integer, String> cards = Game.cards.getCards();
		ArrayList<String> myCards = new ArrayList<String>();
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
	}
	
	public ArrayList<String> mySort(List<String>cList){
		HashMap<Integer, String> cards = Game.cards.getCards();
//		LinkedHashMap<Integer, String>cHashMap = new LinkedHashMap<Integer, String>();
		TreeMap<Integer, String>treeMap = new TreeMap<Integer, String>();
		for (String str : cList) {
			Set<Integer>keys = cards.keySet();
			for(Integer key: keys){
				if (str.equals(cards.get(key))) {
					treeMap.put(key, str);
				}
			}
//			System.out.println(treeMap);
		}
		ArrayList<String>list = new ArrayList<String>();
		for(Integer c : treeMap.keySet()){
			list.add(treeMap.get(c));
		}
//		System.out.println(list);
		return list;
	}

	public ArrayList<Player> startGame() {
		Game game = new Game(pCount);
		for (Player player : players) {
			player.setMyCards(game.sndCards(2));
		}
		System.out.println("start_game"+players);
		return this.players;
	}

	public void sortMyCards() {
		for (int i=0; i<players.size(); i++) {
			List<String> myCards = players.get(i).getMyCards();
/*			Integer size = list.size();
			String[] myCards = (String[]) player.getMyCards().toArray(new String[size]);
			Arrays.sort(myCards);*/
			players.get(i).setMyCards(this.mySort(myCards));
			System.out.println("Player No."+(i+1)+"'s cards are: "+this.mySort(myCards));
		}
		System.out.println("Sort finish!");
	}

	public void cmpCards() {

		for (int i = this.pCount - 1; i >= 0; i--) {
			List<String> oneTurn = new LinkedList<>();
			for (int j=0; j<players.size(); j++) {
				ArrayList<String> list = players.get(j).getMyCards();
				oneTurn.add(list.get(i + 2));
				oneTurn = this.mySort(oneTurn);
			}
			System.out.println(oneTurn);
			System.out.println("No."+ i +" turn, the biggest card is: "+oneTurn.get(oneTurn.size()-1));
		}

	}

	public static void main(String[] args) {
		Game game = new Game(3);
		System.out.println(game.sndCards(13));;
//		game.startGame();
//		game.sortMyCards();
//		game.cmpCards();
/*		List<String>cList = new ArrayList<>();
		cList.add("3");
		cList.add("J");
		cList.add("10");
		Game game = new Game(3);
		game.mySort(cList);*/
	}

}
