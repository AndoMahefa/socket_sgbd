package variable;

import java.io.Serializable;

public class Nombre implements Serializable {
    private float value;
    public Nombre(String value) {
        this.value = Float.valueOf(value);
    }
    public float getValue() {
        return value;
    }
    public void setValue(float value) {
        this.value = value;
    }
    public String toString() {
        return value + "";
    }
    public boolean nombre_egaux(Nombre n) {
        return(value == n.value);
    }
}
