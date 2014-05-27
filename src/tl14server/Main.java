package tl14server;

import java.util.Scanner;

public class Main{
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Insert port number: ");
		Scanner in=new Scanner(System.in);
		
		int port=in.nextInt();
		new Thread(new ServerControl(port)).run();
		in.close();
	}

}