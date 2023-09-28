import java.net.*;//java.net package ko include karka sari class import kar lenga
import java.io.*;

public class Client {

    Socket socket;
    BufferedReader br;// br will be used for reading
    PrintWriter out;// out will be used for writing

    public Client() {
        try {
            System.out.println("sending request to server");
            socket = new Socket("127.0.0.1", 7777);// dushra computer par chal raha hai to uska ip address kya hai and
                                                   // port number mention karna hai, 127.0.0.1 is the local host means
                                                   // on the same machine
            System.out.println("connection done");

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
                while (!socket.isClosed()) {// kyoki writer chalta rahega so we have to apply this condition
                                                    // kyoki writer input keyboard sa la raha hai
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
        new Client();
        System.out.println("this is client...");
    }

}

// HERE FOUR THREADS ARE RUNNING CONCURRENTLY
// READER WILL BE CLOSED WHEN WE TYPE exit THEN ONLY WRITER WILL WORK PROPERLY
// AND READER WILL NOT