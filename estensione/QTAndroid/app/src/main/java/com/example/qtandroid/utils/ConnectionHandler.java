package com.example.qtandroid.utils;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

public class ConnectionHandler {

    private SocketContainer socketContainer;
    private static ConnectionHandler conn = new ConnectionHandler();
    private final Object lock = new Object();
    private String ip;
    private int port;
    private String currentTable = "";
    private boolean connected;

    public static final String OK = "ok";
    public static final String EMPTY = "empty";
    public static final String FULL = "full";
    public static final String FNF = "filenotfound";


    private ConnectionHandler() {
    }

    public void setAddress(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static ConnectionHandler getInstance() {
        return conn;
    }

    public void connect() {
        try {
            socketContainer = new CreateSocket(ip, port).execute().get();
            if (socketContainer != null) {
                connected = true;

            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            connected = false;
        }

    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public void disconnect() {
        new Disconnect().execute();
    }

    public boolean isConnected() {
        return connected;
    }

    public LinkedList<String> getTables() throws InterruptedException, ExecutionException {
        return new GetTables().execute().get();
    }

    public String learnFile(String tableName, double radius) throws InterruptedException, ExecutionException {
        return new LearnFile(tableName, radius).execute().get();
    }

    public String learnDB(String tableName, double radius) throws InterruptedException, ExecutionException {
        return new LearnDB(tableName, radius).execute().get();
    }


    private class SocketContainer {

        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;


        public SocketContainer(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
            this.socket = socket;
            this.in = in;
            this.out = out;
        }

        public ObjectInputStream getIn() {
            return in;
        }

        public Socket getSocket() {
            return socket;
        }

        public ObjectOutputStream getOut() {
            return out;
        }

    }

    private class CreateSocket extends AsyncTask<Void, Void, SocketContainer> {

        private String address;
        private int port;

        private CreateSocket(String address, int port) {
            this.address = address;
            this.port = port;
        }


        @Override
        protected SocketContainer doInBackground(Void... voids) {
            try {
                InetAddress add = InetAddress.getByName(address);
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(add, port), 5000);
                return new SocketContainer(socket, new ObjectInputStream(socket.getInputStream()), new ObjectOutputStream(socket.getOutputStream()));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(SocketContainer sc) {
            super.onPostExecute(sc);
            synchronized (lock) {
                socketContainer = sc;
            }

        }
    }

    private class Disconnect extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            synchronized (lock) {
                try {
                    if (connected) {
                        socketContainer.getOut().writeObject(5);
                        socketContainer.getSocket().close();
                        connected = false;
                        currentTable = "";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    private class GetTables extends AsyncTask<Void, Void, LinkedList<String>> {


        @Override
        protected LinkedList<String> doInBackground(Void... voids) {
            try {
                synchronized (lock) {
                    socketContainer.getOut().writeObject(4);
                    return (LinkedList<String>) socketContainer.getIn().readObject();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class LearnFile extends AsyncTask<Void, Void, String> {
        private String tableName;
        private double radius;

        private LearnFile(String tableName, double radius) {
            this.tableName = tableName;
            this.radius = radius;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = "";
            try {
                synchronized (lock) {
                    socketContainer.getOut().writeObject(3);
                    socketContainer.getOut().writeObject(tableName);
                    socketContainer.getOut().writeObject(radius);
                    result = (String) socketContainer.getIn().readObject();
                    if (result.equals(OK)) {
                        return (String) socketContainer.getIn().readObject();
                    } else throw new ServerException(result);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServerException e) {
                return result;
            }
            return null;
        }
    }

    private class LearnDB extends AsyncTask<Void, Void, String> {
        private String tableName;
        private double radius;

        private LearnDB(String tableName, double radius) {
            this.tableName = tableName;
            this.radius = radius;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = "";
            try {
                synchronized (lock) {
                    if (!currentTable.equals(tableName)) {
                        storeTableFromDb(tableName);
                        currentTable = tableName;
                    }

                    result = learningFromDbTable(radius);
                    storeClusterInFile();
                    return result;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServerException e) {
                if (e.getMessage().equals(EMPTY)) return EMPTY;
                return FULL;
            }
            return null;
        }

        private void storeTableFromDb(String tableName) throws ServerException, IOException, ClassNotFoundException {
            socketContainer.getOut().writeObject(0);
            socketContainer.getOut().writeObject(tableName);
            String result = (String) socketContainer.getIn().readObject();
            if (!result.equals(OK))
                throw new ServerException(result);
        }

        private String learningFromDbTable(Double radius) throws ServerException, IOException, ClassNotFoundException {
            socketContainer.getOut().writeObject(1);

            socketContainer.getOut().writeObject(radius);
            String result = (String) socketContainer.getIn().readObject();
            if (result.equals(OK)) {
                result = "Number of Clusters:" + socketContainer.getIn().readObject() + "\n";
                result += (String) socketContainer.getIn().readObject();
                return result;
            } else throw new ServerException(result);

        }

        private void storeClusterInFile() throws ServerException, IOException, ClassNotFoundException {
            socketContainer.getOut().writeObject(2);
            String result = (String) socketContainer.getIn().readObject();
            if (!result.equals(OK))
                throw new ServerException(result);
        }


    }
}
