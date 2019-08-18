import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import keyboardinput.Keyboard;

public class Client {

	private static final String OK = "ok";
	/**
	 * @param args
	 */
	private ObjectInputStream in;
	private ObjectOutputStream out;

	public Client(final String ip, final int port) throws IOException {
		InetAddress addr = InetAddress.getByName(ip); 
		System.out.println("addr = " + addr + " port = " + port);
		Socket socket = new Socket(addr, port); 
		System.out.println(socket);

		in = new ObjectInputStream(socket.getInputStream());
		out = new ObjectOutputStream(socket.getOutputStream());
	}

	private int menu() {
		int answer;

		do {
			System.out.println("(1) Load clusters from file");
			System.out.println("(2) Load data from db");
			System.out.print("(1/2):");
			answer = Keyboard.readInt();
		} while (answer <= 0 || answer > 2);
		return answer;

	}

	private String learningFromFile() throws SocketException, ServerException, IOException, ClassNotFoundException {
		out.writeObject(3);

		System.out.print("Table Name:");
		String tabName = Keyboard.readString();
		out.writeObject(tabName);
		double r = 1.0;
		System.out.print("Radius:");
		r = Keyboard.readDouble();
		while (r <= 0) {
			System.out.println("Insert a radius greater than 0");
			System.out.print("Radius:");
			r = Keyboard.readDouble();
		}
		out.writeObject(r);
		String result = (String) in.readObject();
		if (result.equals(OK)) {
			return (String) in.readObject();
		} else if (result.equals("filenotfound")) {
			throw new ServerException("The selected file does not exists");
		} else {
			throw new ServerException(result);
		}

	}

	private void storeTableFromDb() throws SocketException, ServerException, IOException, ClassNotFoundException {
		out.writeObject(0);
		System.out.print("Table name:");
		String tabName = Keyboard.readString();
		out.writeObject(tabName);
		String result = (String) in.readObject();
		if (result.equals("empty")) {
			throw new ServerException("The selected table is empty");
		} else if (result.equals("notFound")) {
			throw new ServerException("The selected table does not exists");
		} else if (!result.equals(OK)) {
			throw new ServerException(result);
		}

	}

	private String learningFromDbTable()
			throws SocketException, ServerException, IOException, ClassNotFoundException {
		out.writeObject(1);
		double r = 1.0;

		System.out.print("Radius:");
		r = Keyboard.readDouble();
		while (r <= 0) {
			System.out.println("Insert a radius greater than 0");
			System.out.print("Radius:");
			r = Keyboard.readDouble();
		}
		out.writeObject(r);
		String result = (String) in.readObject();
		if (result.equals(OK)) {
			System.out.println("Number of Clusters:" + in.readObject());
			return (String) in.readObject();
		} else if (result.equals("empty")) {
			throw new ServerException("The selected table is empty");
		} else if (result.equals("full")) {
			throw new ServerException("The radious is too big");
		} else {
			throw new ServerException(result);
		}

	}

	private void storeClusterInFile() throws SocketException, ServerException, IOException, ClassNotFoundException {
		out.writeObject(2);

		String result = (String) in.readObject();
		if (!result.equals(OK)) {
			throw new ServerException(result);
		}

	}

	private void stop() {
		try {
			out.writeObject(5);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(final String[] args) {
		String ip = args[0];
		int port = Integer.parseInt(args[1]);
		if (port < 1024 || port > 65535) {
			System.err.println("porta non valida");
			return;
		}
		Client main = null;
		String on="";
		
		do {
			try {
				main = new Client(ip, port);
			} catch (IOException e) {
				System.out.println("connessione fallita");
			}
	
			if (main!=null) {
				do {
					int menuAnswer = main.menu();
					switch (menuAnswer) {
						case 1:
							try {
								String kmeans = main.learningFromFile();
								System.out.println(kmeans);
							} catch (SocketException e) {
								System.out.println(e);
								return;
							} catch (FileNotFoundException e) {
								System.out.println(e);
								return;
							} catch (IOException e) {
								System.out.println(e);
								return;
							} catch (ClassNotFoundException e) {
								System.out.println(e);
								return;
							} catch (ServerException e) {
								System.out.println(e.getMessage());
							}
							break;
						case 2:
		
							while (true) {
								try {
									main.storeTableFromDb();
									break;
								}
		
								catch (SocketException e) {
									System.out.println(e);
									return;
								} catch (FileNotFoundException e) {
									System.out.println(e);
									return;
		
								} catch (IOException e) {
									System.out.println(e);
									return;
								} catch (ClassNotFoundException e) {
									System.out.println(e);
									return;
								} catch (ServerException e) {
									System.out.println(e.getMessage());
								}
							} 
		
							char answer = 'y';
							do {
								try {
									String clusterSet = main.learningFromDbTable();
									System.out.println(clusterSet);
		
									main.storeClusterInFile();
		
								} catch (SocketException e) {
									System.out.println(e);
									return;
								} catch (FileNotFoundException e) {
									System.out.println(e);
									return;
								} catch (ClassNotFoundException e) {
									System.out.println(e);
									return;
								} catch (IOException e) {
									System.out.println(e);
									return;
								} catch (ServerException e) {
									System.out.println(e.getMessage());
								}
								System.out.print("Would you repeat?(y/n)");
								answer = Keyboard.readChar();
							} while (Character.toLowerCase(answer) == 'y');
							break;
						default:
							System.out.println("Invalid option!");
					}
		
					System.out.print("would you choose a new operation from menu?(y/n)");
					if (Keyboard.readChar() != 'y') {
						break;
					}
				} while (true);
				main.stop();
			}
			
			if(main==null) {
				System.out.println("Vuoi riprovare?(y/n)");
				on=Keyboard.readString();
			}
		}while(main==null && on.equals("y"));
	}
}
