import java.util.HashMap;
import java.util.Map;

public class RoomRepository {
    
    private Map<String, Room> rooms = new HashMap<>();

    public RoomRepository() {
        // Salones predefinidos
        rooms.put("E301", new Room("E301", "Salón de Sistemas", 30));
        rooms.put("E302", new Room("E302", "Salón de Redes", 25));
        rooms.put("E303", new Room("E303", "Salón de Multimedia", 35));
        rooms.put("E304", new Room("E304", "Laboratorio de Hardware", 20));
    }

    public Room findById(String id) {
        return rooms.get(id);
    }

    public Map<String, Room> getAllRooms() {
        return rooms;
    }
}