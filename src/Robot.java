import java.util.ArrayList;

public class Robot {

	private int x;
	private int y;
	private String historialMovimientos;
	private boolean encimaDestino;

	public Robot(int coordX, int coordY, String simboloRobot) {
		x = coordX;
		y = coordY;
		historialMovimientos = "";
		if (simboloRobot.equals("@")) {
			encimaDestino = false;
		} else {
			encimaDestino = true;
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
		this.historialMovimientos = this.historialMovimientos + movimiento;
	}

	public void printHistorialMovimientos() {
		System.out.println(this.historialMovimientos);
	}

	public void removeHistorialMovimientos() {
		this.historialMovimientos = "";
	}

	public void moverArriba() {
		this.setY(y - 1);
		this.addMovimiento("a");
	}

	public void moverAbajo() {
		this.setY(y + 1);
		this.addMovimiento("b");
	}

	public void moverIzq() {
		this.setX(x - 1);
		this.addMovimiento("i");
	}

	public void moverDch() {
		this.setX(x + 1);
		this.addMovimiento("d");
	}

	// Estos metodos van a tener que actualizar la posicion de la caja que reciban y
	// ver como
	// Tambien controlan si el movimiento de la caja se puede realizar
	public void moverCajaArriba() {
		this.setY(y - 1);
		this.addMovimiento("A");
	}

	public void moverCajaAbajo() {
		this.setY(y + 1);
		this.addMovimiento("B");
	}

	public void moverCajaIzq() {
		this.setX(x - 1);
		this.addMovimiento("I");
	}

	public void moverCajaDch() {
		this.setX(x + 1);
		this.addMovimiento("D");
	}

	public String simbolRobot() {
		if (encimaDestino == false) {
			return "@";
		} else {
			return "+";
		}
	}

	public String moverRobCajaDestino(int[] caja, int[] destino, int[][] cajasTemp, int[][] destinosTemp,
			ArrayList<String> habitacion, int indice) {

		// CALCULAR LA POSICION DONDE TIENE QUE IR EL ROBOT AL LADO DE LA CAJA
		// TENIENDO EN CUENTA LA POSICION DEL DESTINO. SI ESA UBICACION ESTA OCUPADA
		// TOMAR DECISION DE LA NUEVA POSICION
		Robot robotTemp = new Robot(this.getX(), this.getY(), "@");

		robotTemp.setX(caja[0]);
		robotTemp.setY(caja[1]);

		int[] destinoRobotCaja = new int[2];

		// Selecciona el destino al lado de la caja de forma especifica, comprueba que
		// el destino es valido en el orden de:
		// arriba, derecha, abajo e izquierda

		// arriba
		if (robotTemp.mirarArriba(cajasTemp, destinosTemp, habitacion) == '-'
				|| robotTemp.mirarArriba(cajasTemp, destinosTemp, habitacion) == '!') {
			destinoRobotCaja[0] = caja[0];
			destinoRobotCaja[1] = caja[1] - 1;
		} else if (robotTemp.mirarDcha(cajasTemp, destinosTemp, habitacion) == '-'
				|| robotTemp.mirarDcha(cajasTemp, destinosTemp, habitacion) == '!') {
			destinoRobotCaja[0] = caja[0] + 1;
			destinoRobotCaja[1] = caja[1];
		} else if (robotTemp.mirarAbajo(cajasTemp, destinosTemp, habitacion) == '-'
				|| robotTemp.mirarAbajo(cajasTemp, destinosTemp, habitacion) == '!') {
			destinoRobotCaja[0] = caja[0];
			destinoRobotCaja[1] = caja[1] + 1;
		} else if (robotTemp.mirarIzq(cajasTemp, destinosTemp, habitacion) == '-'
				|| robotTemp.mirarIzq(cajasTemp, destinosTemp, habitacion) == '!') {
			destinoRobotCaja[0] = caja[0] - 1;
			destinoRobotCaja[1] = caja[1];
		}

		// Mover el robot al lado de la caja
		String resultado = moverRobDestinoDeCaja(destinoRobotCaja, habitacion, cajasTemp, destinosTemp);

		// Porque no consigue llegar nunca al destino, hay una barrera o algo
		// COMPROBAR SI REALMENTE EL CATH PUEDE RECOGER UN STACK OVERFLOW
		if (resultado == "NO") {
			return "NO";
		}

		// MOVER CAJA A DESTINO
		
		resultado = resultado + moverRobDeCajaADestino(caja, destino, cajasTemp, habitacion, indice);
		
		
		
		
		return resultado;

	}

	private String moverRobDeCajaADestino(int[] caja, int[] destino, int[][] cajasTemp, ArrayList<String> habitacion,
			int indice) {
		
		//DEVUELVE LA CADENA DE LOS MOVIEMIENTOS REALIZADOS HASTA LLEGAR AL DESTINO
		
		// El registro del robot temporal esta vacio
		
		// La ubicacion del robot temporal es CORRECTA
		
		// Los movimientos del robot de la caja al destino se almacenan en resultado
		
		// Es necesario actualizar las posiciones de las cajas en cajasTemp y caja,
		// al llegar al destino marcarlo en la habitacion. El destino marcado como 
		// se marca a la llegada del metodo y NO DENTRO! y se marca en la habitacion
		//en la ubicacion de destino
		
		// CONFIRMADO SE MODFICIA --> IMPORTANTE: comprobar que la habitacion cuando se
		// marca el destino se cambia tambien al llegar
		// a la clase Habitacion ya que es un ArrayList.
		
		// El indice va de dos en dos, hay que trarlo en el recursivo al encontrar el
		// caso base
		
		
		
		
		
		return " ";
	}

	private String moverRobDestinoDeCaja(int[] destino, ArrayList<String> habitacion, int[][] cajasTemp,
			int[][] destinosTemp) {

		// Meter en try catch???? por si no llega nunca al destino
		Robot robotTemp = new Robot(this.getX(), this.getY(), "@");
		robotTemp.addMovimiento("x");

		try {
			moverRobDestinoDeCajaRecursivo(robotTemp, destino, habitacion, cajasTemp, destinosTemp);
			robotTemp.eliminarCaracterXRegistro();
			return robotTemp.getHistorialMovimientos();
		} catch (Exception e) {
			// TODO: handle exception
			return "NO";
		}

	}

	private void moverRobDestinoDeCajaRecursivo(Robot robotTemp, int[] destino, ArrayList<String> habitacion,
			int[][] cajasTemp, int[][] destinosTemp) {
		// TODO Auto-generated method stub
		if (robotTemp.getX() == destino[0] && robotTemp.getY() == destino[1]) {
			this.setX(robotTemp.getX());
			this.setY(robotTemp.getY());
			this.addMovimiento(robotTemp.getHistorialMovimientos());
		} else {
			// Destino encima de robotTemp
			if (destino[1] < robotTemp.getY() && robotTemp.getHistorialMovimientos()
					.charAt(robotTemp.getHistorialMovimientos().length() - 1) != 'b') {
				if (robotTemp.mirarArriba(cajasTemp, destinosTemp, habitacion) == '-'
						|| robotTemp.mirarArriba(cajasTemp, destinosTemp, habitacion) == '!') {
					robotTemp.moverArriba();
				} else if (robotTemp.mirarDcha(cajasTemp, destinosTemp, habitacion) == '-'
						|| robotTemp.mirarDcha(cajasTemp, destinosTemp, habitacion) == '!') {
					robotTemp.moverDch();
				} else if (robotTemp.mirarAbajo(cajasTemp, destinosTemp, habitacion) == '-'
						|| robotTemp.mirarAbajo(cajasTemp, destinosTemp, habitacion) == '!') {
					robotTemp.moverAbajo();
				} else if (robotTemp.mirarIzq(cajasTemp, destinosTemp, habitacion) == '-'
						|| robotTemp.mirarIzq(cajasTemp, destinosTemp, habitacion) == '!') {
					robotTemp.moverIzq();
				}
				// Destino a la derecha del robot
			} else if (destino[0] > robotTemp.getX() && robotTemp.getHistorialMovimientos()
					.charAt(robotTemp.getHistorialMovimientos().length() - 1) != 'i') {
				if (robotTemp.mirarDcha(cajasTemp, destinosTemp, habitacion) == '-'
						|| robotTemp.mirarDcha(cajasTemp, destinosTemp, habitacion) == '!') {
					robotTemp.moverDch();
				} else if (robotTemp.mirarAbajo(cajasTemp, destinosTemp, habitacion) == '-'
						|| robotTemp.mirarAbajo(cajasTemp, destinosTemp, habitacion) == '!') {
					robotTemp.moverAbajo();
				} else if (robotTemp.mirarIzq(cajasTemp, destinosTemp, habitacion) == '-'
						|| robotTemp.mirarIzq(cajasTemp, destinosTemp, habitacion) == '!') {
					robotTemp.moverIzq();
				} else if (robotTemp.mirarArriba(cajasTemp, destinosTemp, habitacion) == '-'
						|| robotTemp.mirarArriba(cajasTemp, destinosTemp, habitacion) == '!') {
					robotTemp.moverArriba();
				}
				// Destino debajo del robot
			} else if (destino[1] > robotTemp.getY() && robotTemp.getHistorialMovimientos()
					.charAt(robotTemp.getHistorialMovimientos().length() - 1) != 'a') {
				if (robotTemp.mirarAbajo(cajasTemp, destinosTemp, habitacion) == '-'
						|| robotTemp.mirarAbajo(cajasTemp, destinosTemp, habitacion) == '!') {
					robotTemp.moverAbajo();
				} else if (robotTemp.mirarIzq(cajasTemp, destinosTemp, habitacion) == '-'
						|| robotTemp.mirarIzq(cajasTemp, destinosTemp, habitacion) == '!') {
					robotTemp.moverIzq();
				} else if (robotTemp.mirarArriba(cajasTemp, destinosTemp, habitacion) == '-'
						|| robotTemp.mirarArriba(cajasTemp, destinosTemp, habitacion) == '!') {
					robotTemp.moverArriba();
				} else if (robotTemp.mirarDcha(cajasTemp, destinosTemp, habitacion) == '-'
						|| robotTemp.mirarDcha(cajasTemp, destinosTemp, habitacion) == '!') {
					robotTemp.moverDch();
				}
				// Destino a la izquierda del robot
			} else if (destino[0] < robotTemp.getX() && robotTemp.getHistorialMovimientos()
					.charAt(robotTemp.getHistorialMovimientos().length() - 1) != 'd') {
				if (robotTemp.mirarIzq(cajasTemp, destinosTemp, habitacion) == '-'
						|| robotTemp.mirarIzq(cajasTemp, destinosTemp, habitacion) == '!') {
					robotTemp.moverIzq();
				} else if (robotTemp.mirarArriba(cajasTemp, destinosTemp, habitacion) == '-'
						|| robotTemp.mirarArriba(cajasTemp, destinosTemp, habitacion) == '!') {
					robotTemp.moverArriba();
				} else if (robotTemp.mirarDcha(cajasTemp, destinosTemp, habitacion) == '-'
						|| robotTemp.mirarDcha(cajasTemp, destinosTemp, habitacion) == '!') {
					robotTemp.moverDch();
				} else if (robotTemp.mirarAbajo(cajasTemp, destinosTemp, habitacion) == '-'
						|| robotTemp.mirarAbajo(cajasTemp, destinosTemp, habitacion) == '!') {
					robotTemp.moverAbajo();
				}

			}

			// Habitacion habTemp = new Habitacion(habitacion, robotTemp.getX(),
			// robotTemp.getY(), robotTemp.simbolRobot(), habitacion.size(),
			// habitacion.get(0).length(), cajasTemp, destinosTemp);
			// habTemp.printHabitacion();

			moverRobDestinoDeCajaRecursivo(robotTemp, destino, habitacion, cajasTemp, destinosTemp);
		}

	}

	public boolean puedoMoverme(ArrayList<String> habitacion, int[][] cajas, int[][] destinos) {

		boolean libreIzq = false;
		boolean libreArr = false;
		boolean libreDch = false;
		boolean libreAba = false;

		if (this.mirarIzq(cajas, destinos, habitacion) == '-' || this.mirarIzq(cajas, destinos, habitacion) == '#'
				|| this.mirarIzq(cajas, destinos, habitacion) == '!') {
			libreIzq = true;
		}

		if (this.mirarArriba(cajas, destinos, habitacion) == '-' || this.mirarArriba(cajas, destinos, habitacion) == '#'
				|| this.mirarArriba(cajas, destinos, habitacion) == '!') {
			libreArr = true;
		}

		if (this.mirarDcha(cajas, destinos, habitacion) == '-' || this.mirarDcha(cajas, destinos, habitacion) == '#'
				|| this.mirarDcha(cajas, destinos, habitacion) == '!') {
			libreDch = true;
		}

		if (this.mirarAbajo(cajas, destinos, habitacion) == '-' || this.mirarAbajo(cajas, destinos, habitacion) == '#'
				|| this.mirarAbajo(cajas, destinos, habitacion) == '!') {
			libreAba = true;
		}

		if (libreIzq == false && libreArr == false && libreDch == false && libreAba == false) {
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

		for (int i = 0; i < cajas.length; i++) {
			cajasCad += cajas[i][0] + "," + cajas[i][1] + "|";
		}

		for (int i = 0; i < destinos.length; i++) {
			destinosCad += destinos[i][0] + "," + destinos[i][1] + "|";
		}

		for (int i = 0; i < habitacion.size(); i++) {
			for (int j = 0; j < habitacion.get(i).length(); j++) {
				if (i == this.getY() && j == this.getX()) {
					// Hay caja
					if (cajasCad.contains((j - 1) + "," + i)) {
						c = '#';
					} else
					// Hay destino
					if (destinosCad.contains((j - 1) + "," + i)) {
						c = '!';
					} else
					// Hay muro 0
					if (habitacion.get(i).charAt(j - 1) == '0') {
						c = '0';
					} else
					// Hay muro 1
					if (habitacion.get(i).charAt(j - 1) == '1') {
						c = '1';
						// Hay una caja en destino
					} else if (habitacion.get(i).charAt(j - 1) == '*') {
						c = '*';
					} else {
						// Hay hueco
						c = '-';
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

		for (int i = 0; i < cajas.length; i++) {
			cajasCad += cajas[i][0] + "," + cajas[i][1] + "|";
		}

		for (int i = 0; i < destinos.length; i++) {
			destinosCad += destinos[i][0] + "," + destinos[i][1] + "|";
		}

		for (int i = 0; i < habitacion.size(); i++) {
			for (int j = 0; j < habitacion.get(i).length(); j++) {
				if (i == this.getY() && j == this.getX()) {
					// Hay caja
					if (cajasCad.contains(j + "," + (i - 1))) {
						c = '#';
					} else
					// Hay destino
					if (destinosCad.contains(j + "," + (i - 1))) {
						c = '!';
					} else
					// Hay muro 0
					if (habitacion.get(i - 1).charAt(j) == '0') {
						c = '0';
					} else
					// Hay muro 1
					if (habitacion.get(i - 1).charAt(j) == '1') {
						c = '1';
						// Hay una caja en destino
					} else if (habitacion.get(i - 1).charAt(j) == '*') {
						c = '*';
					} else {
						// Hay hueco
						c = '-';
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

		for (int i = 0; i < cajas.length; i++) {
			cajasCad += cajas[i][0] + "," + cajas[i][1] + "|";
		}

		for (int i = 0; i < destinos.length; i++) {
			destinosCad += destinos[i][0] + "," + destinos[i][1] + "|";
		}

		for (int i = 0; i < habitacion.size(); i++) {
			for (int j = 0; j < habitacion.get(i).length(); j++) {
				if (i == this.getY() && j == this.getX()) {
					// Hay caja
					if (cajasCad.contains((j + 1) + "," + i)) {
						c = '#';
					} else
					// Hay destino
					if (destinosCad.contains((j + 1) + "," + i)) {
						c = '!';
					} else
					// Hay muro 0
					if (habitacion.get(i).charAt(j + 1) == '0') {
						c = '0';
					} else
					// Hay muro 1
					if (habitacion.get(i).charAt(j + 1) == '1') {
						c = '1';
						// Hay una caja en destino
					} else if (habitacion.get(i).charAt(j + 1) == '*') {
						c = '*';
					} else {
						// Hay hueco
						c = '-';
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

		for (int i = 0; i < cajas.length; i++) {
			cajasCad += cajas[i][0] + "," + cajas[i][1] + "|";
		}

		for (int i = 0; i < destinos.length; i++) {
			destinosCad += destinos[i][0] + "," + destinos[i][1] + "|";
		}

		for (int i = 0; i < habitacion.size(); i++) {
			for (int j = 0; j < habitacion.get(i).length(); j++) {
				if (i == this.getY() && j == this.getX()) {
					// Hay caja
					if (cajasCad.contains(j + "," + (i + 1))) {
						c = '#';
					} else
					// Hay destino
					if (destinosCad.contains(j + "," + (i + 1))) {
						c = '!';
					} else
					// Hay muro 0
					if (habitacion.get(i + 1).charAt(j) == '0') {
						c = '0';
					} else
					// Hay muro 1
					if (habitacion.get(i + 1).charAt(j) == '1') {
						c = '1';
						// Hay una caja en destino
					} else if (habitacion.get(i + 1).charAt(j) == '*') {
						c = '*';
					} else {
						// Hay hueco
						c = '-';
					}
				}
			}
		}
		return c;
	}

	private void eliminarCaracterXRegistro() {
		String regTemp = this.getHistorialMovimientos();
		regTemp = regTemp.replace("x", "");
		this.removeHistorialMovimientos();
		this.addMovimiento(regTemp);
	}
}