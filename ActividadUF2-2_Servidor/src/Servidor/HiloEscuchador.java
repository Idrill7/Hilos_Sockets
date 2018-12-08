package Servidor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
/**
 * Esta es la clase HiloEscuchador, implementa Runnable para que la clase pueda crear los hilos y estos ejecuten  la funcion run() una vez iniciados en el constructor
 * En esta clase cada vez que se cree un objeto de la misma, mediante el constructor se creara un objeto de la clase Thread, un hilo, que tendra una referencia al mismo objeto creado de esta clase
 * Por lo que cada vez que sea crea un objeto de la clase HiloEscuchador, se creara un hilo de ese objeto mediante el constructor, siendo este referenciado por this
 * La finalidad del run() es cambiar el nombre del hilo (del cliente) en base al que el SocketCliente determine y tambien responder cuantos caracteres tiene
 * el mensaje que envia tras identificarse
 *
 */
public class HiloEscuchador implements Runnable {
	// Declaramos las propiedades de la clase HiloEscuchador
	// Se guarda una referencia en el atributo hilo que apunta a un objeto de la clase Thread 
	private Thread hilo;
	// Creamos una variable estatica que pertenece a la clase y a la region de
			// memoria conocida "memoria inmortal" de java, esta se ira sumando en el constructor por cada hilo creado ( por cada cliente)
	private static int numCliente = 0;
	// Guardamos una referencia en el atributo enchufeAlCliente que apunta a un objeto de la clase Socket
	private Socket enchufeAlCliente;
	/**
	 * Este es el constructor de la clase HiloEscuchador, sera compuesto por los siguientes parametros
	 * @param cliente
	 * 					que hace referencia al objeto de la clase Socket
	 */
	public HiloEscuchador(Socket cliente) {
		// Aumentamos la variable cada vez que se crea un objeto de la clase HiloEscuchador
		numCliente++;
		// Creamos un objeto de la clase Thread, el cual tendra como referencia el mismo objeto de la clase HiloEscuchador, 
		//y como nombre el Cliente y numeroCliente
		hilo = new Thread(this, "Cliente"+numCliente);
		// Hacemos una  referencia mediante this, esta apuntara  al Socket del cliente creado en la clase Servidor cuando se ejecuta el metodo accept() y acepta una conexion
		this.enchufeAlCliente = cliente;
		// Iniciamos el hilo
		hilo.start();
	}
	/**
	 * Sobreescribimos el metodo run(), una vez el hilo sea iniciado sera ejecutado
	 */
	@Override
	public void run() {
		
		System.out.println("Estableciendo comunicacion con " + hilo.getName());
		/**
		 * Introducimos una sentencia try/catch, en el try obtendremos el texto mediante los stream de entrada y de salida
		 * Tambien estableceremos un nombre al hilo segun la identificacion del usuario y  si el texto que envia despues de la identificacion
		 * es "FIN", acabara la conexion, pero si el texto no es "FIN",escribira el numero de caracteres de ese texto al cliente
		 *  En el catch capturamos las excepciones y las imprimimos por pantalla
		 */

		try {
			// Necesitamos unos flujos de entrada y salida para obtener los datos del Socket del cliente y para enviarle los datos
						// Obtenemos el stream de entrada por el que va a viajar la informacion que nos va a enviar el Socket del cliente  mediante el metodo getInputStream
			InputStream entrada = enchufeAlCliente.getInputStream();
			// Obtenemos el stream de salida por el que van a enviarse los datos al Socket del cliente mediante el metodo getOutputStream
			OutputStream salida = enchufeAlCliente.getOutputStream();
			// Creamos la variable String  texto y la inicializamos a un texto vacio 
			String texto = "";	
			/**
			 * Antes de entrar en el bucle while, crearemos un array de 100 bytes, leeremos el primer mensaje que nos entre del cliente
			 * este primer mensaje sera el nombre del cliente. 
			 * Lo que se hace es con el metodo setName se le pone al hilo el nombre que ha tecleado el cliente ( en la clase SocketCliente se le pide el nombre como primer mensaje)
			 * Por lo que cada hilo tendra de nombre el indicado por ese cliente.
			 * Si el cliente escribe FIN en vez de un nombre, se cerraran la conexion directamente
			 */
			
			// creamos un array de 100 bytes, para recoger el mensaje de entrada del cliente 
			byte[] mensajeNombre = new byte[100];
			// leemos el texto que viene del stream de  entrada y lo guardamos en el array de bytes
			entrada.read(mensajeNombre);
			// en la variable texto, guardamos un objeto de la clase String y le pasaremos como parametro el array de bytes
			texto = new String(mensajeNombre);
			if (texto.trim().equals("FIN")) {
				// Si el cliente escribe FIN en vez de su nombre, cerrara la conexion por lo que lo imprimiremos en el servidor
				System.out.println("Se ha cerrado la comunicacion con el " + hilo.getName().trim());
			} else if (!texto.trim().equals("FIN")) {
				// Con el metodo setName le indicaremos que el  nombre del hilo sera el texto anterior
				hilo.setName(texto);
				// Imprimos por el servidor el numero del cliente y el nombre del hilo, que sera su nombre
				System.out.println("Idenfiticado como: " + hilo.getName().trim());
			}
		
			/**
			 * En este while  leera el mensaje que llega por el stream de entrada, despues de haber asignado el nombre del hilo
			 * Si el texto escrito es distinto a FIN, enviamos mediante el stream de salida los caracteres que tiene ese texto
			 *  Como el texto lo hemos iniciado a "", va a entrar en el bucle, va a leer el mensaje y si escribimos FIN , se despedira del cliente,
			 *  la proxima vez que este intenta entrar en el bucle, texto sera FIN, no entrara en el bucle y entonces se cerraran las conexiones
			 */
			while (!texto.trim().equals("FIN")) {
				// creamos un array de 100 bytes, para recoger el mensaje de entrada del cliente 
				byte[] mensaje = new byte[100];
				// leemos el texto que viene del stream de  entrada y lo guardamos en el array de bytes mediante el metodo  read()
				entrada.read(mensaje);
				// en la variable texto, guardamos un objeto de la clase String y le pasaremos como parametro el array de bytes, el mensaje
				texto = new String(mensaje);
				// si escribimos "FIN", entrara en este else if y saldra del bucle,  no volvera a entrar
				if (texto.trim().equals("FIN")) {
					// nos despedimos del cliente , le escribimos mediante el stream de salida y el metodo write()
					salida.write("Hasta pronto, gracias por establecer conexion".getBytes());
					System.out.println(hilo.getName().trim() +" ha cerrado la comunicacion");
				} else {
					// Si no es FIN, imprimimos en el servidor lo que nos dice el cliente y le enviamos los caracteres
					// que tiene el texto mediante el stream de salida , estos se cuentan mendiante el metodo lenght() 
					System.out.println(hilo.getName().trim()  + " dice: " + texto);
					salida.write(("Tu mensaje tiene " + texto.trim().length() + " caracteres").getBytes());
				}
			}
			// cuando sale del while, es decir, cuando el cliente escribe "FIN"
			// cerramos el stream de entrada 
			entrada.close();
			// cerramos el stream de salida
			salida.close();
			//cerramos el Socket del cliente que se creo en la clase Servidor
			enchufeAlCliente.close();
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
	}
}