public class Room {
    private String id;
    private String name;
    private int capacity;
    private boolean reserved;
    private String reservedBy;

    public Room(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.reserved = false;
        this.reservedBy = null;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getCapacity() { return capacity; }
    public boolean isReserved() { return reserved; }
    public String getReservedBy() { return reservedBy; }

    public void reserve(String user) {
        if (!reserved) {
            this.reserved = true;
            this.reservedBy = user;
        }
    }

    public void release() {
        this.reserved = false;
        this.reservedBy = null;
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Nombre: %s | Capacidad: %d | Estado: %s%s",
            id, name, capacity, 
            reserved ? "RESERVADO por " + reservedBy : "DISPONIBLE",
            reserved ? "" : " ✓");
    }

    public String toHtmlRow() {
        String statusClass = reserved ? "reserved" : "available";
        String statusText = reserved ? "Reservado por " + reservedBy : "Disponible";
        String actionButton = reserved ?
            String.format("<button onclick=\"location.href='/rooms/release?id=%s'\">Liberar</button>", id) :
            String.format("<button onclick=\"location.href='/rooms/reserve?id=%s'\">Reservar</button>", id);
        
        return String.format(
            "<tr class='%s'>" +
            "<td>%s</td><td>%s</td><td>%d</td>" +
            "<td>%s</td><td>%s</td>" +
            "</tr>",
            statusClass, id, name, capacity, statusText, actionButton
        );
    }
}