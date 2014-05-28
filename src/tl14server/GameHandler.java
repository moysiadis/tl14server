package tl14server;

import java.util.ArrayList;
import java.util.List;


public class GameHandler {

	private int gameCount=0;
	private ArrayList<Game> games;
	//private ArrayList<Thread> thList;
	
	public GameHandler(){
		games=new ArrayList<Game>();
	}
	
	public void addThread(Thread client){
		client.start();
		//thList.add(client);
	}
	
	protected synchronized Game getGame(int pId,String name,String name2){
		//name2 είναι το όνομα του παικτη του νήματος που καλεί
		boolean wait=false;
		Game tempG = null;
		
		if(!name.equals("")){
			for(int i=0;i<games.size();i++){
				if(games.get(i).isWait() && name.equals(games.get(i).getPlayerName())){
					gameCount++;
					return games.get(i);
				}
			}
		}else{
			boolean found=false;
			int i=0;
			while(!found && i<games.size()){
				if(!games.get(i).isWait() && !games.get(i).isFull()){
					gameCount++;
					games.get(i).addPlayer(name2, pId);
					tempG=games.get(i);
					found=true;
				}
			}
			if(!found){
				gameCount++;
				if(!name.equals(""))
					wait=true;
				
				tempG=new Game(gameCount,pId,name2,wait,this);
				games.add(tempG);
			}
		}
		
		return tempG;

	}
	
	public synchronized void dropGame(int id){
		for(int i=0;i<=games.size();i++){
			if(games.get(i).getId()==id){
				games.remove(i);
			}
		}
	}
	
	
	
	public synchronized Game findMatch(int pId,String name,int id){
		
		for(int i=0;i<games.size();i++){
			if(games.get(i).getId()!=id && !games.get(i).isFull()){
				games.get(i).addPlayer(name, pId);
				return games.get(i);
			}
		}
		return null;

	}
	
	/*
	 * for(int j=0;j<=thList.size();j++){
						if(thList.get(j).getId()==pId){
							thList.remove(j);
							break;
						}
					}
	*/
}
