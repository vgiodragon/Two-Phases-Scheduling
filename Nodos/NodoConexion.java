import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NodoConexion {

	Socket socket;
	HiloSubtask [] hsN;
	int cantHilos;
	public NodoConexion(Socket socket, int cantHilos){
		this.socket=socket;
		this.cantHilos=cantHilos;
		hsN = new HiloSubtask [cantHilos];
		for(int k=0;k<cantHilos;k++){
			hsN[k] = new HiloSubtask(k+"", this);
		}
	}
	
	
	public void Work(){
		try {
			String trabajo = Recibir(socket);
			String respuesta=Trabajar(trabajo);
			System.out.println("mando" + respuesta);
			Mandar(socket, respuesta);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public String Trabajar(String trabajo){
		System.out.println("Trabajo segun: "+trabajo);
		
		ExecutorService hiloEjecutor = Executors.newCachedThreadPool();
		
		String [] tareas = DivideChamba(trabajo);
		System.out.println("respustas de divide Chamba:");
		for(String element: tareas){
			String[]tar=element.split("%");
			hsN[Integer.parseInt(tar[1])].
				addChamba("["+tar[2]+","+tar[3]+"]"+"%");
			System.out.println(element);
		}
		/**
		 * 
		 * aca debo llamar a un funcion que divide trabajo para
		 * añadirle a los hilos respectivos
		 * 
		 */

		for(int i=0; i<cantHilos; i++){
			hiloEjecutor.execute(hsN[i]);
		}
		hiloEjecutor.shutdown();
		
		try {
			while (!hiloEjecutor.awaitTermination(2, TimeUnit.SECONDS)) {
				  //log.info("Awaiting completion of threads.");
				System.out.println("espero que terminen todos");
				}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String respues="Esta es la respuesta del HiloNodo\n";
		for(int i=0; i<cantHilos; i++){
			respues+=hsN[i].getRespuesta()+"\n";
		}
		
		
		return respues;
	}
	
	
	public String[] DivideChamba(String trabajo){
		int matrixPesos[][]=new int [cantHilos][cantHilos];
		boolean visi[][]= new boolean [cantHilos][cantHilos];
		float Threshold[]= new float [cantHilos];
		String [] tra= trabajo.split("_");
		int min = Integer.parseInt(tra[1]);
		int max = Integer.parseInt(tra[2]);
		int prom=(max-min)/cantHilos;
		
		for(int i=0; i<cantHilos; i++){
			int pri=min+prom/2;
			int aux =pri;
			Threshold[i]=0f;
			for(int j=0; j<cantHilos; j++){
				if (Math.abs(pri+prom*j)!=0)
					aux = Math.abs(pri+prom*j);
				matrixPesos[i][j]=hsN[j].getTimeExecution(aux);
				Threshold[i]+=matrixPesos[i][j];
				visi[i][j]=true;
			}
			Threshold[i]/=cantHilos;
		}
		/*
		int matrixPesos[][]=new int [][]{{18,14,38,26},
										 {14,12,24,18},
										 {26,18,66,42},
										 {19,20,24,36}};
		float Threshold[]= new float []{24,17,41,24.75f};
		for(int i=0; i<cantHilos; i++)
			for(int j=0; j<cantHilos; j++)
				visi[i][j]=true;
			
		*/
		
		String[] resul=new String[cantHilos] ;
		for(int k=0;k<cantHilos;k++){
			PrintMatrix(matrixPesos,visi,Threshold);
			resul[k]=MM(matrixPesos,visi,Threshold)+"%"+(min+prom*k)+"%"+(prom*(k+1));
		}
		return resul;		
	}
	
	public String MM(int matrixPesos[][],boolean visi[][], float Threshold[]){
		int imin=0,jmin=0;
		int valMin=999999;
		for(int i=0; i<cantHilos; i++){
			for(int j=0; j<cantHilos; j++)
				if(visi[i][j]){
					if(matrixPesos[i][j]<valMin){
						valMin=matrixPesos[i][j];
						imin=i;
						jmin=j;
					}
				}
		}
		 
		if(valMin<Threshold[imin]){
			for(int k=0; k<cantHilos; k++){
				visi[k][jmin] = visi[imin][k] = false;				
			}
			return imin+"%"+jmin;
		}else{
			System.out.println("ELSE para visible");
			for(int k=0; k<cantHilos; k++){
				visi[imin][k] = true;				
			}

			PrintMatrix(matrixPesos,visi,Threshold);
			return MM(matrixPesos,visi, Threshold);
		}

	}
	
	
	
	public void PrintMatrix(int matrixPesos[][],boolean visi[][] , float Threshold[]){
		String prin="";
		for(int i=0; i<cantHilos; i++){
			for(int j=0; j<cantHilos; j++){
				if(visi[i][j])
					prin+=" "+matrixPesos[i][j];
				else
					prin+=" "+"__";
			}
			prin+= " " + Threshold[i]+"\n";
			
		}
		System.out.println(prin);
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
