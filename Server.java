import java.net.*;//java.net package ko include karka sari class import kar lenga
import java.io.*;

public class Server {
    ServerSocket server;
    Socket socket;
    BufferedReader br;// br will be used for reading
    PrintWriter out;// out will be used for writing

    public Server()// Constructor of the Server class
    {
        try {
            server = new ServerSocket(7777);// hama port batana padega, because when the client contact to the serve
                                            // then the client must know that from which of the applicatuion we are
                                            // gonna talk

            System.out.println("Server is ready to accept connection");
            System.out.println("waiting....");
            socket = server.accept();// accepting the connection of client i.e socket (connection accept client ka hi
                                     // kar raha hoga so vo usi ka object return kar dega)

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));// hamna socket sa input stream
                                                                                    // nikal li hai and given to
                                                                                    // InputStreamReader so jo byte ka
                                                                                    // data aayaga InputStreamReader
                                                                                    // usko character mai convert kar
                                                                                    // dega, now only we have to read
                                                                                    // char data from br
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // BOTH READING AND WRITING DATA KA KAM SATH MAI HO GAYA< SO WE HAVE TO USE THE
    // CONCEPT OF MULTITHREADING
    public void startReading()// here we have to read data with the help of bufferedReader br
    {
        // thread - read karka det rahaga
        // thread creation using lamda expression
        Runnable r1 = () -> {
            System.out.println("Reader started");

            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Client Terminated the chat");

                        socket.close();
                        break;
                    }
                    System.out.println("Client: " + msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(r1).start();// for starting a thread, thread class ka object bana ka reference runnable ka
                               // pass karka start kar denga
    }

    public void startWriting()// here we have start writing data
    {
        // thread - data user sa laga and then send karaga client tak
        // thread creation using lamda expression
        Runnable r2 = () -> {
            System.out.println("Writer Started");
            try {
                while (!socket.isClosed()) {
                    // out.print(content); but here we do not have content, we have to tak content
                    // from the user(console)
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));// System.in for taking
                                                                                              // input from console or
                                                                                              // we can say keyboard

                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("This is server..going to start server");
        new Server();// Constructor is called
    }
}
