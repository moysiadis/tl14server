package tl14server;

import java.util.ArrayList;

public class AnalyseMessage {

	public AnalyseMessage(){
		
	}
	
	public ArrayList<String> AnalyseMsg(String msg){
		//εάν είναι move επιστρέφουμε temp[0]=0
		//εάν είναι error επιστρέφουμε temp[0]=1
		ArrayList<String> temp=new ArrayList<String>();
		
		for(int i=0;i<msg.split(",").length;i++){
			temp.add(msg.split(",")[i]);
		}
		
		
		
		if(temp.get(0).equals("player")){
			temp.remove(0);
		}else if(temp.get(0).equals("move")){
			temp.set(0, "0");
		}
		
		return temp;
	}
}
