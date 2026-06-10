import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class RoomServer {
    private static RoomRepository repository = new RoomRepository();

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(35001);
        System.out.println("RoomServer TCP escuchando en puerto 35001...");
        System.out.println("Comandos disponibles:");
        System.out.println("  GET_ROOM:id");
        System.out.println("  RESERVE:id:usuario");
        System.out.println("  RELEASE:id");
        System.out.println("  LIST_ROOMS");
        
        while (true) {
            Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            
            String request = in.readLine();
            String response = processRequest(request);
            out.println(response);
            
            in.close();
            out.close();
            clientSocket.close();
        }
    }

    private static String processRequest(String request) {
        if (request == null || request.isEmpty()) {
            return "ERROR: Solicitud vacía";
        }

        String[] parts = request.split(":");
        String command = parts[0];

        switch (command) {
            case "GET_ROOM":
                return handleGetRoom(parts);
            case "RESERVE":
                return handleReserve(parts);
            case "RELEASE":
                return handleRelease(parts);
            case "LIST_ROOMS":
                return handleListRooms();
            default:
                return "ERROR: Comando desconocido. Use: GET_ROOM, RESERVE, RELEASE, LIST_ROOMS";
        }
    }

    private static String handleGetRoom(String[] parts) {
        if (parts.length < 2) {
            return "ERROR: Formato inválido. Use GET_ROOM:id";
        }
        String id = parts[1];
        Room room = repository.findById(id);
        if (room == null) {
            return "ERROR: Salón no encontrado";
        }
        return "OK|" + room.toString();
    }

    private static String handleReserve(String[] parts) {
        if (parts.length < 3) {
            return "ERROR: Formato inválido. Use RESERVE:id:usuario";
        }
        String id = parts[1];
        String user = parts[2];
        Room room = repository.findById(id);
        if (room == null) {
            return "ERROR: Salón no encontrado";
        }
        if (room.reserve(user)) {
            return "OK|Salón " + id + " reservado exitosamente por " + user;
        } else {
            return "ERROR|El salón " + id + " ya está reservado por " + room.getReservedBy();
        }
    }

    private static String handleRelease(String[] parts) {
        if (parts.length < 2) {
            return "ERROR: Formato inválido. Use RELEASE:id";
        }
        String id = parts[1];
        Room room = repository.findById(id);
        if (room == null) {
            return "ERROR: Salón no encontrado";
        }
        if (room.release()) {
            return "OK|Salón " + id + " liberado exitosamente";
        } else {
            return "ERROR|El salón " + id + " no estaba reservado";
        }
    }

    private static String handleListRooms() {
        StringBuilder sb = new StringBuilder("OK|LISTA_DE_SALONES:\n");
        for (Room room : repository.getAllRooms().values()) {
            sb.append("  - ").append(room.toString()).append("\n");
        }
        return sb.toString();
    }
}