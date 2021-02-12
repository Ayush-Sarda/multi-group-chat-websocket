import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import java.util.stream.Stream;

public class ClientSock1 {
    public static void main(String a[]) throws Exception {
        String ip = "localhost";
        Integer port = 9999;
        Scanner scanner = new Scanner(System.in);
        Socket s = new Socket(ip, port);
        OutputStreamWriter os = new OutputStreamWriter(s.getOutputStream());
        PrintWriter out = new PrintWriter(os);
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

        System.out.println("Type your id : ");
        String userId = scanner.nextLine();
        System.out.println("Type your group name : ");
        String groupName = scanner.nextLine();
        out.println(userId);
        out.flush();
        Thread.sleep(100);
        out.println(groupName);
        out.flush();

        Thread thread = new Thread(()->{
            while(true) {
                try {
                    String msg = br.readLine();
                    System.out.println(msg);
                } catch (Exception e) {
                    System.out.println("Error encountered on client side");
                }
            }
        });
        thread.setDaemon(true);
        thread.start();

        while(true) {
//            System.out.print("Client : ");
            String data = scanner.nextLine();
            out.println(data);
            out.flush();
        }

    }
}
