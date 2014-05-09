package tl14server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class clientCon implements Runnable{

	private Socket sock;
	private int pId,pNo;
	private BufferedReader input;
	private BufferedWriter output;
	private AnalyseMessage anM;
	private GameHandler gH;
	private Game game;
	private boolean error=false;
	private boolean start=false;
	private boolean changeGame=false;
	///////////////////////////////////
	private boolean takeMove;
	private String thMsg;
	private boolean turn;
	private int[] dice;
	private boolean diceChanged;
	
	
	public clientCon(Socket sock,int pNo,GameHandler gH){
		this.sock=sock;
		this.pId=pNo;
		 anM=new AnalyseMessage();
		 this.gH=gH;
		 game=null;
	}
	
	
	@Override
	public void run() {
		String temp,rMsg;
		String[] msg;
		try {
			input  = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			output = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			getGame();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		while(true){//να δω wait/notify
			
			do{//ενεργή αναμονή
				if(error){
					
					send("errornewgame");
					
					try {
						getGame();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					error=false;
					break;
				}else if(start){
					
					temp="playerno,";
					pNo=game.getPNo(pId);
					temp.concat(String.valueOf(pNo));
					send(temp);
					if(pNo==0)
						turn=true;
					else
						turn=false;
					break;
				}
			}while(true);
			
			if (!error) {
				if (turn) {
					try {
						dice=game.getDice();
						temp="dice,";
						temp.concat(String.valueOf(dice[0])+","+String.valueOf(dice[1]));
						send(temp);
						rMsg = receive();
						game.notifyTh(pId, rMsg);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					turn=false;

				} else {
					try {
						sleep(2);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					msg = anM.AnalyseMsg(thMsg);
					if (msg[0].equals("0")) {
						send(thMsg);
						moveConsumed();
					}else if(msg[0].equals("1")){
						System.out.println("error in msg");
						game.leaveGame(pId);
						
					}
					turn=true;
				}
			}
			
			
		}
		
	}
	
	public void notifyMove(String msg){
		takeMove=true;
		thMsg=msg;
	}
	
	
	
	private void moveConsumed(){
		takeMove=false;
	}
	
	public void changeDice(){
		diceChanged=true;
	}
	
	private void sleep(int time) throws InterruptedException{
		String d;
		while(true){
			
			Thread.sleep(time);
			if(diceChanged){
				d="dice,";
				d.concat(String.valueOf(dice[0])+","+String.valueOf(dice[1]));
				send(d);
				diceChanged=false;
			}else if(takeMove){
				break;
			}
		}
	}
	
	
	public void startGame(){
		start=true;
	}
	
	private int getGame() throws IOException{
		String temp;
		String[] msg;
		if (game==null) {
			///////////////////////////get game=0
			send("sendname");
			temp = receive();
			msg = anM.AnalyseMsg(temp);
			if (msg[1].equals("-1")) {
				//στο πρώτο μήνυμα ο client δίνει το όνομα του παίκτη
				//και το όνομα του παίκτη που ψάχνει ο χρήστης αν ζητάει να παίξει με άλλον
				game = gH.getGame(pId, "", msg[0]);
				game.addThread(pId, this);
				send("searchwait");
				return 0;
			}
			////////////////////////////
		}
		return -1;

		
	}
	
	public void endGame(){
		error=true;
		
	}
	
	private String receive() throws IOException{
		String temp="";
		boolean done=false;
		
		while(!done){
			try {
				temp.concat(input.readLine());
				done=true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Connection problem in receive");
				game.leaveGame(pId);
				sock.close();
			}
		}
		return temp;
		
	}
	
	public void setDice(int[] dice){
		this.dice[0]=dice[0];
		this.dice[1]=dice[1];
	}
	
	private int send (String msg){
		
		try {
			output.write(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection problem in send");
			return 2;
		}
		return 1;
	}

}
