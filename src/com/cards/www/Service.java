package com.cards.www;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

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
//	private ArrayList<Player> othersData;
	
	@Override
	public void init(){
		if (this.getServletContext().getAttribute("game") == null) {// if not initial yet
			game = new Game(4);
			business = new Business();
			players = new ArrayList<Player>();
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
		} else {
			System.out.println("404");
		}
	}

	public void gameInit(HttpServletRequest request,
			HttpServletResponse response) {

//		pWriter.print("game init success!");
	}

	public void gameStart(HttpServletRequest request,
			HttpServletResponse response) {
		String nickname = request.getParameter("nickname");
		request.getSession().setAttribute("nickname", nickname);

		Player player = new Player();
		player.setNickname(nickname);
		player.setStatus((byte) 1);
		player.setMyCards(game.sndCards(2));
		ArrayList<Player> players = game.getPlayers();
		players.add(player);
		game.setPlayers(players);
		
		Gson gson = new Gson();
		String jsonStr = gson.toJson(player);
//		response.setContentType("text/plain; charset=UTF-8"); 
		// OutputStream optStream = response.getOutputStream();
		// request.setAttribute("players", players);
		// pWriter.print(JSONUtil.serialize(players));
//		response.setHeader("Content", "text/JSON");
		try {
//			response.setContentType("application/json;charset=UTF-8");
			pWriter = response.getWriter();
			pWriter.print(jsonStr);
			pWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void moreCard(HttpServletRequest request,
			HttpServletResponse response) {
		String myName = request.getParameter("nickname");
//		response.setContentType("text/html; charset=UTF-8");
//		String nickname = (String) request.getSession().getAttribute("nickname");
//		System.out.println("more_card:"+nickname);

//		if (myName.equals(nickname)) {
			Game game = (Game) this.getServletContext().getAttribute("game");
			Player me = null;
			ArrayList<Player> players = game.getPlayers();
			for (int i = 0; i < players.size(); i++) {
				Player player = players.get(i);
				if (player.getNickname().equals(myName)) {
					me = player;
				}
			}
			if (me.getStatus() == (byte) 1) {
//				response.setContentType("text/plain; charset=UTF-8"); 
				if (me.getMyCards().size()<5) {
					String moreCard = game.sndCards(1).get(0);// deal with the new card at back-end
					me.getMyCards().add(moreCard);
					try {
						pWriter = response.getWriter();
						pWriter.write(moreCard);
						pWriter.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("full cards!");
					return;
				}

			} else {
				System.out.println("you have submited finish already!");
				return;
			}
//		}

	}

	public void getOthersData(HttpServletRequest request,
			HttpServletResponse response) {
		String myName = request.getParameter("nickname");
//		response.setContentType("text/plain; charset=UTF-8");
		Game game = (Game) this.getServletContext().getAttribute("game");// package_package_package_package_package_package_
		Player me = null;
		ArrayList<Player> players = game.getPlayers();
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			if (player.getNickname().equals(myName)) {
				me = player;
			}
		}
		if (me!=null) {
			Gson gson = new Gson();
//			response.setContentType("text/JavaScript; charset=UTF-8");
//			response.setContentType("application/json;charset=UTF-8");
//			System.out.println(me.getStatus()+", "+jsonString);
			if ( me.getStatus() == (byte) 1 || me.getStatus() == (byte) 2 && game.getFinishCount() != game.getpCount() ) {
				@SuppressWarnings("unchecked")
				ArrayList<Player> othersData = (ArrayList<Player>) players.clone();
				othersData.remove(me);
				String jsonString = gson.toJson(othersData);
				if (game.getFinishCount() != game.getpCount()) {//combine with up					
					try {
//						response.setContentType("application/json;charset=utf-8");
						PrintWriter prWriter = response.getWriter();// package_package_package_package_package_package_package_package_package_package_
						prWriter.print(jsonString);
						prWriter.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} /*else if (game.getFinishCount() == game.getpCount()) {
					String winner = (String) this.getServletContext().getAttribute(
							"winner");
					winner = gson.toJson(winner);
					try {
						PrintWriter prWriter = response.getWriter();// package_package_package_package_package_package_package_package_package_package_
						prWriter.write("winner: "+winner);
						prWriter.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}*/ else {
					return;
				}
			} else if ( me.getStatus() == (byte) 2 && game.getFinishCount() == game.getpCount() ) {
					String winner = (String) this.getServletContext().getAttribute(
							"winner");
					winner = gson.toJson(winner);
					try {
						PrintWriter prWriter = response.getWriter();// package_package_package_package_package_package_package_package_package_package_
						prWriter.write("winner: "+winner);
						prWriter.flush();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						me.setStatus((byte) 3);//-1:off-line, 0:ready, 1:playing, 2:finish, 3:over
					}
			} else {
//				System.out.println("me.getStatus():"+me.getStatus());
				return;
			}
		} else {
			return;
		}
	}

	public void finish(HttpServletRequest request, HttpServletResponse response) {
		String myName = request.getParameter("nickname");
//		response.setContentType("text/plain; charset=UTF-8");
		Game game = (Game) this.getServletContext().getAttribute("game");// package_package_package_package_package_package_
		Player me = null;
		for (int i = 0; i < game.getPlayers().size(); i++) {
			Player player = game.getPlayers().get(i);
			if (player.getNickname().equals(myName)) {
				me = player;
			}
		}
		System.out.println(game.getPlayers());
		me.setStatus((byte) 2);//-1:off-line, 0:ready, 1:playing, 2:finish, 3:over
		game.setFinishCount(game.getFinishCount() + 1);
		Business business = (Business) this.getServletContext().getAttribute(
				"business");
		Integer weight = business.weight(me.getMyCards());
//		System.out.println(weight);
		game.getAllWeights().put(weight, myName);// one of player is finish
//		System.out.println(game.getAllWeights());
		if (game.getFinishCount() == game.getpCount()) {// all players are finish
			System.out.println("game.getAllWeights():"+game.getAllWeights());
			winner = business.referee(game.getAllWeights(), game.getpCount());
			this.getServletContext().setAttribute("winner", winner);
/*			try {
				PrintWriter prWriter = response.getWriter();// package_package_package_package_package_package_package_package_package_package_
				prWriter.write(winner);
				prWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
		} else {
			System.out.println(me+" finish");
			return;
		}
	}

	/*
	 * public void monitor(HttpServletRequest request, HttpServletResponse
	 * response){ if (((Game)
	 * this.getServletContext().getAttribute("game")).getFinishCount()==4) {
	 * winner = business.referee(game.getAllWeights()); } pWriter.print(winner);
	 * }
	 */
	public static void main(String[] args) {
		// Service service = new Service();
		Game game = new Game(4);
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
		System.out.println(winner);
	}

}
