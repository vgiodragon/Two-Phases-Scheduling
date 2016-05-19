import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Cliente extends Thread{
    final int dstPort = 6081;
    String dstAddress = "127.0.0.1";
    Socket socket = null;
    String tarea;
	
	public Cliente(String tarea) {
		this.tarea=tarea;
	}   
	
	@Override
	public void run() {
        try {
        	String respuesta="NO HAY CAPACIDAD";
        	while(respuesta.equals("NO HAY CAPACIDAD")){
	            socket = new Socket(dstAddress, dstPort);            
	            String name = Recibir(socket);
	            
	            System.out.println(name+"Mando esto "+tarea);	            
	            Mandar(socket,name+"_"+tarea);
	            
	            respuesta = Recibir(socket);
	            
	            System.out.println(name+" recibio esto:"+respuesta);
        	}
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }		
	}
	
	public static void main(String[] args) {
		new Cliente("-100_100"+"%"+"500_256_20f").start();
		new Cliente("0_200"+"%"+"300_146_15f").start();
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
            DataInputStream dIn = 
            		new DataInputStream(socket.getInputStream());
            respuesta=dIn.readUTF();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return respuesta;
    }
	
}

