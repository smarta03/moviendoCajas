import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
//import java.util.Scanner;

public class Habitacion {

	private ArrayList<String> habitacion;
	private Robot robot;
	private int n;
	private int m;
	private int[][] cajas;
	private int[][] destinos;

	public Habitacion(ArrayList<String> mapa, int coorXRob, int coorYRob, String simboloRobot, int filas, int columnas,
			int[][] coordCajas, int[][] coordDestinos) {
		habitacion = mapa;
		robot = new Robot(coorXRob, coorYRob, simboloRobot);
		n = filas;
		m = columnas;
		// cajas = new int[coordCajas.length][2];
		cajas = coordCajas;
		destinos = coordDestinos;
	}

	public void solucionarHabitacion2() {

		ArrayList<String> parciales = new ArrayList<String>();
		parciales = calculadorSolucionesParciales();

		int[][] solParcCoor = new int[cajas.length * 2][2];
		int[][] cajasTemp = new int[cajas.length][2];
		int[][] destinosTemp = new int[destinos.length][2];
		boolean[] esMasCorto = { true };
		boolean[] haySolucion = { true };
		int[] contador = {0};

		Robot robotTemp = new Robot(robot.getX(), robot.getY(), "@");

		// Rellenar el registro de robot para que fijo sea mas grande que el temporal en
		// la primera vuelta

		for (int i = 0; i < 10; i++) {
			robot.addMovimiento("xxxxxxxxxxxxxxxxxxxx");
		}


		// Desplazarse por todas las soluciones parciales
		for (int i = 0; i < parciales.size(); i++) {
			// Tengo una solucion parcial en coordenadas
			solParcCoor = cadenaACoord(parciales.get(i));
			// Rellenar cajasTemp y destinosTemp
			for (int j = 0; j < solParcCoor.length; j+=2) {
				cajasTemp[j/2][0] = solParcCoor[j][0];
				cajasTemp[j/2][1] = solParcCoor[j][1];
				destinosTemp[j/2][0] = solParcCoor[j+1][0];
				destinosTemp[j/2][1] = solParcCoor[j+1][1];
			}

			//System.out.println();

			// Solucionar la parcial
			// Para si el registro del robot temporal es mayor que el del actual
			// o si ha pasado por todos los puntos de las solucion parcial

			while (esMasCorto[0] == true && haySolucion[0] && contador[0] < cajasTemp.length) {

				// Mueve el robotTemp a cajasTemp[contador] y la lleva a destinosTemp[contador]
				// Marca la caja en destino en la habitacion
				//Actualiza
				try {
					moverRobotCajaDestino(robotTemp, cajasTemp, destinosTemp, contador, haySolucion);
				} catch (StackOverflowError e) {
					// TODO: handle exception
					haySolucion[0] = false;
					robotTemp.removeHistorialMovimientos();
					robotTemp.addMovimiento(robot.getHistorialMovimientos());
				}

				// AQUI comprobar que el registro del robotTemp es mas corto que el del robot
				if (robot.getHistorialMovimientos().length() <= robotTemp.getHistorialMovimientos().length()) {
					esMasCorto[0] = false;
				}

				contador[0] ++;

			}

			// Asignar el registro de temp a robot en caso de ser mas corto y existir solucion
			if (esMasCorto[0] && haySolucion[0]) {
				robot.removeHistorialMovimientos();
				robot.addMovimiento(robotTemp.getHistorialMovimientos());
				System.out.println(robot.getHistorialMovimientos());
			}

			robotTemp.removeHistorialMovimientos();
			esMasCorto[0] = true;
			haySolucion[0] = true;
			contador[0] = 0;
			robotTemp.setX(robot.getX());
			robotTemp.setY(robot.getY());

		}
		
		if(robot.getHistorialMovimientos().contains("x")) {
			System.out.println("NO HAY SOLUCION");
		} else {
			System.out.println(robot.getHistorialMovimientos());
		}

	}

	private void moverRobotCajaDestino(Robot robotTemp, int[][] cajasTemp, int[][] destinosTemp, int[] contador, boolean[] haySolucion) {

		int contCamino = 0;

		//Elegir el destino cerca de la caja donde debe ir el robot

		//Crear robot encima de la caja para ver que tiene alrededor y decidir en base a la posicion del destino
		Robot robotMov = new Robot(cajasTemp[contador[0]][0], cajasTemp[contador[0]][1],"@");
		int[] destinoCajaRob = {0,0};

		calcularPosRobotCaja(cajasTemp, destinosTemp, contador, haySolucion, robotMov, destinoCajaRob);

		if(haySolucion[0]) {
			
			//1 Mover robot hasta la caja
			//Buscar el camino mas corto desde el robotTemp hasta cajasTemp[indice]
			
			//PASAR UNA COPIA DE LA HABITACION
			ArrayList<String> habitacionTemp = new ArrayList<String>(habitacion);
			
			robotACajaRecursivo(robotTemp.getX(), robotTemp.getY(), destinoCajaRob[0], destinoCajaRob[1]
					,contCamino, habitacionTemp, robotTemp, cajasTemp, destinosTemp, haySolucion);
			
			//2 Mover la caja con el robot al destino
			//Buscar el caminos mas corto entre cajasTemp[contador] y destinosTemp[contador]
			//Mover la cajasTemp[contador] hasta destinosTemp[contador]
			
			//Marcar el destino como completado en la habitacion general
			//Mover cajasTemp[contador] a la nueva ubicacion que sera el destino si ha habido solucion

		} 
		
		if(!haySolucion[0]) {
			robotTemp.removeHistorialMovimientos();
			//robotTemp.addMovimiento(robot.getHistorialMovimientos());
		}

	}

	private void calcularPosRobotCaja(int[][] cajasTemp, int[][] destinosTemp, int[] contador, boolean[] haySolucion,
			Robot robotMov, int[] destinoCajaRob) {
		
		//Destino encima en linea
		if(destinosTemp[contador[0]][0]==robotMov.getX() && destinosTemp[contador[0]][1]<robotMov.getY()) {
			if(robotMov.mirarAbajo(cajasTemp, destinosTemp, habitacion)=='-' ||
					robotMov.mirarAbajo(cajasTemp, destinosTemp, habitacion)=='!') {
				destinoCajaRob[0] = robotMov.getX();
				destinoCajaRob[1] = robotMov.getY()+1;
			} else if (robotMov.mirarDcha(cajasTemp, destinosTemp, habitacion)=='-' ||
					robotMov.mirarDcha(cajasTemp, destinosTemp, habitacion)=='!') {
				destinoCajaRob[0] = robotMov.getX()+1;
				destinoCajaRob[1] = robotMov.getY();
			} else if (robotMov.mirarIzq(cajasTemp, destinosTemp, habitacion)=='-' ||
					robotMov.mirarIzq(cajasTemp, destinosTemp, habitacion)=='!') {
				destinoCajaRob[0] = robotMov.getX()-1;
				destinoCajaRob[1] = robotMov.getY();
			} else {
				haySolucion[0]=false;
			}
			
			if(robotMov.mirarAbajo(cajasTemp, destinosTemp, habitacion)=='0') {
				haySolucion[0] = false;
			}

			//Destino a la derecha en linea
		} else if (destinosTemp[contador[0]][0]>robotMov.getX() && destinosTemp[contador[0]][1]==robotMov.getY()) {
			if (robotMov.mirarIzq(cajasTemp, destinosTemp, habitacion)=='-' ||
					robotMov.mirarIzq(cajasTemp, destinosTemp, habitacion)=='!') {
				destinoCajaRob[0] = robotMov.getX()-1;
				destinoCajaRob[1] = robotMov.getY();			
			} else if(robotMov.mirarAbajo(cajasTemp, destinosTemp, habitacion)=='-' ||
					robotMov.mirarAbajo(cajasTemp, destinosTemp, habitacion)=='!') {
				destinoCajaRob[0] = robotMov.getX();
				destinoCajaRob[1] = robotMov.getY()+1;
			} else if (robotMov.mirarArriba(cajasTemp, destinosTemp, habitacion)=='-' ||
					robotMov.mirarArriba(cajasTemp, destinosTemp, habitacion)=='!') {
				destinoCajaRob[0] = robotMov.getX();
				destinoCajaRob[1] = robotMov.getY()-1;
			} else {
				haySolucion[0]=false;
			}

			if(robotMov.mirarIzq(cajasTemp, destinosTemp, habitacion)=='0') {
				haySolucion[0] = false;
			}

			//Destino debajo en linea
		} else if (destinosTemp[contador[0]][0]==robotMov.getX() && destinosTemp[contador[0]][1]>robotMov.getY()) {
			if (robotMov.mirarArriba(cajasTemp, destinosTemp, habitacion)=='-' ||
					robotMov.mirarArriba(cajasTemp, destinosTemp, habitacion)=='!') {
				destinoCajaRob[0] = robotMov.getX();
				destinoCajaRob[1] = robotMov.getY()-1;
			} else if (robotMov.mirarDcha(cajasTemp, destinosTemp, habitacion)=='-' ||
					robotMov.mirarDcha(cajasTemp, destinosTemp, habitacion)=='!') {
				destinoCajaRob[0] = robotMov.getX()+1;
				destinoCajaRob[1] = robotMov.getY();
			} else if (robotMov.mirarIzq(cajasTemp, destinosTemp, habitacion)=='-' ||
					robotMov.mirarIzq(cajasTemp, destinosTemp, habitacion)=='!') {
				destinoCajaRob[0] = robotMov.getX()-1;
				destinoCajaRob[1] = robotMov.getY();
			} else {
				haySolucion[0]=false;
			}

			if(robotMov.mirarArriba(cajasTemp, destinosTemp, habitacion)=='0') {
				haySolucion[0] = false;
			}

			//Destino izquierda en linea
		} else if (destinosTemp[contador[0]][0]<robotMov.getX() && destinosTemp[contador[0]][1]==robotMov.getY()) {
			if (robotMov.mirarDcha(cajasTemp, destinosTemp, habitacion)=='-' ||
					robotMov.mirarDcha(cajasTemp, destinosTemp, habitacion)=='!') {
				destinoCajaRob[0] = robotMov.getX()+1;
				destinoCajaRob[1] = robotMov.getY();			
			} else if(robotMov.mirarAbajo(cajasTemp, destinosTemp, habitacion)=='-' ||
					robotMov.mirarAbajo(cajasTemp, destinosTemp, habitacion)=='!') {
				destinoCajaRob[0] = robotMov.getX();
				destinoCajaRob[1] = robotMov.getY()+1;
			} else if (robotMov.mirarArriba(cajasTemp, destinosTemp, habitacion)=='-' ||
					robotMov.mirarArriba(cajasTemp, destinosTemp, habitacion)=='!') {
				destinoCajaRob[0] = robotMov.getX();
				destinoCajaRob[1] = robotMov.getY()-1;
			} else {
				haySolucion[0]=false;
			}
			
			if(robotMov.mirarDcha(cajasTemp, destinosTemp, habitacion)=='0') {
				haySolucion[0] = false;
			}

			//Destino arriba derecha
		} else if (destinosTemp[contador[0]][0]>robotMov.getX() && destinosTemp[contador[0]][1]<robotMov.getY()) {
			if(robotMov.mirarAbajo(cajasTemp, destinosTemp, habitacion)=='-' ||
					robotMov.mirarAbajo(cajasTemp, destinosTemp, habitacion)=='!') {
				destinoCajaRob[0] = robotMov.getX();
				destinoCajaRob[1] = robotMov.getY()+1;
			} else if (robotMov.mirarIzq(cajasTemp, destinosTemp, habitacion)=='-' ||
					robotMov.mirarIzq(cajasTemp, destinosTemp, habitacion)=='!') {
				destinoCajaRob[0] = robotMov.getX()-1;
				destinoCajaRob[1] = robotMov.getY();
			} else {
				haySolucion[0]=false;
			}

			//Destino abajo derecha
		} else if (destinosTemp[contador[0]][0]>robotMov.getX() && destinosTemp[contador[0]][1]>robotMov.getY()) {
			if (robotMov.mirarArriba(cajasTemp, destinosTemp, habitacion)=='-' ||
					robotMov.mirarArriba(cajasTemp, destinosTemp, habitacion)=='!') {
				destinoCajaRob[0] = robotMov.getX();
				destinoCajaRob[1] = robotMov.getY()-1;
			} else if (robotMov.mirarIzq(cajasTemp, destinosTemp, habitacion)=='-' ||
					robotMov.mirarIzq(cajasTemp, destinosTemp, habitacion)=='!') {
				destinoCajaRob[0] = robotMov.getX()-1;
				destinoCajaRob[1] = robotMov.getY();
			} else {
				haySolucion[0]=false;
			}
			//Destino abajo izquierda
		} else if (destinosTemp[contador[0]][0]<robotMov.getX() && destinosTemp[contador[0]][1]>robotMov.getY()) {
			if (robotMov.mirarArriba(cajasTemp, destinosTemp, habitacion)=='-' ||
					robotMov.mirarArriba(cajasTemp, destinosTemp, habitacion)=='!') {
				destinoCajaRob[0] = robotMov.getX();
				destinoCajaRob[1] = robotMov.getY()-1;
			} else if (robotMov.mirarDcha(cajasTemp, destinosTemp, habitacion)=='-' ||
					robotMov.mirarDcha(cajasTemp, destinosTemp, habitacion)=='!') {
				destinoCajaRob[0] = robotMov.getX()+1;
				destinoCajaRob[1] = robotMov.getY();
			} else {
				haySolucion[0]=false;
			}
			//Destino arriba izquierda
		} else if (destinosTemp[contador[0]][0]<robotMov.getX() && destinosTemp[contador[0]][1]<robotMov.getY()) {
			if(robotMov.mirarAbajo(cajasTemp, destinosTemp, habitacion)=='-' ||
					robotMov.mirarAbajo(cajasTemp, destinosTemp, habitacion)=='!') {
				destinoCajaRob[0] = robotMov.getX();
				destinoCajaRob[1] = robotMov.getY()+1;
			} else if (robotMov.mirarDcha(cajasTemp, destinosTemp, habitacion)=='-' ||
					robotMov.mirarDcha(cajasTemp, destinosTemp, habitacion)=='!') {
				destinoCajaRob[0] = robotMov.getX()+1;
				destinoCajaRob[1] = robotMov.getY();
			} else {
				haySolucion[0]=false;
			}
		}
	}

	private void robotACajaRecursivo(int xInicio, int yInicio, int xFin, int yFin, int contador, ArrayList<String> habitacionTemp, Robot robotTemp,
			int[][] cajasTemp, int[][] destinosTemp, boolean[] haySolucion) {
		// TODO Auto-generated method stub

		Habitacion habitacionPrint = new Habitacion(habitacionTemp, robotTemp.getX(), robotTemp.getY(), "@",n,m, cajasTemp, destinosTemp);

		habitacionPrint.printHabitacion();

		//Las cordenadas de inicio llegan al final
		if (xInicio == xFin && yInicio == yFin) {
			//Mover el robot a la posicion final
			robotTemp.setX(xFin);
			robotTemp.setY(yFin);

		} else {
			
//Marcamos donde estamos ahora con el robot con un 0 en la habitacion para np volver a pasar
//y evitar bucles

//			habitacionTemp.set(robotTemp.getY(), habitacionTemp.get(robotTemp.getY()).substring(0, robotTemp.getX())
//					+ "0" +  habitacionTemp.get(robotTemp.getY()).substring(robotTemp.getX()+1, habitacionTemp.get(robotTemp.getY()).length()) );	
	
			//Arriba
			if(yFin<yInicio && robotTemp.mirarArriba(cajasTemp, destinosTemp, habitacionTemp) == '-'
					|| yFin<yInicio && robotTemp.mirarArriba(cajasTemp, destinosTemp, habitacionTemp) == '!') {
				robotTemp.moverArriba();
				robotACajaRecursivo(robotTemp.getX(), robotTemp.getY(), xFin, yFin, contador+1, habitacionTemp, robotTemp, cajasTemp, destinosTemp, haySolucion);


				//Derecha
			} else if(xFin>xInicio && robotTemp.mirarDcha(cajasTemp, destinosTemp, habitacionTemp) == '-'
					|| xFin>xInicio && robotTemp.mirarDcha(cajasTemp, destinosTemp, habitacionTemp) == '!') {
				robotTemp.moverDch();
				robotACajaRecursivo(robotTemp.getX(), robotTemp.getY(), xFin, yFin, contador+1, habitacionTemp, robotTemp, cajasTemp, destinosTemp, haySolucion);


				//Abajo
			} else if(yFin>yInicio && robotTemp.mirarAbajo(cajasTemp, destinosTemp, habitacionTemp) == '-'
					|| yFin>yInicio && robotTemp.mirarAbajo(cajasTemp, destinosTemp, habitacionTemp) == '!') {
				robotTemp.moverAbajo();
				robotACajaRecursivo(robotTemp.getX(), robotTemp.getY(), xFin, yFin, contador+1, habitacionTemp, robotTemp, cajasTemp, destinosTemp, haySolucion);


				//Izquierda
			} else if(xFin<xInicio && robotTemp.mirarIzq(cajasTemp, destinosTemp, habitacionTemp) == '-'
					|| xFin<xInicio && robotTemp.mirarIzq(cajasTemp, destinosTemp, habitacionTemp) == '!') {
				robotTemp.moverIzq();
				robotACajaRecursivo(robotTemp.getX(), robotTemp.getY(), xFin, yFin, contador+1, habitacionTemp, robotTemp, cajasTemp, destinosTemp, haySolucion);
			//No se puede mover al destino
			} else {
				haySolucion[0] = false; 
				System.out.println("No hay solucion");
			}

			//Desbloqueo del sitio donde estamos
			//			habitacionTemp.set(robotTemp.getY(), habitacionTemp.get(robotTemp.getY()).substring(0, robotTemp.getX())
			//					+ "-" +  habitacionTemp.get(robotTemp.getY()).substring(robotTemp.getX()+1, habitacionTemp.get(robotTemp.getY()).length()) );
			//			

		}

	}

	public void solucionarHabitacion() {

		// ELIMINAR BLOQUE
		this.printHabitacion();
		System.out.println("cajas");
		for (int i = 0; i < cajas.length; i++) {
			System.out.println(cajas[i][0] + " " + cajas[i][1]);
		}
		System.out.println("destinos");
		for (int i = 0; i < destinos.length; i++) {
			System.out.println(destinos[i][0] + " " + destinos[i][1]);
		}

		// Devuelvo array con todas las combinaciones de movimientos
		ArrayList<String> parciales = new ArrayList<String>();

		parciales = calculadorSolucionesParciales();

		// boolean[] esSolucion = { true };
		// boolean[] esMasCorto = { true };
		String[] regRobotTemp = { "x" };
		// String[] regRobotSolucion = { "" };
		int originalX = robot.getX();
		int originalY = robot.getY();

		for (int i = 0; i < 100; i++) {
			regRobotTemp[0] += "x";
		}

		//PASARLO A VUELTA ATRAS PARA CONTROLAR EL CASO EN EL QUE NO EXISTA SOLUCION
		//SE ASIGNA LA CADE LARGA DE X'S
		String regRobotTempClone = new String(regRobotTemp[0]);

		// Mover robot segun combinaciones
		for (int i = 0; i < parciales.size(); i++) {
			vueltaAtras(parciales.get(i), regRobotTemp);
			// Aqui compruebo si devuelve que hay solucion o si el
			// registro del robot es mas largo. Restablece estas
			// variables

			// Reiniciar registro robot y almanarlo en otro sitio
			// Devolver al robot y las cajas a la posicion original

			robot.setX(originalX);
			robot.setY(originalY);
		}

		System.out.println();

	}

	private void vueltaAtras(String solParc, String[] regRobotTemp) {

		int[][] camino = new int[cajas.length * 2][2];
		camino = cadenaACoord(solParc);
		int contador = 0;

		//IMPORTANTE
		//VER SI HAY QUE ACTUALIZAR CAJAS Y DESTINOS GLOBAL!!!
		//-------------------------------------------------------------------------------
		int[][] cajasTemp = new int[camino.length / 2][2];
		int[][] destinosTemp = new int[camino.length / 2][2];

		boolean movRealizado = true;
		boolean existeSolucionTemp = true;

		String acumuladorRegistro = "";
		String registroTempSave = regRobotTemp[0];

		for (int i = 0, j = 0; i < cajasTemp.length * 2; i += 2, j++) {
			cajasTemp[j] = camino[i];
			destinosTemp[j] = camino[i + 1];
		}

		while (contador < camino.length && movRealizado && existeSolucionTemp) {

			// existeSolucion: Comprueba si hay solucion para esas cajas y destinos en esa
			// habitacion y si
			// el robot se puede mover.
			// Las cajas y destinos las actualiza el robot.
			// Puede recibir cajas ya solucionadas.

			if (existeSolucion(cajasTemp, destinosTemp, contador)) {

				// Mueve el robot a la siguiente caja y la lleva al destino registrando
				// los movimientos. Actualiza cajasTemp y destinosTemp
				// IMPORTANTE!!!La caja en el destino se indica en la habitacion
				regRobotTemp[0] = robot.moverRobCajaDestino(camino[contador], camino[contador + 1], cajasTemp,
						destinosTemp, habitacion, contador);
				if (regRobotTemp[0] == "NO") {
					movRealizado = false;
				} else {
					movRealizado = true;
				}
				// System.out.println(robot.getHistorialMovimientos());

				acumuladorRegistro = acumuladorRegistro + regRobotTemp[0];

			} else {
				existeSolucionTemp = false;
			}

			contador += 2;

		}

		if (movRealizado && acumuladorRegistro.length() < robot.getHistorialMovimientos().length() && existeSolucionTemp) {
			robot.removeHistorialMovimientos();
			robot.addMovimiento(acumuladorRegistro);
			// esMasCorto[0] = true;

		} else {
			// Si no se realiza el movimiento el registro del robot sera el registro
			// anterior
			robot.removeHistorialMovimientos();
			//robot.addMovimiento(registroTempSave);

		}

	}

	private boolean existeSolucion(int[][] cajasTemp, int[][] destinosTemp, int indice) {

		// Comprobar que el robot se puede mover
		boolean robotPuedeMoverse = robot.puedoMoverme(habitacion, cajasTemp, destinosTemp);

		// Comprobar que las cajas se pueden mover. Tener en cuenta cajas ya resueltas.
		boolean cajaMov = cajaPuedeMoverse(cajasTemp, indice);

		// Comprobar que la caja es movible al destino asignado
		boolean cajaADestino = cajaPuedeMoverseADestino(cajasTemp, destinosTemp, indice);

		// Comprobar que los destinos son accesibles. Tener en cuenta destinos ya
		// resueltos.
		// Necesito pasarle las cajas por el caso especial en el que un destino este
		// tapado por
		// una caja que no se esta moviendo en ese momento
		boolean destinoAccesible = destinoEsAccesible(destinosTemp, cajasTemp, indice);

		return robotPuedeMoverse && cajaMov && cajaADestino && destinoAccesible;
	}

	private boolean destinoEsAccesible(int[][] destinosTemp, int[][] cajasTemp, int indice) {

		// El indice servira el mismo tanto para los destinos como para las cajas,
		// porque la caja
		// que se va a mover es justo la anterior del camino al destino
		if (indice != 0) {
			indice = indice / 2;
		}

		// Vemos primero que tiene en las cuatro posiciones arriba, derecha, abajo,
		// izquierda.

		boolean arriba = true;
		boolean derecha = true;
		boolean abajo = true;
		boolean izquierda = true;

		boolean cajaArriba = false;
		boolean cajaDerecha = false;
		boolean cajaAbajo = false;
		boolean cajaIzquierda = false;

		// cuatro posibilidades para que una caja no pueda moverse

		// arriba
		if (habitacion.get(destinosTemp[indice][1] - 1).charAt(destinosTemp[indice][0]) == '0'
				|| habitacion.get(destinosTemp[indice][1] - 1).charAt(destinosTemp[indice][0]) == '1'
				|| habitacion.get(destinosTemp[indice][1] - 1).charAt(destinosTemp[indice][0]) == '*') {
			arriba = false;
		}

		// derecha
		if (habitacion.get(destinosTemp[indice][1]).charAt(destinosTemp[indice][0] + 1) == '0'
				|| habitacion.get(destinosTemp[indice][1]).charAt(destinosTemp[indice][0] + 1) == '1'
				|| habitacion.get(destinosTemp[indice][1]).charAt(destinosTemp[indice][0] + 1) == '*') {
			derecha = false;
		}

		// abajo
		if (habitacion.get(destinosTemp[indice][1] + 1).charAt(destinosTemp[indice][0]) == '0'
				|| habitacion.get(destinosTemp[indice][1] + 1).charAt(destinosTemp[indice][0]) == '1'
				|| habitacion.get(destinosTemp[indice][1] + 1).charAt(destinosTemp[indice][0]) == '*') {
			abajo = false;
		}

		// izquierda
		if (habitacion.get(destinosTemp[indice][1]).charAt(destinosTemp[indice][0] - 1) == '0'
				|| habitacion.get(destinosTemp[indice][1]).charAt(destinosTemp[indice][0] - 1) == '1'
				|| habitacion.get(destinosTemp[indice][1]).charAt(destinosTemp[indice][0] - 1) == '*') {
			izquierda = false;
		}

		// Si esta totalemente encerrado ya no es accesible
		if (!arriba && !derecha && !abajo && !izquierda) {
			return false;
		}

		// CASO ESPECIAL: DESTINO ABIERTO SOLO POR UN PUNTO QUE LO TAPA UNA CAJA,
		// COMPROBAR QUE ESA CAJA ES LA
		// QUE SE ESTA MOVIENDO AHORA, SI ES OTRA EL DESTINO NO ES ACCESIBLE

		// Ver si hay alguna caja alrededor del destino
		for (int i = 0; i < cajasTemp.length; i++) {
			// Comprobar si hay alguna caja con cordenadas x e y que este alrededor del
			// destino
			// Arriba
			if (cajasTemp[i][0] == destinosTemp[indice][0] && cajasTemp[i][1] + 1 == destinosTemp[indice][1]) {
				cajaArriba = true;
				arriba = false;
				// Derecha
			} else if (cajasTemp[i][0] - 1 == destinosTemp[indice][0] && cajasTemp[i][1] == destinosTemp[indice][1]) {
				cajaDerecha = true;
				derecha = false;
				// Abajo
			} else if (cajasTemp[i][0] == destinosTemp[indice][0] && cajasTemp[i][1] - 1 == destinosTemp[indice][1]) {
				cajaAbajo = true;
				abajo = false;
				// Izquierda
			} else if (cajasTemp[i][0] + 1 == destinosTemp[indice][0] && cajasTemp[i][1] == destinosTemp[indice][1]) {
				cajaIzquierda = true;
				izquierda = false;
			}
		}

		// La caja que se esta empujando esta encima del destino
		if (cajaArriba && destinosTemp[indice][0] == cajasTemp[indice][0]
				&& destinosTemp[indice][1] == cajasTemp[indice][1] + 1) {
			arriba = true;
			// La caja que se esta empujando esta a la derecha del destino
		} else if (cajaDerecha && destinosTemp[indice][0] == cajasTemp[indice][0] - 1
				&& destinosTemp[indice][1] == cajasTemp[indice][1]) {
			derecha = true;
			// La caja que se esta empujando esta debajo del destino
		} else if (cajaAbajo && destinosTemp[indice][0] == cajasTemp[indice][0]
				&& destinosTemp[indice][1] == cajasTemp[indice][1] - 1) {
			abajo = true;
			// La caja que se esta empujando esta a la izquierda del destino
		} else if (cajaIzquierda && destinosTemp[indice][0] == cajasTemp[indice][0] + 1
				&& destinosTemp[indice][1] == cajasTemp[indice][1]) {
			izquierda = true;
		}

		// Compruebo ya si el destino tiene al menos un lado abierto
		if (!arriba && !derecha && !abajo && !izquierda) {
			return false;
		} else {
			return true;
		}

	}

	private boolean cajaPuedeMoverse(int[][] cajasTemp, int indice) {

		if (indice != 0) {
			indice = indice / 2;
		}

		boolean arriba = true;
		boolean derecha = true;
		boolean abajo = true;
		boolean izquierda = true;

		// cuatro posibilidades para que una caja no pueda moverse

		// arriba
		if (habitacion.get(cajasTemp[indice][1] - 1).charAt(cajasTemp[indice][0]) == '0'
				|| habitacion.get(cajasTemp[indice][1] - 1).charAt(cajasTemp[indice][0]) == '1'
				|| habitacion.get(cajasTemp[indice][1] - 1).charAt(cajasTemp[indice][0]) == '*') {
			arriba = false;
		}

		// derecha
		if (habitacion.get(cajasTemp[indice][1]).charAt(cajasTemp[indice][0] + 1) == '0'
				|| habitacion.get(cajasTemp[indice][1]).charAt(cajasTemp[indice][0] + 1) == '1'
				|| habitacion.get(cajasTemp[indice][1]).charAt(cajasTemp[indice][0] + 1) == '*') {
			derecha = false;
		}

		// abajo
		if (habitacion.get(cajasTemp[indice][1] + 1).charAt(cajasTemp[indice][0]) == '0'
				|| habitacion.get(cajasTemp[indice][1] + 1).charAt(cajasTemp[indice][0]) == '1'
				|| habitacion.get(cajasTemp[indice][1] + 1).charAt(cajasTemp[indice][0]) == '*') {
			abajo = false;
		}

		// izquierda
		if (habitacion.get(cajasTemp[indice][1]).charAt(cajasTemp[indice][0] - 1) == '0'
				|| habitacion.get(cajasTemp[indice][1]).charAt(cajasTemp[indice][0] - 1) == '1'
				|| habitacion.get(cajasTemp[indice][1]).charAt(cajasTemp[indice][0] - 1) == '*') {
			izquierda = false;
		}

		// comprobar las posibilidades
		// arriba derecha
		if (!arriba && !derecha) {
			return false;
			// derecha abajo
		} else if (!derecha && !abajo) {
			return false;
			// abajo izquierda
		} else if (!abajo && !izquierda) {
			return false;
			// izquierda arriba
		} else if (!izquierda && !arriba) {
			return false;
		} else {
			return true;
		}

	}

	private boolean cajaPuedeMoverseADestino(int[][] cajasTemp, int[][] destinosTemp, int indice) {
		// COMPROBAR QUE LA CAJA SE PUEDE MOVER AL DESTINO: CASOS ESPECIALES

		// EJ. LA CAJA SOLO TIENE UN PUNTO CUBIERTO QUE ES EL DE ARRIBA, SI LA FILA DE
		// ARRIBA ESTA ENTERA CUBIERTA
		// POR 0, 1 , espacio o * LA CAJA NO PODRIA MOVERSE

		if (indice != 0) {
			indice = indice / 2;
		}

		int[] caja = cajasTemp[indice];
		int[] destino = destinosTemp[indice];

		boolean libreArriba = false;
		boolean libreDerecha = false;
		boolean libreAbajo = false;
		boolean libreIzquierda = false;

		// Comprobar linea de encima de la caja
		for (int i = 0; i < m && libreArriba == false; i++) {
			if (habitacion.get(caja[1] - 1).charAt(i) == '#' || habitacion.get(caja[1] - 1).charAt(i) == '!'
					|| habitacion.get(caja[1] - 1).charAt(i) == '-') {
				libreArriba = true;
			}
		}

		// Comprobar linea de la derecha de la caja
		for (int i = 0; i < n && libreDerecha == false; i++) {
			if (habitacion.get(i).charAt(caja[0] + 1) == '#' || habitacion.get(i).charAt(caja[0] + 1) == '!'
					|| habitacion.get(i).charAt(caja[0] + 1) == '-') {
				libreDerecha = true;
			}
		}

		// Comprobar linea debajo de la caja
		for (int i = 0; i < m && libreAbajo == false; i++) {
			if (habitacion.get(caja[1] + 1).charAt(i) == '#' || habitacion.get(caja[1] + 1).charAt(i) == '!'
					|| habitacion.get(caja[1] + 1).charAt(i) == '-') {
				libreAbajo = true;
			}
		}

		// Comprobar linea de la izquierda de la caja
		for (int i = 0; i < n && libreIzquierda == false; i++) {
			if (habitacion.get(i).charAt(caja[0] - 1) == '#' || habitacion.get(i).charAt(caja[0] - 1) == '!'
					|| habitacion.get(i).charAt(caja[0] - 1) == '-') {
				libreIzquierda = true;
			}
		}

		// VER LAS COMBIANCIONES DE LIBRES QUE SON SOLUCION

		if (destino[0] == caja[0] && destino[1] < caja[1]) {
			if (!libreArriba) {
				return false;
			} else {
				return true;
			}
		} else if (destino[0] > caja[0] && destino[1] < caja[1]) {
			if (!libreArriba && !libreDerecha) {
				return false;
			} else {
				return true;
			}
		} else if (destino[0] > caja[0] && destino[1] == caja[1]) {
			if (!libreDerecha) {
				return false;
			} else {
				return true;
			}
		} else if (destino[0] > caja[0] && destino[1] > caja[1]) {
			if (!libreDerecha && !libreAbajo) {
				return false;
			} else {
				return true;
			}
		} else if (destino[0] == caja[0] && destino[1] > caja[1]) {
			if (!libreAbajo) {
				return false;
			} else {
				return true;
			}
		} else if (destino[0] < caja[0] && destino[1] > caja[1]) {
			if (!libreAbajo && !libreIzquierda) {
				return false;
			} else {
				return true;
			}
		} else if (destino[0] < caja[0] && destino[1] == caja[1]) {
			if (!libreIzquierda) {
				return false;
			} else {
				return true;
			}
		} else if (destino[0] < caja[0] && destino[1] < caja[1]) {
			if (!libreIzquierda && !libreArriba) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}

	}

	private int[][] cadenaACoord(String solParc) {

		int[][] coor = new int[cajas.length * 2][2];
		int contador = 0;
		String temp = "";

		String[] puntos = solParc.split("|");

		for (int i = 0; i < puntos.length; i++) {
			try {
				Integer.parseInt(puntos[i]);
				temp += puntos[i];
			} catch (Exception e) {
				if (puntos[i].equals("|")) {

					coor[contador][1] = Integer.parseInt(temp);
					contador++;
				} else {
					coor[contador][0] = Integer.parseInt(temp);
				}

				temp = "";

			}
		}

		return coor;
	}

	private ArrayList<String> calculadorSolucionesParciales() {
		// TODO Auto-generated method stub

		// generar soluciones parciales y nos quedamos con la corta

		// 1. generar todas las combinaciones de cajas y destinos
		ArrayList<String> combinaciones = new ArrayList<String>();
		ArrayList<String> permCajas = new ArrayList<String>();
		LinkedList<String> cajasTemp = new LinkedList<String>();
		ArrayList<String> permDestinos = new ArrayList<String>();
		LinkedList<String> destinosTemp = new LinkedList<String>();

		for (int i = 0; i < cajas.length; i++) {
			cajasTemp.add(cajas[i][0] + "," + cajas[i][1]);
		}

		for (int i = 0; i < destinos.length; i++) {
			destinosTemp.add(destinos[i][0] + "," + destinos[i][1]);
		}

		permutarSimple("", cajasTemp, permCajas);

		permutarSimple("", destinosTemp, permDestinos);

		ArrayList<ArrayList<String>> permCajas2 = new ArrayList<ArrayList<String>>();
		ArrayList<String> permCajasTemp = new ArrayList<String>();
		int contador = 0;
		for (int i = 0; i < permCajas.size(); i++) {
			permCajas.set(i, permCajas.get(i) + "|");
			for (int j = 0; j < permCajas.get(i).length(); j++) {
				if (permCajas.get(i).charAt(j) == '|' && j != 0) {
					String s = permCajas.get(i).substring(contador + 1, j);
					permCajasTemp.add(s);
					contador = j;
				}
			}
			ArrayList<String> clon = new ArrayList<String>();
			clon = (ArrayList<String>) permCajasTemp.clone();
			permCajas2.add(clon);
			permCajasTemp.removeAll(permCajasTemp);
			contador = 0;

		}

		ArrayList<ArrayList<String>> permDestinos2 = new ArrayList<ArrayList<String>>();
		ArrayList<String> permDestinosTemp = new ArrayList<String>();
		contador = 0;
		for (int i = 0; i < permDestinos.size(); i++) {
			permDestinos.set(i, permDestinos.get(i) + "|");
			for (int j = 0; j < permDestinos.get(i).length(); j++) {
				if (permDestinos.get(i).charAt(j) == '|' && j != 0) {
					String s = permDestinos.get(i).substring(contador + 1, j);
					permDestinosTemp.add(s);
					contador = j;
				}
			}
			ArrayList<String> clon2 = new ArrayList<String>();
			clon2 = (ArrayList<String>) permDestinosTemp.clone();
			permDestinos2.add(clon2);
			permDestinosTemp.removeAll(permDestinosTemp);
			contador = 0;

		}

		// 1.3 union de combinaciones teniendo en cuenta que siempre es
		// caja-destini-caja-destino...

		for (int i = 0; i < permCajas2.size(); i++) {

			for (int j = 0; j < permDestinos2.size(); j++) {
				String s = "";
				// combinaciones.add(permCajas2.get(i).get(j));

				for (int k = 0; k < permDestinos2.get(j).size(); k++) {

					s = s + permCajas2.get(i).get(k) + "|" + permDestinos2.get(j).get(k) + "|";
				}
				combinaciones.add(s);
				s = "";

			}

		}
		// Orden de factorial(n)=k numero combinaciones cajas o destinos, n numero de
		// elementod
		// k*k= combinaciones totales
		return combinaciones;
	}

	// http://chuwiki.chuidiang.org/index.php?title=Escribir_permutaciones_en_Java
	private void permutarSimple(String a, LinkedList<String> conjunto, ArrayList<String> perm) {
		// TODO Auto-generated method stub

		if (conjunto.size() == 1) {
			perm.add(a + "|" + conjunto.get(0));
			// System.out.println(a+"|"+conjunto.get(0));
		}
		for (int i = 0; i < conjunto.size(); i++) {
			String b = conjunto.remove(i);
			permutarSimple(a + "|" + b, conjunto, perm);
			conjunto.add(i, b);
		}
	}

	public void printHabitacion() {

		int contadorCajas = 0;
		int contadorDestinos = 0;
		boolean esCaja = false;
		boolean esDestino = false;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {

				if (contadorCajas < cajas.length) {
					if (j == cajas[contadorCajas][0] && i == cajas[contadorCajas][1]) {
						esCaja = true;
					} else {
						esCaja = false;
					}
				} else {
					esCaja = false;
				}

				if (contadorDestinos < destinos.length) {
					if (j == destinos[contadorDestinos][0] && i == destinos[contadorDestinos][1]) {
						esDestino = true;
					} else {
						esDestino = false;
					}
				} else {
					esDestino = false;
				}

				// imprimir robot
				if (robot.getX() == j && robot.getY() == i) {
					if (habitacion.get(i).charAt(j) == '0' && j == m - 1) {
						System.out.println("@");
					} else {
						System.out.print("@");
					}

					// imprimir cajas
				} else if (esCaja) {
					System.out.print("#");
					contadorCajas++;
					// imprimir destinos
				} else if (esDestino) {
					System.out.print("!");
					contadorDestinos++;

					// imprimir resto de habitacion
				} else {
					if (habitacion.get(i).charAt(j) == '0' && j == m - 1) {
						System.out.println(habitacion.get(i).charAt(j));
					} else {
						System.out.print(habitacion.get(i).charAt(j));
					}
				}
			}
		}
	}


	//	public void printHabitacion2() {
	//
	//		char[][] habitacionChar = new char[n][m];
	//		int[] cajaTemp = new int[2];
	//		int[] destinoTemp = new int[2];
	//
	//		int contadorCajas = 0;
	//		int contadorDestinos = 0;
	//
	//		for (int i = 0; i < habitacionChar.length; i++) {
	//			for (int j = 0; j < habitacionChar[0].length; j++) {
	//				cajaTemp[0] = cajas[contadorCajas][0];
	//				cajaTemp[1] = cajas[contadorCajas][1];
	//				destinoTemp[0] = destinos[contadorDestinos][0];
	//				destinoTemp[1] = destinos[contadorDestinos][1];
	//				habitacionChar[i][j] = habitacion.get(i).charAt(j);
	//				if (cajaTemp[0] == j && cajaTemp[1] == i) {
	//					habitacionChar[i][j] = '#';
	//					if (contadorCajas < cajas.length-1)
	//						contadorCajas++;
	//				}
	//				if (destinoTemp[0] == j && destinoTemp[1] == i) {
	//					habitacionChar[i][j] = '!';
	//					if (contadorDestinos < destinos.length-1)
	//						contadorDestinos++;
	//				}
	//				if(robot.getY()==i && robot.getX()==j) {
	//					habitacionChar[i][j] = '@';
	//				}
	//				System.out.print(habitacionChar[i][j]);
	//			}
	//			System.out.println();
	//		}
	//
	//		
	//	}
}