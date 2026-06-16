
public class Room {
    
    public String id;
    public String name;
    public int capacity;
    public boolean reserved;
    public String reservedBy;

    public Room(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.reserved = false;
        this.reservedBy = null;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isReserved() {
        return reserved;
    }

    public String getReservedBy() {
        return reservedBy;
    }

    public boolean reserve(String user) {
        if (!reserved) {
            this.reserved = true;
            this.reservedBy = user;
            return true;
        }
        return false;
    }

    public boolean release() {
        if (reserved) {
            this.reserved = false;
            this.reservedBy = null;
            return true;
        }
        return false;
    }

    public String toString() {
        return id + "|" + name + "|" + capacity + "|" + reserved + "|" + (reservedBy != null ? reservedBy : "ninguno");
    }
}