import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LaboratoryServiceImpl extends UnicastRemoteObject implements LaboratoryService {
    private Map<String, Equipment> equipmentMap = new HashMap<>();
    
    public LaboratoryServiceImpl() throws RemoteException {
        super();
        // Datos iniciales
        equipmentMap.put("LAB-001", new Equipment("LAB-001", "Microscopio Olympus CX23", "Biología", true));
        equipmentMap.put("LAB-002", new Equipment("LAB-002", "Centrífuga Refrigerada", "Biología", true));
        equipmentMap.put("LAB-003", new Equipment("LAB-003", "Espectrofotómetro", "Química", true));
        equipmentMap.put("LAB-004", new Equipment("LAB-004", "Computadora Dell Optiplex", "Computación", true));
        equipmentMap.put("LAB-005", new Equipment("LAB-005", "Osciloscopio Tektronix", "Electrónica", true));
        equipmentMap.put("LAB-006", new Equipment("LAB-006", "Multímetro Fluke", "Electrónica", false)); // Ya reservado
        equipmentMap.put("LAB-007", new Equipment("LAB-007", "Balanza Analítica", "Química", true));
        equipmentMap.put("LAB-008", new Equipment("LAB-008", "Robot Educativo LEGO", "Robótica", true));
    }
    
    @Override
    public List<String> consultarEquipos() throws RemoteException {
        List<String> equipmentList = new ArrayList<>();
        for (Equipment eq : equipmentMap.values()) {
            equipmentList.add(eq.toString());
        }
        return equipmentList;
    }
    
    @Override
    public String consultarEquipo(String codigo) throws RemoteException {
        Equipment eq = equipmentMap.get(codigo);
        if (eq == null) {
            return "ERROR: Equipo con codigo " + codigo + " no encontrado";
        }
        return eq.toString();
    }
    
    @Override
    public boolean reservarEquipo(String codigo) throws RemoteException {
        Equipment eq = equipmentMap.get(codigo);
        if (eq == null) {
            return false;
        }
        if (eq.isAvailable()) {
            eq.setAvailable(false);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean liberarEquipo(String codigo) throws RemoteException {
        Equipment eq = equipmentMap.get(codigo);
        if (eq == null) {
            return false;
        }
        if (!eq.isAvailable()) {
            eq.setAvailable(true);
            return true;
        }
        return false;
    }
}