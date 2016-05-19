
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Nodo extends Thread{
	
	int socketServerPORT;
	ServerSocket serverSocket;
	
	public Nodo(int ssPort){
		socketServerPORT= ssPort;
	}
	
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(socketServerPORT);		

			while(true){
				System.out.println("Worker a la espera");        		
				Socket socket = serverSocket.accept();        		        		
				NodoConexion nc = new NodoConexion(socket,4);        		
        		nc.Work();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void main(String[] args) {
		Scanner sc= new Scanner(System.in);
		System.out.println("Ingresa el # de nodo");
		int op=sc.nextInt();
		
		if(op==1)
			new Nodo(7081).start();
		
		else if(op==2)
			new Nodo(7082).start();
		
		else if(op==3)
			new Nodo(7083).start();
		
		else if(op==4)
			new Nodo(7084).start();
		
		else
			new Nodo(7085).start();
			
		sc.close();
	}
	

}
