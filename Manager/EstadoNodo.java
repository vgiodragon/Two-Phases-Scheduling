public class EstadoNodo {

	int R_CPU;
	int R_Memory;
	float Transmission_Rate;

	
	public EstadoNodo(int R_CPU,int R_Memory,float Transmission_Rate) {
		this.R_CPU=R_CPU;
		this.R_Memory=R_Memory;
		this.Transmission_Rate=Transmission_Rate;
	}

	public boolean posible(int CPU,int Memory,float Transmission){
		if(R_CPU>CPU && R_Memory>Memory && Transmission_Rate>Transmission){
			return true;
		}
		
		return false;
	}
}
