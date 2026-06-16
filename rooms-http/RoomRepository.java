import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RoomRepository {
    private Map<String, Room> rooms = new ConcurrentHashMap<>();

    public RoomRepository() {
        // Datos iniciales de salones
        rooms.put("E301", new Room("E301", "Salon de Conferencias", 50));
        rooms.put("E302", new Room("E302", "Sala de Reuniones", 20));
        rooms.put("E303", new Room("E303", "Auditorio Principal", 100));
        rooms.put("E304", new Room("E304", "Sala de Estudio", 15));
    }

    public Collection<Room> getAllRooms() {
        return rooms.values();
    }

    public Room findById(String id) {
        return rooms.get(id);
    }

    public boolean reserveRoom(String id, String user) {
        Room room = rooms.get(id);
        if (room != null && !room.isReserved()) {
            room.reserve(user);
            return true;
        }
        return false;
    }

    public boolean releaseRoom(String id) {
        Room room = rooms.get(id);
        if (room != null && room.isReserved()) {
            room.release();
            return true;
        }
        return false;
    }
}