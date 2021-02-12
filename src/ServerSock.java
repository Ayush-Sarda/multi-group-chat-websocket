import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSock {
    public static void main(String a[]) throws Exception {
        ServerSocket ss = new ServerSocket(9999);

        while(true) {
            Socket s = ss.accept();
            System.out.println("Client connected");

            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            OutputStreamWriter os = new OutputStreamWriter(s.getOutputStream());
            PrintWriter out = new PrintWriter(os);

            while (true) {
                String data = br.readLine();
                if (data == null) break;
                System.out.println("Client sent : " + data);
                out.println("Message recieved");
                out.flush();
            }
            System.out.println("Client disconnected");
        }

    }
}
