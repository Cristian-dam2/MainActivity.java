package com.example.wheeloffortune.Auxiliares;

/**
 * Esta clase auxiliar tiene un boolean que puede usarse para crear un método con ejecución condicional.
 * Más concretamente, esta clase sirve para que crees un listener que ejecutará una función cuando cambies
 * el boolean guardado en su instancia con setBool(boolean) y ejecutar un bloque u otro según el estado
 * del boolean.
 * <pre>
 * Ejemplo:
 *     {@code
 *         C c = new C();
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
 */
public class C {
    private boolean myBool;

    public BoolChangeListener mOnChange = null;
    public interface BoolChangeListener {
        void onBoolChange(boolean b);
    }

    /**
     * Cambia el boolean guardado e intenta ejecutar onBoolChange(boolean)
     * @param b Boolean con el que ejecutar onBoolChange
     */
    public void setBool(boolean b) {
        myBool = b;
        if (mOnChange != null) {
            mOnChange.onBoolChange(b);
        }
    }
    public void setOnBoolChangeListener(BoolChangeListener bcl) {
        mOnChange = bcl;
    }
}