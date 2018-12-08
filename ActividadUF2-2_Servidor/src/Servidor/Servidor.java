package Servidor;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * 
 * @author Alejandro Gonzalez Casado
 * @version 1.0
 * 
 *          Esta es la clase Servidor, en ella crearemos un Objeto de la clase
 *          ServerSocket y lo enlazaremos a una dirección IP junto a un puerto
 *          Con un demonio aceptaremos  todas las solicitudes que vaya recibiendo al servidor 
 *          por parte de  un Socket (cliente), crearemos un Socket  y arrancaremos un hilo a cada conexion aceptada
 *          mediante la creacion de un objeto de la clase HiloEscuchador,  al cual le pasaremos
 *          el objeto Socket creado al aceptar una conexion 
 */
public class Servidor {

	public static void main(String[] args) {
		System.out.println("APLICACION DE SERVIDOR MULTITAREA");
		System.out.println("----------------------------------");
		// Creamos la variable que sera referencia del objeto de la clase ServerSocket
		ServerSocket servidor = null;
		/**
		 * Introducimos una sentencia try/catch, en el try,  la creacion del objeto ServerSocket
		 * y un objeto de la clase InetSocketAdress que sera
		 * el enlace a direccion y el puerto donde se podra establecer la conexion con
		 * el ServerSocket. El while sera un demonio que estara en continuo funcionamiento, este aceptara 
		 * las conexiones de los clientes y a su vez crearemos un objeto de la clase
		 * HiloEscuchador, arrancando un hilo,  por cada cliente que se conecte al servidor
		 *  En el catch capturamos las excepciones y las imprimimos por pantalla
		 */
		try {
			// Creamos un objeto de la clase ServerSocket que sera el Socket del servidor
			servidor = new ServerSocket();

			// Mediante la creacion del objeto de la clase InetSocketAdress determinaremos
			// en los parametros la IP y el puerto de conexion 
			InetSocketAddress direccion = new InetSocketAddress("localhost",2000);
			// Enlazamos el servidor a la direccion anterior mediante el metodo bind()
			// El servidor arranca y empezara a escuchar peticiones que llegaran a esa direccion y puerto
			servidor.bind(direccion);

			System.out.println("Servidor listo para aceptar solicitudes");
			System.out.println("Dirección IP: " + direccion.getAddress());
			// En este while creamos un bucle conocido como "demonio", de forma que sea infinito mientras dure el Servidor arrancado
			while (true) {
				// Esperamos a que llegue un cliente
				
				// El servidor.accept() lo que hace es que cuando se acepta una conexion que le entra al servidor
				//  crea un objeto de tipo Socket 
				Socket enchufeAlCliente = servidor.accept();
				System.out.println("Comunicacion entrante");
				// Creamos un objeto de la clase HiloEscuchador, este arranca un hilo para cada
				// cliente, a este le pasamos como parametro el Socket creado anteriormente
				new HiloEscuchador(enchufeAlCliente);
			}
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}