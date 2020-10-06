package com.example.einkaufsapp;

import androidx.annotation.NonNull;
// Speichtert alle nötigen Objekte für eine Bestellung in einer Klasse.
// Kann diese als String für Text und Sql insert ausgeben
public class einkauf {
    private String  ware;
    private int anzahl;
    private boolean OoO,wichtig; //OoO für Oma oder Opa, Oma = true
    public einkauf(boolean OoO, String ware, int anzahl, boolean wichtig) {
        this.OoO= OoO;
        this.ware = ware;
        this.anzahl = anzahl;
        this.wichtig = wichtig;
    }
    @NonNull
    @Override
    public String toString() {
        String text ="";
        if(OoO){
            text = text + "Oma";
        }else{
            text = text + "Opa";
        }
        text = text+": "+anzahl+"x "+ware;
        if(wichtig){
            text = text+" "+"wichtig";
        }
        return text;
    }
    public String toSqlValue(){
        String text = "";
        text += (OoO?1:0)+", ";
        text +="'"+ ware +"', ";
        text += anzahl +", ";
        text += (wichtig?1:0);
        return text;

    }

    public boolean getOoO() {
        return OoO;
    }

    public String getWare() {
        return ware;
    }

    public int getAnzahl() {
        return anzahl;
    }

    public boolean isWichtig() {
        return wichtig;
    }
}
