import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Habitacion {

	private ArrayList<String> habitacion;
	private Robot robot;
	private int n;
	private int m;
	private int[][] cajas;
	private int[][] destinos;

	public Habitacion(ArrayList<String> mapa, int coorXRob, int coorYRob, String simboloRobot,
			int filas, int columnas, int[][] coordCajas, int[][] coordDestinos) {
		habitacion = mapa;
		robot = new Robot(coorXRob, coorYRob, simboloRobot);
		n=filas;
		m=columnas;
		//cajas =  new int[coordCajas.length][2];
		cajas = coordCajas;
		destinos = coordDestinos;
	}

	public void solucionarHabitacion() {

		// ELIMINAR BLOQUE
		this.printHabitacion();	  
		System.out.println("cajas");
		for (int i=0; i<cajas.length; i++) { System.out.println(cajas[i][0] + " " +
				cajas[i][1]); }
		System.out.println("destinos");
		for (int i=0; i<destinos.length; i++) { System.out.println(destinos[i][0] + " "
				+ destinos[i][1]); }
		
		//Devuelvo array con todas las combinaciones de movimientos
		ArrayList<String> parciales = new ArrayList<String>();
			
		parciales = calculadorSolucionesParciales();

		boolean[] esSolucion = {true};
		boolean[] esMasCorto = {true};
		String regRobotTemp = "x";
		String regRobotSolucion = "";
		int originalX = robot.getX();
		int originalY = robot.getY();
		
		for (int i=0; i<Math.pow(cajas.length,destinos.length); i++) {
			regRobotTemp += "x";
		}
		
		//Mover robot segun combinaciones
		for (int i=0; i<parciales.size(); i++) {
			vueltaAtras(parciales.get(i), esSolucion, esMasCorto, regRobotTemp);
			//Aqui compruebo si devuelve que hay solucion o si el
			//registro del robot es mas largo. Restablece estas
			//variables
			if(esSolucion[0] && esMasCorto[0]) {
				regRobotTemp = robot.getHistorialMovimientos();
				regRobotSolucion = regRobotTemp.toString();
			}
			
			//Reiniciar registro robot y almanarlo en otro sitio
			//Devolver al robot y las cajas a la posicion original
			esSolucion[0]=true;
			esMasCorto[0]=true;
			robot.removeHistorialMovimientos();
			robot.setX(originalX);
			robot.setY(originalY);
		}

	}

	private void vueltaAtras(String solParc, boolean[] esSolucion, boolean[] esMasCorto, String regRobot) {
		
		int[][] camino = new int[cajas.length*2][2];
		camino = cadenaACoord(solParc);
		int contador = 0;
		
		int[][] cajasTemp = new int[camino.length/2][2];
		int[][] destinosTemp = new int[camino.length/2][2];
		
		for(int i=0, j=0; i<cajasTemp.length*2; i+= 2, j++) {
			cajasTemp[j]=camino[i];
			destinosTemp[j]=camino[i+1];
		}
		
		while(esSolucion[0] && esMasCorto[0] && contador<camino.length) {
			
			//Comprueba si hay solucion para esas cajas y destinos en esa habitacion y si
			// el robot se puede mover.
			//Las cajas y destinos las actualiza el robot.
			//Puede recibir cajas ya solucionadas.
			
			if(!existeSolucion(cajasTemp, destinosTemp, contador)) {
				esSolucion[0] = false;
			} else if(robot.getHistorialMovimientos().length()>regRobot.length()) {
				esMasCorto[0] = false;
			} else {
				//Mueve el robot a la siguiente caja y la lleva al destino registrando
				//los movimientos. Actualiza cajasTemo y destinosTemp
				//IMPORTANTE!!!La caja en el destino se indica en la habitacion
				robot.moverRobCajaDestino(camino[contador], camino[contador+1], 
						cajasTemp, destinosTemp, habitacion);
				
			}
			
			
			
			contador += 2;
		}
		
	}

	private boolean existeSolucion(int[][] cajasTemp, int[][] destinosTemp, int indice) {
		
		//Comprobar que el robot se puede mover
		boolean robotPuedeMoverse = robot.puedoMoverme(habitacion, cajasTemp, destinosTemp);
		
		//Comprobar que las cajas se pueden mover. Tener en cuenta cajas ya resueltas
		boolean cajaMov = cajaPuedeMoverse(cajasTemp, destinosTemp, indice);
		
		//Compromar que los destinos son accesibles. Tener en cuenta destinos ya resueltos.
		
		return false;
	}

	private boolean cajaPuedeMoverse(int[][] cajasTemp, int[][] destinosTemp, int indice) {
		// TODO Auto-generated method stub
		
		if(indice!=0) {
			indice = indice / 2;
		}
		
		boolean arriba = true;
		boolean derecha = true;
		boolean abajo = true;
		boolean izquierda = true;
		
		
		//cuatro posibilidades para que una caja no pueda moverse
		
		//arriba
		if (habitacion.get(cajasTemp[indice][1]-1).charAt(cajasTemp[indice][0]) == '0' ||
			habitacion.get(cajasTemp[indice][1]-1).charAt(cajasTemp[indice][0]) == '1' ||
			habitacion.get(cajasTemp[indice][1]-1).charAt(cajasTemp[indice][0]) == '*') {
			arriba = false;
		}
		
		//derecha
		if (habitacion.get(cajasTemp[indice][1]).charAt(cajasTemp[indice][0]+1) == '0' ||
				habitacion.get(cajasTemp[indice][1]).charAt(cajasTemp[indice][0]+1) == '1' ||
				habitacion.get(cajasTemp[indice][1]).charAt(cajasTemp[indice][0]+1) == '*') {
				derecha = false;
			}
		
		//abajo
		if (habitacion.get(cajasTemp[indice][1]+1).charAt(cajasTemp[indice][0]) == '0' ||
				habitacion.get(cajasTemp[indice][1]+1).charAt(cajasTemp[indice][0]) == '1' ||
				habitacion.get(cajasTemp[indice][1]+1).charAt(cajasTemp[indice][0]) == '*') {
				abajo = false;
			}
		
		//izquierda
		if (habitacion.get(cajasTemp[indice][1]).charAt(cajasTemp[indice][0]-1) == '0' ||
				habitacion.get(cajasTemp[indice][1]).charAt(cajasTemp[indice][0]-1) == '1' ||
				habitacion.get(cajasTemp[indice][1]).charAt(cajasTemp[indice][0]-1) == '*') {
				izquierda = false;
			}
		
		//comprobar las posibilidades
		// arriba derecha
		if(!arriba && !derecha) {
		return false;
		// derecha abajo
		} else if(!derecha && !abajo) {
			return false;
		// abajo izquierda
		} else if(!abajo && !izquierda) {
			return false;
		//izquierda arriba
		} else if(!izquierda && !arriba) {
			return false;
		} else {
			return true;
		}
		
	}

	private int[][] cadenaACoord(String solParc) {
		
		int[][] coor = new int[cajas.length*2][2];
		int contador = 0;
		String temp = "";
		
		String[] puntos = solParc.split("|");
		
		for (int i=0; i<puntos.length;i++) {
			try {
				Integer.parseInt(puntos[i]);
				temp += puntos[i];
			}catch (Exception e) {
				if(puntos[i].equals("|")) {
					
					coor[contador][1]=Integer.parseInt(temp);
					contador++;
				} else {
					coor[contador][0]=Integer.parseInt(temp);
				}
				
				temp="";
				
			}
		}
		
		return coor;
	}

	private ArrayList<String> calculadorSolucionesParciales() {
		// TODO Auto-generated method stub

		//generar soluciones parciales y nos quedamos con la corta

		//1. generar todas las combinaciones de cajas y destinos
		ArrayList<String> combinaciones = new ArrayList<String>();
		ArrayList<String> permCajas = new ArrayList<String>();
		LinkedList<String> cajasTemp = new LinkedList<String>();
		ArrayList<String> permDestinos = new ArrayList<String>();
		LinkedList<String> destinosTemp = new LinkedList<String>();

		for(int i=0; i<cajas.length;i++) {
			cajasTemp.add(cajas[i][0]+","+cajas[i][1]);
		}
		
		for(int i=0; i<destinos.length;i++) {
			destinosTemp.add(destinos[i][0]+","+destinos[i][1]);
		}

		permutarSimple("",cajasTemp,permCajas);

		permutarSimple("",destinosTemp,permDestinos);
		
		ArrayList<ArrayList<String>> permCajas2 = new ArrayList<ArrayList<String>>();
		ArrayList<String> permCajasTemp = new ArrayList<String>();
		int contador = 0;
		for (int i=0; i<permCajas.size();i++) {
			permCajas.set(i, permCajas.get(i)+"|");
			for (int j=0; j<permCajas.get(i).length();j++) {
				if(permCajas.get(i).charAt(j)=='|' && j!=0) {
					String s = permCajas.get(i).substring(contador+1, j);
					permCajasTemp.add(s);
					contador = j;
				}
			}
			ArrayList<String> clon = new ArrayList<String>();
			clon = (ArrayList<String>) permCajasTemp.clone();
			permCajas2.add(clon);
			permCajasTemp.removeAll(permCajasTemp);
			contador=0;
			
		}
		
		ArrayList<ArrayList<String>> permDestinos2 = new ArrayList<ArrayList<String>>();
		ArrayList<String> permDestinosTemp = new ArrayList<String>();
		contador = 0;
		for (int i=0; i<permDestinos.size();i++) {
			permDestinos.set(i, permDestinos.get(i)+"|");
			for (int j=0; j<permDestinos.get(i).length();j++) {
				if(permDestinos.get(i).charAt(j)=='|' && j!=0) {
					String s = permDestinos.get(i).substring(contador+1, j);
					permDestinosTemp.add(s);
					contador = j;
				}
			}
			ArrayList<String> clon2 = new ArrayList<String>();
			clon2 = (ArrayList<String>) permDestinosTemp.clone();
			permDestinos2.add(clon2);
			permDestinosTemp.removeAll(permDestinosTemp);
			contador=0;
			
		}
		
		//1.3 union de combinaciones teniendo en cuenta que siempre es caja-destini-caja-destino...

		
		for(int i=0; i<permCajas2.size();i++) {
			
			for(int j=0; j<permDestinos2.size();j++) {
				String s = "";
				//combinaciones.add(permCajas2.get(i).get(j));
				
				for (int k=0; k<permDestinos2.get(j).size();k++) {
					
					s = s + permCajas2.get(i).get(k)+"|"+permDestinos2.get(j).get(k)+"|";
				}
				combinaciones.add(s);
				s="";
				
			}
			
		}
		//Orden de factorial(n)=k numero combinaciones cajas o destinos, n numero de elementod
		//k*k= combinaciones totales
		return combinaciones;
	}

	//http://chuwiki.chuidiang.org/index.php?title=Escribir_permutaciones_en_Java
	private void permutarSimple(String a, LinkedList<String> conjunto, ArrayList<String> perm) {
		// TODO Auto-generated method stub

		if (conjunto.size()==1)
		{
			perm.add(a+"|"+conjunto.get(0));
			//System.out.println(a+"|"+conjunto.get(0));
		}
		for (int i=0;i<conjunto.size();i++)
		{
			String b = conjunto.remove(i);
			permutarSimple (a+"|"+b, conjunto, perm);
			conjunto.add(i,b);
		}
	}

	public void printHabitacion() {

		int contadorCajas = 0;
		int contadorDestinos = 0;
		boolean esCaja=false;
		boolean esDestino=false;

		for (int i=0; i<n ;i++){
			for (int j=0; j<m ;j++){

				if(contadorCajas<cajas.length) {
					if(j==cajas[contadorCajas][0] && i==cajas[contadorCajas][1]) {
						esCaja=true;
					} else {
						esCaja=false;
					}
				} else {
					esCaja = false;
				}

				if(contadorDestinos<destinos.length) {
					if(j==destinos[contadorDestinos][0] && i==destinos[contadorDestinos][1]) {
						esDestino=true;
					} else {
						esDestino=false;
					}
				} else {
					esDestino = false;
				}

				//imprimir robot
				if (robot.getX()==j && robot.getY()==i) {
					if (habitacion.get(i).charAt(j)=='0' && j==m-1) {
						System.out.println("@");
					} else {
						System.out.print("@");
					}

					//imprimir cajas
				} else if (esCaja) {
					System.out.print("#");
					contadorCajas++;	
					//imprimir destinos
				} else if (esDestino) {
					System.out.print("!");
					contadorDestinos++;

					//imprimir resto de habitacion		
				} else {
					if (habitacion.get(i).charAt(j)=='0' && j==m-1) {
						System.out.println(habitacion.get(i).charAt(j));
					} else {
						System.out.print(habitacion.get(i).charAt(j));
					}
				}
			}
		}
	}

}