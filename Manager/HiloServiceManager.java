import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class HiloServiceManager implements Runnable{
	
	Socket socket;
	String nameC;
	ServidorNodos servidorNodos;
	
	public HiloServiceManager(Socket socket, String nameC,ServidorNodos servidorNodos) {
		this.socket=socket;
		this.nameC=nameC;
		this.servidorNodos=servidorNodos;
	}
	
	@Override
	public void run() {
		
		try {
			Mandar(socket,nameC);
			String pedido=Recibir(socket);
			String respuesta = servidorNodos.Trabajo(pedido);
			System.out.println("respuesta en el hiloServiceMaanger" +respuesta);
			Mandar(socket,respuesta);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void Mandar(Socket socket,String mnsj) throws IOException {
        DataOutputStream dOut;
        try {
            dOut = new DataOutputStream(socket.getOutputStream());
            dOut.writeUTF(mnsj);
            dOut.flush(); // Send off the data

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
	
	public String Recibir(Socket socket){
        String respuesta="";
        try {
            DataInputStream dIn = new DataInputStream(socket.getInputStream());
            respuesta=dIn.readUTF();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return respuesta;
    }
}
