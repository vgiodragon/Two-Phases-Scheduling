public class NodoLocal {

	int port;
	String adrs;
	String id;	
	EstadoNodo miEstado;
	boolean working;
	
	public NodoLocal(String adrs,int port,String id,
			int R_CPU,int R_Memory,float Transmission_Rate) {
		this.adrs=adrs;
		this.port=port;
		this.id=id;
		working=false;
		miEstado= new EstadoNodo(R_CPU,R_Memory,Transmission_Rate);		
	}
	
	public String getID(){
		return id;
	}
	
	public EstadoNodo getMiEstado(){
		return miEstado;
	}
	
	public boolean posible(int CPU,int Memory,float Transmission){
		return miEstado.posible(CPU, Memory, Transmission);
	}
	
}
