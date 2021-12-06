import java.util.ArrayList;
import java.util.Scanner;

public class MoviendoCajas {

	private static int n;
	private static int m;
	private static ArrayList<String> habitacion;
	private static int coordXRobot;
	private static int coordYRobot;
	private static int numCajas;
	private static int numDestinos;
	private static int[][] cajas;
	private static int[][] destinos;
	private static String simboloRobotInicial;
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		n=0;
		m=0;
		
		habitacion = new ArrayList<String>();

		// Almacena la wntrada t hace primeras comprobaciones de n y m, pero no revisa si hay
		//robot, cajas y destinos
		boolean entradaValida;
		
		entradaValida = leerEntrada();
		entradaValida = entradaValida & compruebaHabitacion();
		
		if(!entradaValida) {
			System.out.println("Entrada mal formada");
		} else {
		//La entrada esta bien formada
			
			guardarCajasYDestinos();
			
			Habitacion hab = new Habitacion(habitacion,coordXRobot,coordYRobot,simboloRobotInicial,
					n,m, cajas, destinos);
		    hab.solucionarHabitacion();
		}

	}

	private static void guardarCajasYDestinos() {
		
		cajas = new int[numCajas][2];
		destinos = new int[numDestinos][2];
		
		int contadorCajas = 0;
		int contadorDestinos = 0;
	
		for(int i=0; i<n; i++) {
			for(int j=0; j<m; j++) {
				if(habitacion.get(i).charAt(j)=='#') {
					cajas[contadorCajas][0]=j;
					cajas[contadorCajas][1]=i;
					habitacion.set(i, habitacion.get(i).substring(0, j)+"-"+habitacion.get(i).substring(j+1,habitacion.get(i).length()));
					contadorCajas++;
					
				} else if(habitacion.get(i).charAt(j)=='!') {
					destinos[contadorDestinos][0]=j;
					destinos[contadorDestinos][1]=i;
					habitacion.set(i, habitacion.get(i).substring(0, j)+"-"+habitacion.get(i).substring(j+1,habitacion.get(i).length()));
					contadorDestinos++;
				}
			}
		}
		
	}

	private static boolean compruebaHabitacion() {
		
		boolean robot = false;
		boolean relacionCD = false;
		
		//los generalizo
		//int numCajas =0;
		//int numDestinos=0;
		
		for(int i= 0; i<habitacion.size(); i++) {
			for(int j = 0; j<habitacion.get(i).length();j++) {
				if (habitacion.get(i).charAt(j)=='@' || habitacion.get(i).charAt(j)=='+' ) {
					robot = true;
					coordXRobot = j;
					coordYRobot = i;
					if(habitacion.get(i).charAt(j)=='@') {
						simboloRobotInicial="@";
						habitacion.set(i, habitacion.get(i).substring(0, j)+"-"+habitacion.get(i).substring(j+1,habitacion.get(i).length()));
					} else {
						simboloRobotInicial="!";
						habitacion.set(i, habitacion.get(i).substring(0, j)+"!"+habitacion.get(i).substring(j+1,habitacion.get(i).length()));
					}
				
						
				}
				if (habitacion.get(i).charAt(j)=='#') {
					numCajas++;
				}
				if (habitacion.get(i).charAt(j)=='!') {
					numDestinos++;
				}
			}
		}
	
		return numCajas == numDestinos && robot && numCajas!=0;
	}

	private static boolean leerEntrada() {
		// TODO Auto-generated method stub

		String primeraLinea="";

		boolean entradaBienFormada = true;

		Scanner scan = new Scanner(System.in);

		primeraLinea=scan.nextLine();

		String[] nym = primeraLinea.split(" ");
		try {
			n = Integer.parseInt(nym[0]);
			m = Integer.parseInt(nym[1]);

		} catch (Exception e) {
			entradaBienFormada = false;
		}

		//Con menos espacio no puede existir una solucion
		if(n<3 || m<3) {
			entradaBienFormada = false;
		}

		System.out.println(n + " " + m);

		String temp = "";
		int valorLinea = 0;

		for (int i=0;temp!="stop";i++) {
			try {
				temp=scan.nextLine();
				valorLinea = Integer.parseInt(temp);
				if (valorLinea==0 && i!=0) {
					habitacion.add(temp);
					temp = "stop";		
				} else {
					habitacion.add(temp);
				}
			} catch (Exception e) {
				habitacion.add(temp);

			}

		}

		scan.close();

		//Comprueba que el tamaño de la matriz corresponde con n y m
 //Comprueba n
		if(habitacion.size()!=n)
			entradaBienFormada = false;
//Comprueba m
		if(entradaBienFormada == true) {

			int contador=0;
			int espacios = 0;
			boolean salir = false;

			while(!salir) {
				if (contador==habitacion.size()-1) {
					salir=true;
				}

				if(habitacion.get(contador).length()!=m) {
					entradaBienFormada=false;
					salir=true;
					//Comprobar que no son todo blancos

				} else {
					for(int i=0; i<habitacion.get(contador).length();i++) {
						if(habitacion.get(contador).charAt(i)==' ') {
							espacios++;
						}
					}
					if(espacios==m) {
						entradaBienFormada = false;
						salir=true;
					}

					espacios=0;

				}

				contador++;
			}
		}

		return entradaBienFormada;
	}

}