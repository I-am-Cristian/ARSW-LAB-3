import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class LaboratoryClient {
    private static LaboratoryService service;
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 23090);
        service = (LaboratoryService) registry.lookup("LaboratoryService");
        
        System.out.println("=== SISTEMA DE INVENTARIO DE LABORATORIOS ===");
        System.out.println("Conectado al servidor RMI");
        System.out.println();
        
        boolean running = true;
        while (running) {
            showMenu();
            int option = Integer.parseInt(scanner.nextLine());
            
            switch (option) {
                case 1:
                    consultarTodosEquipos();
                    break;
                case 2:
                    consultarEquipoPorCodigo();
                    break;
                case 3:
                    reservarEquipo();
                    break;
                case 4:
                    liberarEquipo();
                    break;
                case 5:
                    System.out.println("Cerrando conexion...");
                    running = false;
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
            System.out.println();
        }
        scanner.close();
    }
    
    private static void showMenu() {
        System.out.println("--- MENÚ PRINCIPAL ---");
        System.out.println("1. Consultar todos los equipos");
        System.out.println("2. Consultar equipo por código");
        System.out.println("3. Reservar equipo");
        System.out.println("4. Liberar equipo");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opcion: ");
    }
    
    private static void consultarTodosEquipos() throws Exception {
        System.out.println("\n--- LISTA DE EQUIPOS ---");
        List<String> equipos = service.consultarEquipos();
        if (equipos.isEmpty()) {
            System.out.println("No hay equipos registrados");
        } else {
            for (String equipo : equipos) {
                System.out.println(equipo);
            }
        }
    }
    
    private static void consultarEquipoPorCodigo() throws Exception {
        System.out.print("\nIngrese el codigo del equipo: ");
        String codigo = scanner.nextLine();
        String resultado = service.consultarEquipo(codigo);
        System.out.println(resultado);
    }
    
    private static void reservarEquipo() throws Exception {
        System.out.print("\nIngrese el codigo del equipo a reservar: ");
        String codigo = scanner.nextLine();
        
        // Primero consultamos el estado actual
        String info = service.consultarEquipo(codigo);
        if (info.startsWith("ERROR")) {
            System.out.println(info);
            return;
        }
        
        boolean success = service.reservarEquipo(codigo);
        if (success) {
            System.out.println("Equipo reservado exitosamente");
        } else {
            System.out.println("No se pudo reservar el equipo (puede que ya este reservado)");
        }
    }
    
    private static void liberarEquipo() throws Exception {
        System.out.print("\nIngrese el codigo del equipo a liberar: ");
        String codigo = scanner.nextLine();
        
        boolean success = service.liberarEquipo(codigo);
        if (success) {
            System.out.println("Equipo liberado exitosamente");
        } else {
            System.out.println("No se pudo liberar el equipo (puede que ya este disponible o no exista)");
        }
    }
}