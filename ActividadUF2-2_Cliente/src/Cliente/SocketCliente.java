package Cliente;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
/**
 * Esta es la clase SocketCliente, en ella crearemos un objeto de la clase
 * Socket del cliente y le indicaremos la direccion IP y el puerto al que
 * va a realizar la conexion. Crearemos los canutos de entrada y salida de
 * datos para con ello enviar al servidor y recibir del mismo los datos que nos envie
 *
 */
public class SocketCliente {

	public static void main(String[] args) {
		System.out.println("        APLICACION CLIENTE");
		System.out.println("-----------------------------------");
		// Creamos un objeto de la clase Scanner, este guardara lo que escribamos en el teclado 
		// ( los inputStream )  mediante el System.in
		Scanner lector = new Scanner(System.in);
		/**
		 * Introducimos una sentencia try/catch. En el try, creamos el Socket del cliente junto a la
		 * direccion y puerto al que se va a  conectarse, tambien generaremos los flujos de
		 * entrada y salida de datos . 
		 * 
		 * 
		 * Lo primero que haremos sera pedirle al cliente que escriba su nombre, 
		 * ese texto sera enviado al servidor y sera guardado como nombre del hilo
		 * 
		 * Una vez escrito el nombre, comprobaremos el texto escrito en un bucle while, si es FIN
		 * cierra los flujos y el Socket, pero si es distinto a FIN, envia el mensaje al
		 * Servidor por el flujo de salida y recibe el mensaje de respuesta del Servidor mediante el
		 * flujo de entrada
		 * 
		 *  En el catch capturamos las excepciones y las imprimimos por pantalla
		 */
		try {
			// Creamos un objeto de la clase Socket que sera el del cliente
			Socket cliente = new Socket();
			// Mediante la creacion del objeto de la clase InetSocketAdress determinaremos
			// en los parametros la IP y el puerto de conexion a los que se conectara el cliente,
			// el ServerSocket estara enlazado y recibira peticiones de  esta direccion y puerto 
			InetSocketAddress direccionServidor = new InetSocketAddress("localhost",2000);
			System.out.println("Esperando a que el servidor acepte la conexion");
			// Con el metodo connect() haremos que el objeto Socket del cliente se conecte a
			// la IP y el puerto indicado en el objeto direccionServidor
			cliente.connect(direccionServidor);
			System.out.println("Comunicacion establecida con el servidor");
			// Necesitamos unos flujos de entrada y salida de la informacion
			// Obtenemos el stream de entrada por el que va a viajar la informacion
			//que nos va a devolver el ServerSocket mediante el metodo getInputStream
			InputStream entrada = cliente.getInputStream();
			// Obtenemos el stream de salida por el que van a enviarse 
			// los datos al ServerSocket mediante el metodo getOutputStream
			OutputStream salida = cliente.getOutputStream();
			// Creamos la variable String  texto y la inicializamos a un texto vacio 
			String texto = "";
			
			// Una vez el cliente se conecta, le pediremos que escriba su nombre, antes de entrar en el bucle
			//escribiremos este texto al servidor mediante el stream de salida y el metodo write()
			// Se le asignara este nombre al hilo creado en la clase HiloEscuchador del Servidor
			System.out.println("Escribe tu nombre (FIN para terminar):");
			texto= lector.nextLine();
			salida.write(texto.getBytes());
			/**
			 * En este bucle, se envia el texto escrito al servidor mediante el stream de salida
			 * y leemos  la respuesta del Servidor mediante el stream de entrada y la imprimimos 
			 * Si escibimos FIN, no vuelve a entrar en el bucle y cierra las conexiones de ese cliente
			 */
			while (!texto.equals("FIN")) {
				
				System.out.println("Escribe mensaje (FIN para terminar): ");
				// guardamos en la variable texto lo que ha escrito el cliente mediante el metodo nextLine() del Scanner lector, creado anteriormente
				texto = lector.nextLine();
				// le enviamos al servidor el texto escrito mediante el stream de salida del Socket del cliente y el metodo write()
				salida.write(texto.getBytes());
				// creamos un array de 100 bytes, como lo que responde el servidor es en bytes, lo recogemos en este array
				byte[] mensaje = new byte[100];
				System.out.println("Esperando respuesta ...... ");
				// leemos del servidor el mensaje que nos entra mediante el stream de entrada del Socket del cliente y el metodo read()
				entrada.read(mensaje);
				// imprimimos la respuesta del servidor, que sera un objeto String del mensaje que hemos leido anteriormente
				System.out.println("Servidor responde: " + new String(mensaje));
			}
			// Cuando se sale del bucle while, es  porque el cliente ha escrito FIN
						// cerramos el scanner
						lector.close();
						// cerramos el stream de entrada 
						entrada.close();
						// cerramos el stream de salida
						salida.close();
						// cerramos la conexion del Socket con ese cliente
						cliente.close();
			
			System.out.println("Comunicacion cerrada");
			
		} catch (UnknownHostException e) {
			System.out.println("No se puede establecer comunicacion con el servidor");
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println("Error de E/S");
			System.out.println(e.getMessage());
		}  
	}

}