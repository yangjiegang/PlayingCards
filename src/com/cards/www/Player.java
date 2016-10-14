package com.cards.www;

import java.util.ArrayList;

public class Player {

	private String nickname;
	private ArrayList<String> myCards;
	private Byte status;//-1:off-line, 0:ready, 1:playing, 2:finish, 3:over

	public Byte getStatus() {
		return status;
	}
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public ArrayList<String> getMyCards() {
		return myCards;
	}

	public void setMyCards(ArrayList<String> myCards) {
		this.myCards = myCards;
	}

	public Player() {
		myCards = new ArrayList<String>();
		this.status = -1;
		this.nickname = "who";
	}
	
	@Override
	public String toString(){
		return this.nickname+", "+this.myCards+", "+this.status;
	}
	
}
