package com.cards.www;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class ChatServer {

	private ServerSocket chatServer;
	private Socket clinetSocket;
	private Hashtable<String, Socket> usrLst = new Hashtable<String, Socket>();// user
																				// name
																				// and
																				// it's
																				// socket
	private HashSet<Socket> allSockets = new HashSet<Socket>();// all the socket
	DataOutputStream out; // 鍒涘缓娴佸璞�
	DataInputStream in; // 鍒涘缓娴佸璞�
	ServerSocket server; // 澹版槑ServerSocket瀵硅薄
	Long lengths = -1l; // 鍥剧墖鏂囦欢鐨勫ぇ灏�
	// String uname;
	// String str;


	public static void main(String[] args) {
		ChatServer chatServer = new ChatServer();
		chatServer.getServer();
	}
	
	public void getServer() {
		try {
			chatServer = new ServerSocket(8081);
			while (true) {
				clinetSocket = chatServer.accept();
				in = new DataInputStream(clinetSocket.getInputStream());
				out = new DataOutputStream(clinetSocket.getOutputStream());
				new Thread( new serverThread(clinetSocket,in,out) ).start();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void broadcast(String message){
		DataOutputStream outStream = null;
		try {
			for (Socket socket : allSockets) {// broadcast all users info to everyone;
				outStream = new DataOutputStream(socket.getOutputStream());
				outStream.writeUTF(message);
				outStream.flush();
//					outStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	class serverThread implements Runnable {
		
		private Socket sckt;
		private DataInputStream inStream;
		private DataOutputStream outStream;
		private Boolean runFlag;

		protected serverThread(Socket skt, DataInputStream in, DataOutputStream out) {
			this.sckt = skt;
			this.inStream = in;
			this.outStream = out;
			this.runFlag = true;
		}

		@Override
		public void run() {
			
			try {
				String str = this.inStream.readUTF();
				
				if (str.startsWith("unm")) {
					String uname = str.substring(4);
					usrLst.put(uname, sckt);
					Set<String> keySet = usrLst.keySet();
					for (String key : keySet) {
						Socket tmpSocketckt = usrLst.get(key);
						allSockets.add(tmpSocketckt);
					}
					String message = "user:"+uname+" loign."; 
					broadcast(message);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				//
			}
			
			while (runFlag) {
				String string = null;
				try {
					string = this.inStream.readUTF();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if (string.startsWith("msg")) {
					String[] strArr = string.split(",");
					// String mark = str[0];
					String msg = strArr[1];
					String usrFrom = strArr[2];
					String message = usrFrom + " say: "+ msg;
					broadcast(message);
				} else {
					String[] extArr = string.split(",");
					String unm = extArr[1];
					Set<String> unms = usrLst.keySet();
					for (String uname : unms) {
						if (unm.equals(uname)) {
							usrLst.remove(usrLst.get(uname));
							Socket sct = usrLst.get(uname);
							try {
								sct.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					String message = unm+" leaved chat."; 
					broadcast(message);
					//end the process
					try {
						this.outStream.close();
						this.inStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					this.runFlag = false;
					break;
				}
			}

		}
	}
	
}
