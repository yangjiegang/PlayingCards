package com.cards.www;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/*import org.apache.struts2.json.JSONException;
 import org.apache.struts2.json.JSONUtil;*/

public class Service extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Game game;
	private Business business;
	private String winner;
	private PrintWriter pWriter;
	private ArrayList<Player> players;
	private TreeMap<Long, HashMap<String, String>>chatDataMap;
	
//	private ArrayList<Player> othersData;
	
	@Override
	public void init(){
		if (this.getServletContext().getAttribute("game") == null) {// if not initial yet
			game = new Game(4);
			business = new Business();
			players = new ArrayList<Player>();
			chatDataMap = new TreeMap<Long, HashMap<String, String>>();
			game.setPlayers(players);
			winner = "who";
			try {
				pWriter = new PrintWriter(winner);//temperature way to avoid null point
			} catch (IOException e) {
				e.printStackTrace();
			}
			ServletContext appCtx = this.getServletContext();
			appCtx.setAttribute("game", game);
			appCtx.setAttribute("business", business);
			appCtx.setAttribute("pWriter", pWriter);
			appCtx.setAttribute("chatDataMap", chatDataMap);
			// appCtx.setAttribute("players", players);
		}
	}
	
/*	public Service() {
		System.out.println("service constructor");
	}*/

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		// String path = request.getServletPath();
		String pathinfo = request.getPathInfo();
		if ("/gameInit".equals(pathinfo)) {
			gameInit(request, response);
		} else if ("/gameStart".equals(pathinfo)) {
			gameStart(request, response);
		} else if ("/more".equals(pathinfo)) {
			moreCard(request, response);
		} else if ("/finish".equals(pathinfo)) {
			finish(request, response);
		} else if ("/getOthersData".equals(pathinfo)) {
			getOthersData(request, response);
		} else if("/getMsg".equals(pathinfo)){
			getChatData(request, response);
		} else if("/sndMsg".equals(pathinfo)){
			sndChatData(request, response);
		} else if("/valiNick".equals(pathinfo)){
			valiNick(request, response);
		} else {
			System.out.println("404:"+pathinfo);
		}
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
	}

	public void gameInit(HttpServletRequest request,
			HttpServletResponse response) {
		
		String nickname = request.getParameter("nickname");
			
		request.getSession().setAttribute("nickname", nickname);//save nickname in session for validation

		Player player = new Player();
		player.setNickname(nickname);
		player.setStatus((byte) 1);
		player.setMyCards(game.sndCards(2));
		ArrayList<Player> players = game.getPlayers();
		players.add(player);
		game.setPlayers(players);

		stdOut(response, nickname);//just for validation

	}

	public void gameStart(HttpServletRequest request,
			HttpServletResponse response) {
		
		String nickname = request.getParameter("nickname");
		Player mePlayer = findPlayer(nickname);
		
		Gson gson = new Gson();
		String jsonStr = gson.toJson(mePlayer);

		stdOut(response, jsonStr);
		
	}

	public void moreCard(HttpServletRequest request,
			HttpServletResponse response) {
		
		String myName = request.getParameter("nickname");
		
		String nickname = (String) request.getSession().getAttribute("nickname");
		if (myName.equals(nickname)) {
			Game game = (Game) this.getServletContext().getAttribute("game");
			Player me = findPlayer(myName);
			if (me.getStatus() == (byte) 1) {
				if (me.getMyCards().size()<5) {
					String moreCard = game.sndCards(1).get(0);// deal with the new card at back-end
					me.getMyCards().add(moreCard);
					
					stdOut(response, moreCard);
				} else {
					System.out.println("Error: Full cards!");
					return;
				}

			} else {
				System.out.println("you have submited finish already!");
				return;
			}
		}

	}

	public void getOthersData(HttpServletRequest request,
			HttpServletResponse response) {
		String myName = request.getParameter("nickname");
		
		String nickname = (String) request.getSession().getAttribute("nickname");
		if (myName.equals(nickname)) {
			Game game = (Game) this.getServletContext().getAttribute("game");
			Player me = findPlayer(myName);
			if (me!=null) {
				Gson gson = new Gson();
				if ( me.getStatus() == (byte) 1 || me.getStatus() == (byte) 2 && game.getFinishCount() != game.getpCount() ) {
					@SuppressWarnings("unchecked")
					ArrayList<Player> othersData = (ArrayList<Player>) players.clone();
					othersData.remove(me);
					String jsonString = gson.toJson(othersData);
					if (game.getFinishCount() != game.getpCount()) {//combine with up					
						stdOut(response, jsonString);
					} else {
						return;
					}
				} else if ( me.getStatus() == (byte) 2 && game.getFinishCount() == game.getpCount() ) {
						String winner = (String) this.getServletContext().getAttribute(
								"winner");
						winner = gson.toJson(winner);
						stdOut(response, "winner: "+winner);
						me.setStatus((byte) 3);//-1:off-line, 0:ready, 1:playing, 2:finish, 3:over
				} else {
					return;
				}
			} else {
				return;
			}
		}
	}

	public void finish(HttpServletRequest request, HttpServletResponse response) {
		String myName = request.getParameter("nickname");
		
		String nickname = (String) request.getSession().getAttribute("nickname");
		if (myName.equals(nickname)) {
			Game game = (Game) this.getServletContext().getAttribute("game");// package_package_package_package_package_package_
			Player me = findPlayer(myName);
			System.out.println(game.getPlayers());
			me.setStatus((byte) 2);//-1:off-line, 0:ready, 1:playing, 2:finish, 3:over
			game.setFinishCount(game.getFinishCount() + 1);
			Business business = (Business) this.getServletContext().getAttribute(
					"business");
			Integer weight = business.weight(me.getMyCards());
			game.getAllWeights().put(weight, myName);// one of player is finish
	//		System.out.println(game.getAllWeights());
			if (game.getFinishCount() == game.getpCount()) {// all players are finish
				System.out.println("game.getAllWeights():"+game.getAllWeights());
				winner = business.referee(game.getAllWeights(), game.getpCount());
				this.getServletContext().setAttribute("winner", winner);
			} else {
				System.out.println(me+" finish");
				return;
			}
		}
	}
	
	public void getChatData(HttpServletRequest request, HttpServletResponse response){

//		LinkedHashSet<Long>pushMsgTimeSet = new LinkedHashSet<>();
		String myName = request.getParameter("uname");
		
		String nickname = (String) request.getSession().getAttribute("nickname");
		if (myName.equals(nickname)) {
			@SuppressWarnings("unchecked")
			TreeMap<Long, HashMap<String, String>>chatDataMap = (TreeMap<Long, HashMap<String, String>>) this.getServletContext().getAttribute("chatDataMap");
			if (chatDataMap.size()!=0 && notEmpty(myName)) {
				SimpleDateFormat sdf = new SimpleDateFormat();
	//			Long getMsgTime = new Date().getTime();
	//			Long[]tmpArrayLongs = pushMsgTimeSet.toArray(new Long[pushMsgTimeSet.size()]);
	//			Long prevMsgTime = tmpArrayLongs[tmpArrayLongs.length];
				
				Long startTime = chatDataMap.lastKey()-1000*60;
//				System.out.println(chatDataMap+","+ startTime);
				
	//			SortedMap<Long, HashMap<String, String>> subMap = null;
	//			TreeMap<Long, HashMap<String, String>> dataMap = new TreeMap<>();
				ArrayList<HashMap<String, String>> msgList = new ArrayList<>();
				for (Long sndMsgTime : chatDataMap.keySet()) {
					if (startTime<=sndMsgTime) {
	//					TreeMap<Long, HashMap<String, String>>getMsgMap = (TreeMap<Long, HashMap<String, String>>) chatDataMap.subMap(sndMsgTime, chatDataMap.lastKey());
	//					subMap = chatDataMap.subMap(getMsgTime, chatDataMap.lastKey());
	//					dataMap.put(sndMsgTime, chatDataMap.get(sndMsgTime));
	//					HashMap<String, String>hashMap = new HashMap<>();
	//					hashMap.put("sndMsgTime", sndMsgTime.toString());
	//					msgList.add(hashMap);
						HashMap<String, String> map = chatDataMap.get(sndMsgTime);
						for (Map.Entry<String, String>entry : map.entrySet() ) {
							HashMap<String, String> map2 = new HashMap<String, String>();
							map2.put("uname", entry.getKey());
							map2.put("message", entry.getValue());
							String timeString = sdf.format(new Date(sndMsgTime));
							map2.put("sndMsgTime", timeString);
							msgList.add(map2);
						}
					}
				}
//				System.out.println(msgList);
				Gson gson = new Gson();
				String msgMapString = gson.toJson(msgList);
	//			pushMsgTimeSet.add(new Date().getTime());//record last push time
				stdOut(response, msgMapString);
			}
		}
	}
	
	public void sndChatData(HttpServletRequest request, HttpServletResponse response){
		String uname = request.getParameter("uname");		
		String nickname = (String) request.getSession().getAttribute("nickname");
		if (uname.equals(nickname)) {
			@SuppressWarnings("unchecked")
			TreeMap<Long, HashMap<String, String>>chatDataMap = (TreeMap<Long, HashMap<String, String>>) this.getServletContext().getAttribute("chatDataMap");
			String message = request.getParameter("message");
			if (notEmpty(uname) && notEmpty(message)) {
				Long sndMsgTime = new Date().getTime();
				HashMap<String, String>msgMap = new HashMap<>();
				msgMap.put(uname, message);
				chatDataMap.put(sndMsgTime, msgMap);
	//			System.out.println(chatDataMap);
				stdOut(response, "1");
			}
		}
	}	
	
	public void valiNick(HttpServletRequest request, HttpServletResponse response){
		String nickname = request.getParameter("uname");
		String regex = "^[A-Za-z]\\w{2,10}$";
		if(Pattern.matches(regex, nickname)){
			stdOut(response, "1");
		}else{
			stdOut(response, "0");
		}
	}
	
	
	
	//utility methods
	public Player findPlayer(String nickname){
		Game game = (Game) this.getServletContext().getAttribute("game");
		Player tPlayer = null;
		ArrayList<Player> players = game.getPlayers();
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			if (player.getNickname().equals(nickname)) {
				tPlayer = player;
			}
		}
		return tPlayer;
	}
	
	public void stdOut(HttpServletResponse response, Object content){
		try {
			response.setContentType("text/JavaScript;charset=UTF-8");
//			response.setContentType("application/json;charset=UTF-8");
//			response.setContentType("text/plain; charset=UTF-8"); 
//			response.setContentType("text/html; charset=UTF-8");
//			response.setContentType("application/json;charset=UTF-8");
			PrintWriter pWriter = response.getWriter();
			pWriter.print(content);
			pWriter.flush();
			pWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Boolean notEmpty(Object object){
		Boolean flag = false;
		if(object instanceof String && object!=null && object!=""){
			flag = true;
		}
/*	else if (object instanceof Arrays && (Arrays)object.length!=0) {
			flag = true;
		}*/
		return flag;
	}
	
	public void valiUser(){
		
	}
	
	//test
	public static void main(String[] args) {
		// Service service = new Service();
/*		Game game = new Game(4);
		ArrayList<Player> players = new ArrayList<Player>();
		for (int i = 0; i < 4; i++) {
			Player player = new Player();
			player.setNickname("player" + i);
			player.setStatus((byte) 1);
			ArrayList<String> myCards = new ArrayList<>();
			myCards.add(String.valueOf(i + 2));
			myCards.add(String.valueOf(i + 4));
			myCards.add(String.valueOf(i + 3));
			player.setMyCards(myCards);
			players.add(player);
		}
		game.setFinishCount(4);
		Business business = new Business();// is able to be a static class
		Gson gson = new Gson();
		String jsonString = gson.toJson(players);
		System.out.println(jsonString);
		for (Player player : players) {
			Integer weight = business.weight(player.getMyCards());
			game.getAllWeights().put(weight, player.getNickname());
		}
		String winner = business.referee(game.getAllWeights(), game.getpCount());
		System.out.println(winner);*/

	}

}
