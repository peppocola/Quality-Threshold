package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import data.Data;
import data.EmptyDatasetException;
import database.DatabaseConnectionException;
import database.DbAccess;
import database.EmptySetException;
import database.NoValueException;
import mining.ClusteringRadiusException;
import mining.QTMiner;

/**
 * Gestisce la comunicazione con il client.
 */
public class ServerOneClient extends Thread {
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private QTMiner qt;

	private static final String OK = "ok";

	/**
	 * Inizializza la socket e gli stream.
	 *
	 * @param s socket
	 * @throws IOException problemi nella creazione degli stream.
	 */
	public ServerOneClient(final Socket s) throws IOException {
		socket = s;
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		start();
	}

	@Override
	public void run() {
		Data data = null;
		String tabName = "";
		Double radius = null;
		boolean cicle = true;
		String op0 = "lettura tabella da database...";
		String op1 = "esecuzione algoritmo QT con raggio specificato da utente...";
		String op2 = "salvataggio su file...";
		String op3 = "lettura da file...";
		String op4 = "restituzione nomi tabelle del database...";
		String op5 = "chiusura socket...";

		try {
			while (cicle) {
				System.out.println("in attesa dell'operazione da eseguire...");
				final int operation = (int) in.readObject();
				switch (operation) {
					case 0:
						System.out.println("scelta operazione: " + op0);
						tabName = (String) in.readObject();
						try {
							data = new Data(tabName);
							out.writeObject(OK);
						} catch (final EmptySetException e) {
							out.writeObject("empty");
						} catch (SQLException e) {
							out.writeObject("notFound");
						} catch (DatabaseConnectionException | NoValueException e) {
							out.writeObject(e.getMessage());
						}

						break;
					case 1:
						System.out.println("scelta operazione: " + op1);
						radius = (Double) in.readObject();
						qt = new QTMiner(radius);
						try {
							final int num = qt.compute(data);
							out.writeObject(OK);
							out.writeObject(num);
							out.writeObject(qt.toString(data));
						} catch (final EmptyDatasetException e) {
							out.writeObject("empty");
						} catch (final ClusteringRadiusException e) {
							out.writeObject("full");
						}

						break;
					case 2:
						System.out.println("scelta operazione: " + op2);
						qt.salva(tabName + "_" + radius + ".dmp");
						out.writeObject(OK);
						break;
					case 3:
						System.out.println("scelta operazione: " + op3);
						final String file = (String) in.readObject() + "_"
								+ (double) in.readObject() + ".dmp";
						try {
							qt = new QTMiner(file);
							out.writeObject(OK);
							out.writeObject(qt.toString());
						} catch (FileNotFoundException e) {
							out.writeObject("filenotfound");
						}
						break;
					case 4:
						System.out.println("scelta operazione: " + op4);
						final DbAccess db = new DbAccess();
						final LinkedList<String> tables = new LinkedList<String>();
						try {
							db.initConnection();
							final Connection c = db.getConnection();
							final Statement s = c.createStatement();
							final ResultSet r = s.executeQuery("show tables");
							while (r.next()) {
								tables.add(r.getString(1));
							}
							out.writeObject(tables);
							s.close();
						} catch (final DatabaseConnectionException | SQLException e) {
							e.printStackTrace();
						} finally {
							db.closeConnection();
						}
						break;
					case 5:
						System.out.println("scelta operazione: " + op5);
						cicle = false;
						break;
					default:
						break;
				}
			}
		} catch (final IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
				System.out.println("Socket " + socket.getInetAddress() + " chiusa con successo!");
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

	}

}
