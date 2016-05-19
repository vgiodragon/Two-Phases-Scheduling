import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServiceManager {
	
	static final int socketServerPORT = 6081;
    ServerSocket serverSocket;
    int count;
	
	public ServiceManager() {
		count=0;
		porSiempre();
	}
	
	public void porSiempre() {
        try {        	
        	System.out.println("Server Cliente Listo y la espera de clientes");
        	serverSocket = new ServerSocket(socketServerPORT);
        	ExecutorService hiloejecutor = Executors.newCachedThreadPool();
        	ServidorNodos servidorNodos = new ServidorNodos ();
        	while (true) {
        		System.out.println("Espero a un cliente");
        		Socket socket = serverSocket.accept();
                count++;
                HiloServiceManager hc = new HiloServiceManager(socket,"Cliente "+count,servidorNodos);
                hiloejecutor.execute(hc);
        	}
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
	public static void main(String[] args) {
		new ServiceManager();
	}	
}
