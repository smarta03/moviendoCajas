import java.util.ArrayList;

public class Robot {
	
	private int x;
	private int y;
	private String historialMovimientos;
	private boolean encimaDestino;
	
	public Robot(int coordX ,int coordY,String simboloRobot) {
		x = coordX;
		y = coordY;
		historialMovimientos = "";
		if(simboloRobot.equals("@")) {
			encimaDestino=false;
		} else {
			encimaDestino=true;
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public String getHistorialMovimientos() {
		return historialMovimientos;
	}

	public void addMovimiento(String movimiento) {
		this.historialMovimientos=this.historialMovimientos + movimiento;
	}
	
	public void printHistorialMovimientos() {
		System.out.println(this.historialMovimientos);
	}
	
	public void removeHistorialMovimientos() {
		this.historialMovimientos="";
	}
	
	public void moverArriba() {
		this.setY(y-1);
		this.addMovimiento("a");
	}
	
	public void moverAbajo() {
		this.setY(y+1);
		this.addMovimiento("b");
	}
	
	public void moverIzq() {
		this.setX(x-1);
		this.addMovimiento("i");
	}
	
	public void moverDch() {
		this.setX(x+1);
		this.addMovimiento("d");
	}
	
	public void moverCajaArriba() {
		this.setY(y-1);
		this.addMovimiento("A");
	}
	
	public void moverCajaAbajo() {
		this.setY(y+1);
		this.addMovimiento("B");
	}
	
	public void moverCajaIzq() {
		this.setX(x-1);
		this.addMovimiento("I");
	}
	
	public void moverCajaDch() {
		this.setX(x+1);
		this.addMovimiento("D");
	}
	
	public String simbolRobot() {
		if(encimaDestino==false) {
			return "@";
		} else {
			return "+";
		}
	}

	public void moverRobCajaDestino(int[] caja, int[] destino, int[][] cajasTemp, int[][] destinosTemp,
			ArrayList<String> habitacion) {
		// TODO Auto-generated method stub
		
	}

	public boolean puedoMoverme(ArrayList<String> habitacion, int[][] cajas, int[][] destinos) {
		
		
		boolean libreIzq = false;
		boolean libreArr = false;
		boolean libreDch = false;
		boolean libreAba = false;
		
		if (this.mirarIzq(cajas, destinos, habitacion)=='-' ||
				this.mirarIzq(cajas, destinos, habitacion)=='#' ||
				this.mirarIzq(cajas, destinos, habitacion)=='!') {
			libreIzq = true;
		}
		
		if (this.mirarArriba(cajas, destinos, habitacion)=='-' ||
				this.mirarArriba(cajas, destinos, habitacion)=='#' ||
				this.mirarArriba(cajas, destinos, habitacion)=='!') {
			libreArr = true;
		}
		
		if (this.mirarDcha(cajas, destinos, habitacion)=='-' ||
				this.mirarDcha(cajas, destinos, habitacion)=='#' ||
				this.mirarDcha(cajas, destinos, habitacion)=='!') {
			libreDch = true;
		}
		
		if (this.mirarAbajo(cajas, destinos, habitacion)=='-' ||
				this.mirarAbajo(cajas, destinos, habitacion)=='#' ||
				this.mirarAbajo(cajas, destinos, habitacion)=='!') {
			libreAba = true;
		}
		
		if(libreIzq==false && libreArr==true && libreDch==true && libreAba==true) {
			return false;
		} else {
			return true;
		}

	}

	private char mirarIzq(int[][] cajas, int[][] destinos, ArrayList<String> habitacion) {
		// TODO Auto-generated method stub
		
		String cajasCad = "";
		String destinosCad = "";
		char c = ' ';
		
		for(int i=0; i<cajas.length;i++) {
			cajasCad += cajas[i][0]+","+cajas[i][1]+"|";
		}
		
		for(int i=0; i<destinos.length;i++) {
			destinosCad += destinos[i][0]+","+destinos[i][1]+"|";
		}
		
		for(int i=0; i<habitacion.size(); i++) {
			for(int j=0; j<habitacion.get(i).length(); j++) {
				if(i==this.getY() && j==this.getX()) {
					//Hay caja
					if(cajasCad.contains((j-1)+","+i)) {
						c='#';
					} else 			
					//Hay destino
					if(destinosCad.contains((j-1)+","+i)) {
						c='!';
					} else 	
					//Hay muro 0
					if(habitacion.get(i).charAt(j-1)=='0') {
						c='0';
					} else 	
					//Hay muro 1
					if(habitacion.get(i).charAt(j-1)=='1') {
						c='1';
					//Hay una caja en destino
					} else if(habitacion.get(i).charAt(j-1)=='*') {
						c='*';
					} else {
					//Hay hueco 
						c='-';
					}
				}
			}
		}
		return c;
	}

	private char mirarArriba(int[][] cajas, int[][] destinos, ArrayList<String> habitacion) {
		// TODO Auto-generated method stub
		
		String cajasCad = "";
		String destinosCad = "";
		char c = ' ';
		
		for(int i=0; i<cajas.length;i++) {
			cajasCad += cajas[i][0]+","+cajas[i][1]+"|";
		}
		
		for(int i=0; i<destinos.length;i++) {
			destinosCad += destinos[i][0]+","+destinos[i][1]+"|";
		}
		
		for(int i=0; i<habitacion.size(); i++) {
			for(int j=0; j<habitacion.get(i).length(); j++) {
				if(i==this.getY() && j==this.getX()) {
					//Hay caja
					if(cajasCad.contains(j+","+(i-1))) {
						c='#';
					} else 			
					//Hay destino
					if(destinosCad.contains(j+","+(i-1))) {
						c='!';
					} else 	
					//Hay muro 0
					if(habitacion.get(i-1).charAt(j)=='0') {
						c='0';
					} else 	
					//Hay muro 1
					if(habitacion.get(i-1).charAt(j)=='1') {
						c='1';
					//Hay una caja en destino
					} else if(habitacion.get(i-1).charAt(j)=='*') {
						c='*';
					} else {
					//Hay hueco 
						c='-';
					}
				}
			}
		}
		return c;
	}
	
	private char mirarDcha(int[][] cajas, int[][] destinos, ArrayList<String> habitacion) {
		// TODO Auto-generated method stub
		
		String cajasCad = "";
		String destinosCad = "";
		char c = ' ';
		
		for(int i=0; i<cajas.length;i++) {
			cajasCad += cajas[i][0]+","+cajas[i][1]+"|";
		}
		
		for(int i=0; i<destinos.length;i++) {
			destinosCad += destinos[i][0]+","+destinos[i][1]+"|";
		}
		
		for(int i=0; i<habitacion.size(); i++) {
			for(int j=0; j<habitacion.get(i).length(); j++) {
				if(i==this.getY() && j==this.getX()) {
					//Hay caja
					if(cajasCad.contains((j+1)+","+i)) {
						c='#';
					} else 			
					//Hay destino
					if(destinosCad.contains((j+1)+","+i)) {
						c='!';
					} else 	
					//Hay muro 0
					if(habitacion.get(i).charAt(j+1)=='0') {
						c='0';
					} else 	
					//Hay muro 1
					if(habitacion.get(i).charAt(j+1)=='1') {
						c='1';
					//Hay una caja en destino
					} else if(habitacion.get(i).charAt(j+1)=='*') {
						c='*';
					} else {
					//Hay hueco 
						c='-';
					}
				}
			}
		}
		return c;
	}
	
	private char mirarAbajo(int[][] cajas, int[][] destinos, ArrayList<String> habitacion) {
		// TODO Auto-generated method stub
		
		String cajasCad = "";
		String destinosCad = "";
		char c = ' ';
		
		for(int i=0; i<cajas.length;i++) {
			cajasCad += cajas[i][0]+","+cajas[i][1]+"|";
		}
		
		for(int i=0; i<destinos.length;i++) {
			destinosCad += destinos[i][0]+","+destinos[i][1]+"|";
		}
		
		for(int i=0; i<habitacion.size(); i++) {
			for(int j=0; j<habitacion.get(i).length(); j++) {
				if(i==this.getY() && j==this.getX()) {
					//Hay caja
					if(cajasCad.contains(j+","+(i+1))) {
						c='#';
					} else 			
					//Hay destino
					if(destinosCad.contains(j+","+(i+1))) {
						c='!';
					} else 	
					//Hay muro 0
					if(habitacion.get(i+1).charAt(j)=='0') {
						c='0';
					} else 	
					//Hay muro 1
					if(habitacion.get(i+1).charAt(j)=='1') {
						c='1';
					//Hay una caja en destino
					} else if(habitacion.get(i+1).charAt(j)=='*') {
						c='*';
					} else {
					//Hay hueco 
						c='-';
					}
				}
			}
		}
		return c;
	}
}