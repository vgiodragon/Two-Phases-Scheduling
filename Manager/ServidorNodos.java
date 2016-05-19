import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServidorNodos {

	NodoLocal[] misNodo;
	
	public synchronized NodoLocal OLB(String tarea){
		System.out.println("Esto es lo que llega a OLB"+tarea);
		
		for(int i=0;i<misNodo.length;i++){
			if(preciso(tarea,i) && !misNodo[i].working){
				misNodo[i].working=true;
				System.out.println("tarea:"+tarea+" se va a:"+(i+1));
				return misNodo[i];
			}
		}
		
		return null;
	}
	
	public boolean preciso(String element,int k){
		String []condi=element.split("_");
		int CPU=Integer.parseInt(condi[0]);
		int Memory=Integer.parseInt(condi[1]);
		float Transmission = Float.parseFloat(condi[2]);
		return misNodo[k].posible(CPU, Memory, Transmission);
	}
	
	public String Trabajo(String tarea){
		String []primeraDiv=tarea.split("%");
		NodoLocal nl = OLB(primeraDiv[1]);
		String respues="error man";
		if(nl==null){//
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "NO HAY CAPACIDAD"; 
		}
		
		try {
			Socket socket = new Socket(nl.adrs, nl.port);
			Mandar(socket, nl.getID()+": "+primeraDiv[0]);
			respues= Recibir(socket);
			nl.working=false;
			System.out.println("servidor nodo responde"+respues);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return respues;
	}
	
	public ServidorNodos() {
		misNodo = new NodoLocal[5];
		misNodo[0] = new NodoLocal("127.0.0.1",7081,"1",400,170,10f);
		misNodo[1] = new NodoLocal("127.0.0.1",7082,"2",525,300,30f);
		misNodo[2] = new NodoLocal("127.0.0.1",7083,"3",500,150,20f);
		misNodo[3] = new NodoLocal("127.0.0.1",7084,"4",450,270,30f);
		misNodo[4] = new NodoLocal("127.0.0.1",7085,"5",550,180,35f);
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
