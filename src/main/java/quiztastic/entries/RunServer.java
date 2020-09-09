package quiztastic.entries;

import quiztastic.ui.Protocol;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

public class RunServer implements Runnable {

    private final Socket socket;
    public static volatile boolean keepRunning = true;

    private static ArrayList<String> users = new ArrayList<>();

    public RunServer(Socket socket) {
        this.socket = socket;
    }

    private String timestamp(){
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    @Override
    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            new Protocol(in, out).run();

            System.out.println(timestamp() + " [DISCONNECTED] " + socket.getInetAddress()
                    + " port " + socket.getPort()
                    + " server port " + socket.getLocalPort());

            socket.close();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void closeInstance(){
        try {
            keepRunning = false;
            socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        final int port = 6969;
        final ServerSocket serverSocket = new ServerSocket(port);
        String timestamp = new SimpleDateFormat("HH:MM:ss").format(new Date());

        Map<Integer, Socket> clients = new HashMap<Integer, Socket>();
        final Scanner scanner = new Scanner(System.in);

        while(keepRunning) {
            Socket socket = serverSocket.accept();
            System.out.println(timestamp + " [CONNECTED] " + socket.getInetAddress()
                    + " port " + socket.getPort()
                    + " server port " + socket.getLocalPort());

            clients.put(socket.getPort(), socket);
            users.add(socket.getInetAddress().toString());

            Thread thread = new Thread(new RunServer(socket));
            thread.start();

            while(true){

                System.out.print("\nSend to users: ");
                String input = scanner.nextLine();

            if(input != null) {

                for (int key : clients.keySet()) {
                    Socket client = clients.get(key);

                    //Checking to make sure it's a client we want to send to.
                    if (users.contains(client.getInetAddress().toString())) {
                        // Sending the response back to the client.
                        // Note: Ideally you want all these in a try/catch/finally block
                        OutputStream os = client.getOutputStream();
                        OutputStreamWriter osw = new OutputStreamWriter(os);
                        BufferedWriter bw = new BufferedWriter(osw);

                        String asciiMsg = "" +
                                "  ____   ______   _____  _  __ ______  _____  \n" +
                                " |  _ \\ |  ____| / ____|| |/ /|  ____||  __ \\ \n" +
                                " | |_) || |__   | (___  | ' / | |__   | |  | |\n" +
                                " |  _ < |  __|   \\___ \\ |  <  |  __|  | |  | |\n" +
                                " | |_) || |____  ____) || . \\ | |____ | |__| |\n" +
                                " |____/ |______||_____/ |_|\\_\\|______||_____/ \n" +
                                "                                              \n" +
                                "                                              ";

                        String inputMsg = "####\t"
                                +input
                                +"\t####";

                        String divider = "";

                        for(int i=0;i<inputMsg.length()+6;i++){
                            divider += "#";
                        }

                        bw.write("\n\n"
                                + divider
                                + "\n## SYSTEM BESKED ###\n"
                                +divider+"\n"
                                + inputMsg
                                +"\n"+divider+"\n> ");
                        bw.flush();
                    }
                }
            }
            }
        }


    }
}
