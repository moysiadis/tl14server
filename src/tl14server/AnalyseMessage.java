package tl14server;

public class AnalyseMessage {

	public AnalyseMessage(){
		
	}
	
	public String[] AnalyseMsg(String msg){
		//εάν είναι move επιστρέφουμε temp[0]=0
		//εάν είναι error επιστρέφουμε temp[0]=1
		String[] temp=null;
		String[] ret=null;
		
		temp=msg.split(",");
		if(temp[0].equals("player")){
			ret[0]=temp[1];
			ret[1]=temp[2];
		}else if(temp[0].equals("move")){
			ret[0]="0";
		}
		
		return ret;
	}
}
