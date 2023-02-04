package com.example.wheeloffortune.Auxiliares;

public class C  {
    private boolean myBool;
    public void setBool(boolean b) {
        myBool = b;
        if (mOnChange != null) mOnChange.onBoolChange(b);
    }

    public BoolChangeListener mOnChange = null;
    public interface BoolChangeListener {
        void onBoolChange(boolean b);
    }

    public void setOnBoolChangeListener(BoolChangeListener bcl) {
        mOnChange = bcl;
    }
}