import java.io.Serializable;

public class Equipment implements Serializable {
    private String code;
    private String name;
    private String laboratory;
    private boolean available;
    
    public Equipment(String code, String name, String laboratory, boolean available) {
        this.code = code;
        this.name = name;
        this.laboratory = laboratory;
        this.available = available;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getName() {
        return name;
    }
    
    public String getLaboratory() {
        return laboratory;
    }
    
    public boolean isAvailable() {
        return available;
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    @Override
    public String toString() {
        return "[" + code + "] " + name + " - Lab: " + laboratory + 
               " - Estado: " + (available ? "DISPONIBLE" : "RESERVADO");
    }
}