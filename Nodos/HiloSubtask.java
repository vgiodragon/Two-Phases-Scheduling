import java.util.Random;

public class HiloSubtask implements Runnable{

	String name;
	NodoConexion nc;
	String chamba;
	String respuesta;
	
	public HiloSubtask(String n,NodoConexion nc) {
		name=n;
		this.nc=nc; 
		chamba="";
	}
	
	public int getTimeExecution(int promedio){
		float maxX = 2;
		Random rand = new Random();
		return (int) ( promedio*rand.nextFloat() * maxX );
	}
	
	public void addChamba(String task){
		chamba+=task;
	}
	
	public String getRespuesta(){
		return respuesta;
	}
	
	
	public String Chambear(){
		String []chambas = chamba.split("%");
		String res="";
		for(int k=0;k<chambas.length;k++){
			res+="toma de subtask "+name+" ["+chambas[k]+"] resu:+"+funcion(100)+"\n";
			
			try {
				Thread.sleep(1200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return res;
	}
	double funcion(int fin){
        double sum = 0;
        for(int j = 0; j<=fin;j++ ){
            sum = sum + Math.sin(j*Math.random());
        }
        return sum;
    }
	
	
	@Override
	public void run() {
		respuesta=Chambear();
		// TODO Auto-generated method stub
		
	}

}
