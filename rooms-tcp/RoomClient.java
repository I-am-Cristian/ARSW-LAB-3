import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class RoomClient {
    private static final String SERVER_HOST = "127.0.0.1";
    private static final int SERVER_PORT = 35001;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== SISTEMA DE GESTIÓN DE SALONES ===");
        System.out.println("Comandos disponibles:");
        System.out.println("1. Consultar salón (GET_ROOM:id)");
        System.out.println("2. Listar todos los salones (LIST)");
        System.out.println("3. Reservar salón (RESERVE:id:usuario)");
        System.out.println("4. Liberar salón (RELEASE:id)");
        System.out.println("5. Salir");
        System.out.println("----------------------------------------");
        
        while (true) {
            System.out.print("\nIngrese comando: ");
            String input = scanner.nextLine();
            
            if (input.equalsIgnoreCase("salir") || input.equalsIgnoreCase("exit")) {
                System.out.println("¡Hasta luego!");
                break;
            }
            
            if (input.equalsIgnoreCase("list") || input.equalsIgnoreCase("LIST")) {
                input = "LIST_ROOMS";
            }
            
            String response = sendRequest(input);
            System.out.println("Respuesta: " + response);
        }
        
        scanner.close();
    }
    
    private static String sendRequest(String request) {
        try {
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            
            out.println(request);
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                if (response.length() > 0) {
                    response.append(System.lineSeparator());
                }
                response.append(line);
            }
            
            in.close();
            out.close();
            socket.close();
            
            return response.toString();
        } catch (Exception e) {
            return "ERROR: No se pudo conectar al servidor - " + e.getMessage();
        }
    }
}