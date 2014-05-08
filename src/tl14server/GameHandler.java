package tl14server;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class GameHandler {

	private int gameCount=0;
	private ArrayList<Game> games;
	private List<Thread> thList;
	
	public GameHandler(){
		
	}
	
	public void addThread(Thread client){
		client.start();
		thList.add(client);
	}
	
	protected synchronized Game getGame(int pId,String name,String name2){
		
		if(!name.equals("")){
			for(int i=0;i<=games.size();i++){
				if(pId==games.get(i).getPlayersId()[0] || pId==games.get(i).getPlayersId()[1]){
					
					return games.get(i);
				}
			}
			gameCount++;
			Game tempG=new Game(gameCount,pId,name,this);
			games.add(tempG);
			return tempG;
		}
		
		return null;
	}
	
	public synchronized void dropGame(int id){
		for(int i=0;i<=games.size();i++){
			if(games.get(i).getId()==id){
				games.remove(i);
			}
		}
	}
}
