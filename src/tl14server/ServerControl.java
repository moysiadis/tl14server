package tl14server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerControl implements Runnable{

	protected int listeningPort;
	protected Thread controlTh;
	protected ServerSocket listeningSock;
	private boolean state=false;//false ο σερβερ δεν έχει δεχτεί σήμα τερματισμού
	private int count=0;
	private GameHandler gH=new GameHandler();
	
	public ServerControl(int portNo){
		listeningPort=portNo;
	}
	
	
	
	@Override
	public void run() {
		
		synchronized(this){
			controlTh=Thread.currentThread();
			
		}
		
		try {
			listeningSock=new ServerSocket(listeningPort);
		} catch (IOException e) {
			
			throw new RuntimeException("cannot open server socket");
		}
		
		
		while(!isStopped()){
			count++;
			Socket clientSock=null;
			try {
				clientSock=listeningSock.accept();
			} catch (IOException e) {
				if(isStopped()){
					System.out.print("Server stopped");
					try {
						listeningSock.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
			Thread temp=new Thread(new clientCon (clientSock,count,gH));
			gH.addThread(temp);
			
		}
		
	}
	
	private synchronized boolean isStopped(){
		return state;
	}
	
}
