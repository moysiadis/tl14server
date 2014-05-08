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
	private int pId;
	private BufferedReader input;
	private BufferedWriter output;
	private AnalyseMessage anM;
	private GameHandler gH;
	private Game game;
	
	public clientCon(Socket sock,int pNo,GameHandler gH){
		this.sock=sock;
		this.pId=pNo;
		 anM=new AnalyseMessage();
		 this.gH=gH;
	}
	
	
	@Override
	public void run() {
		String temp;
		String[] msg;
		try {
			input  = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			output = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		temp=receive();
		msg=anM.AnalyseMsg(temp);
		if(msg[1].equals("-1")){
			//στο πρώτο μνμ ο client δίνει το όνομα του παίκτη
			//και το όνομα του παίκτη που ψάχνει ο χρήστης αν ζητάει να παίξει με άλλον
			game=gH.getGame(pId,"",msg[0]);
		}else
			game=gH.getGame(pId,msg[1],msg[0]);
		
		
		while(true){
			
		}
		
	}
	
	private String receive(){
		String temp="";
		boolean done=false;
		
		while(!done){
			try {
				temp.concat(input.readLine());
				done=true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Connection problem in receive");
			}
		}
		return temp;
		
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
