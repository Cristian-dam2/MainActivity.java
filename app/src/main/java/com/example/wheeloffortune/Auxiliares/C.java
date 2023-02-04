package com.example.wheeloffortune.Auxiliares;

public class C  {
    public BoolChangeListener mOnChange = null;
    public interface BoolChangeListener {
        void onBoolChange(boolean b);
    }

    /**
     * Cambia el boolean guardado e intenta ejecutar onBoolChange(boolean)
     * @param b
     */
    public void setBool(boolean b) {
        if (mOnChange != null) {
            mOnChange.onBoolChange(b);
        }
    }
    public void setOnBoolChangeListener(BoolChangeListener bcl) {
        mOnChange = bcl;
    }
}