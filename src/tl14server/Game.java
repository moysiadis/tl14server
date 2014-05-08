package tl14server;

public class Game {

	private int id;
	private String name1,name2;
	private boolean isAvailable;
	private int pId1,pId2;
	private boolean isDead=false;
	private GameHandler gH;
	public Game(int id,int pId,String pName,GameHandler gH){
		isAvailable=true;
		this.id=id;
		pId1=pId;
		name1=pName;
		this.gH=gH;
	}
	
	public boolean isAvailable(){
		return isAvailable;
	}
	
	public String getPlayerName(){
		return name1;
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
		name2=name;
		pId2=pId;
	}
	
	protected void leaveGame(int pId){
		if(pId1==pId){
			pId1=-1;
		}else{
			pId2=-1;
		}
		
	}
	
	private void checkGame(){
		if(pId1==-1 && pId2==-1){
			gH.dropGame(id);
		}
	}
}
