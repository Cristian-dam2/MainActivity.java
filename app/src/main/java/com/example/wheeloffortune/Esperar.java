package com.example.wheeloffortune;

public class Esperar {



    public void segundos(int tiempo){
        try {
            Thread.sleep(tiempo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
