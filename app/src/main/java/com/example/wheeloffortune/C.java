package com.example.wheeloffortune;

public class C
{
    private boolean mBoo; //that's our variable

    public interface BooChangeListener
    {
        public void OnBooChange(boolean Boo);

        void onBooChange(boolean b);
    }

    public BooChangeListener mOnChange = null;

    public void setOnBooChangeListener(BooChangeListener bcl)
    {
        mOnChange = bcl;
    }

    public void setBoo(boolean b)
    {
        mBoo = b;
        if(mOnChange != null)
            mOnChange.onBooChange(b);
    }
}