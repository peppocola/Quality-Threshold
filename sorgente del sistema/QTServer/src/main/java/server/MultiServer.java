package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Gestisce le richieste dei client delegando la gestione della singola
 * richiesta a ServerOneClient.
 */
public class MultiServer {

	private int port;

	/**
	 * Inizializza la porta.
	 * 
	 * @param port numero di porta su cui il server è in ascolto
	 */
	public MultiServer(final int port) {
		this.port = port;
		run();
	}

	public void run() {
		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
			while (true) {
				System.out.println("Aspetto...");
				final Socket socket = server.accept();
				new ServerOneClient(socket);
				System.out.println("Servito client " + socket.getInetAddress());
			}
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			try {
				server.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(final String[] args) {
		int port = Integer.parseInt(args[0]);
		if (port < 1024 || port > 65535) {
			System.err.println("porta non valida");
			return;
		}
		new MultiServer(port);
		
	}

}
