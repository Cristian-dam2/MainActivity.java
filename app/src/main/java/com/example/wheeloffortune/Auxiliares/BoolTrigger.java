package com.example.wheeloffortune.Auxiliares;

/**
 * Esta clase auxiliar tiene un boolean que puede usarse para crear un método con ejecución condicional.
 * Más concretamente, esta clase sirve para que crees un listener que ejecutará una función cuando cambies
 * el boolean guardado en su instancia con setBool(boolean) y ejecutar un bloque de código u otro según
 * el estado del boolean.
 * <pre>
 * Ejemplo de implementación:
 *     {@code
 *         C c = new C(false);
 *         c.setOnBoolChangeListener(new C.BoolChangeListener) {
 *             @Override
 *             void onBoolChange(boolean b) {
 *                 if b == true ? System.out.println("Sí") : System.out.println("No");
 *             }
 *         }
 *     }
 * A partir de este bloque, si se ejecuta setBool(true), se imprimiría por pantalla "Sí". Si se ejecutara
 * setBool(false), se imprimiría "No".
 * </pre>
 * @see BoolChangeListener
 */
public class BoolTrigger {
    /**
     * Contiene el método que el listener ejecutará cuando el boolean de esta instancia cambie.
     */
    public interface BoolChangeListener {
        void onBoolChange(boolean b);
    }

    private boolean myBool;
    public BoolChangeListener listener = null;

    /**
     * Crea una instancia de C
     * @param valorInicial El valor inicial del boolean de esta instancia
     */
    public BoolTrigger(boolean valorInicial) {
        this.myBool = valorInicial;
    }
    public void setOnBoolChangeListener(BoolChangeListener listener) {
        this.listener = listener;
    }

    // Getter
    public boolean getBool() {
        return this.myBool;
    }
    /**
     * Cambia el boolean guardado y ejecuta el método del listener (si este no es null)
     * @param b Boolean con el que ejecutar onBoolChange
     */
    public void setBool(boolean b) {
        this.myBool = b;
        if (this.listener != null) {
            listener.onBoolChange(b);
        }
    }
}