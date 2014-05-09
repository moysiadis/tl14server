package tl14server;

import java.util.Random;

public class Game {

	private int id,turnCount=0;
	private Game newGameInList;
	private String name1,name2;
	private boolean isAvailable;
	private int pId1=-1;
	private int pId2=-1;
	private boolean isDead=false;
	private GameHandler gH;
	private boolean cWait,isFull;
	private clientCon t1,t2; 
	private int[] dice;
	
	public Game(int id,int pId,String pName,boolean cWait,GameHandler gH){
		isAvailable=true;
		this.id=id;
		pId1=pId;
		name1=pName;
		this.gH=gH;
		this.cWait=cWait;
	}
	
	public boolean isAvailable(){
		return isAvailable;
	}
	
	public boolean isWait(){
		return cWait;
	}
	
	public String getPlayerName(){
		String name;
		if(name1.equals(""))
			name=name2;
		else
			name=name1;
		
		return name;
	}
	
	public boolean isFull(){
		return isFull;
	}
	
	public int[] getPlayersId(){
		int[] temp={0,0};
		temp[0]=pId1;
		temp[1]=pId2;
		return temp;
	}
	public int getId(){
		return id;
	}
	public void addPlayer(String name,int pId){
		
		if(pId1!=-1){
			name2=name;
			pId2=pId;
			isFull=true;
			t1.startGame();
			t2.startGame();
		}else{
			name1=name;
			pId1=pId;
			if(pId2!=-1){
				isFull=true;
				t1.startGame();
				t2.startGame();
			}
		}
	}	
	protected void leaveGame(int pId){
		String name;
		clientCon temp;
		if(pId1==pId){
			pId1=-1;
			name1=null;
			t1=null;
			temp=t2;
			name=name2;
			pId=pId2;//για χρήση στην εύρεση επόμενου παιχνιδιού
		}else{
			pId2=-1;
			name2=null;
			t2=null;
			temp=t1;
			name=name1;
			pId=pId1;
			
		}
		checkGame();
		if(!isDead && !cWait){
			isFull=false;
			newGameInList=gH.findMatch(pId,name,id);
			if(newGameInList!=null){
				temp.endGame();
				gH.dropGame(id);
			}
		}
	}
	private void checkGame(){//επικοινωνία με τον gameHandler
		if(pId1==-1 && pId2==-1){
			isDead=true;
			gH.dropGame(id);
		}
	}
	public void addThread(int id,clientCon t){
		if(id==pId1){
			t1=t;
		}else{
			t2=t;
		}
	}
	
	public Game getNewGame(){
		//σε περίπτωση που γίνει αλλαγή παιχνιδιού για ένα thread 
		return newGameInList;
		
		
	}
	
	//επιστρέφει την θέση του παίκτη στο παιχνίδι
	public int getPNo(int pId){
		if(pId==pId1)
			return 0;
		else
			return 1;
	}
	
	
	public void notifyTh(int pId,String msg){
		clientCon t;
		if(pId==pId1){
			t=t2;
		}else
			t=t1;
		
		t.notifyMove(msg);
		
	}
	
	public int[] getDice(){
		Random rand=new Random();
		clientCon t;
		
		turnCount++;
		if(turnCount%2==0)//θέσε τα ζάρια στον αντίπαλο
			t=t2;
		else
			t=t1;
		dice[0]=rand.nextInt((6 - 0) + 1);
		dice[1]=rand.nextInt((6 - 0) + 1);
		t.setDice(dice);
		t.changeDice();
		return dice;
	}
	
	
	
	
	
	
	
	
	
	
	
	/*public int checkStatus(){
		//για την εσωτερική επικοινωνία με τα threads
		int cS=0;
		if(pId1!=-1 && pId2!=-1){
			cS=1;
		}else{
			cS=2;
		}
		return cS;
	}*/

}
