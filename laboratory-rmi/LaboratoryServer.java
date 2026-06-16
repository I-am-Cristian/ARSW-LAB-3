import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LaboratoryServer {
    public static void main(String[] args) throws Exception {
        LaboratoryService service = new LaboratoryServiceImpl();
        Registry registry = LocateRegistry.createRegistry(23090);
        registry.rebind("LaboratoryService", service);
        
        System.out.println("=== LaboratoryService RMI ===");
        System.out.println("Servidor publicado en puerto: 23090");
        System.out.println("Servicio registrado como: LaboratoryService");
        System.out.println("Esperando solicitudes de clientes...");
        System.out.println();
    }
}