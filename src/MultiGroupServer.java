import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class MultiGroupServer {
    List<String> groupNames;
    List<String> connections;
    List<String> userIds;
    Map<String, ArrayList<Socket>> groupAddresses;
    Map<String, ArrayList<String>> groupChats;
    public MultiGroupServer() {
        groupNames = new ArrayList<>();
        connections = new ArrayList<>();
        userIds = new ArrayList<>();
        groupAddresses = new HashMap<>();
        groupChats = new HashMap<>();
    }

    public void acceptConnections(ServerSocket ss) throws Exception {
        while(true) {
            Socket s = ss.accept();
            OutputStreamWriter os = new OutputStreamWriter(s.getOutputStream());
            PrintWriter out = new PrintWriter(os);
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

            System.out.println("New client connected : " + s.getRemoteSocketAddress());

            String userId = br.readLine();
            System.out.println("user id : " + userId);
            String groupName = br.readLine();
            System.out.println("group name : " + groupName);
            String port = String.valueOf(s.getPort());

            connections.add(String.valueOf(s.getPort()));
            userIds.add(userId);

            if(!groupAddresses.containsKey(groupName)) {
                out.println("New group created");
                groupAddresses.put(groupName, new ArrayList<>());
                groupChats.put(groupName, new ArrayList<>());
            } else {
                out.println("Found the group");
            }
            out.flush();
            groupAddresses.get(groupName).add(s);

            if(groupChats.get(groupName).size() > 0) {
                out.println("Earlier messages in this chat");
                for(String chat:groupChats.get(groupName)) {
                    out.println(chat);
//                    out.flush();
                }
            }
            out.flush();

            Thread thread = new Thread(() -> {
                while(true) {
                    try {
                        String message = br.readLine();
                        if(message == null) {
                            System.out.println("Client " + s.getRemoteSocketAddress() + " disconnected");
                            break;
                        }
                        System.out.println("Client : " + userId + " > " + message);
                        groupChats.get(groupName).add("Client : " + userId + " > " + message);
                        for(Socket socket : groupAddresses.get(groupName)) {
                            OutputStreamWriter groupOs = new OutputStreamWriter(socket.getOutputStream());
                            PrintWriter groupOut = new PrintWriter(groupOs);
                            groupOut.println("Client : " + userId + " > " + message);
                            groupOut.flush();
                        }
                    } catch(Exception e) {
                        System.out.println("Exception encountered");
                        break;
                    }
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }


    public static void main(String a[]) throws Exception {
        MultiGroupServer obj = new MultiGroupServer();
        obj.acceptConnections(new ServerSocket(9999));
    }
}
