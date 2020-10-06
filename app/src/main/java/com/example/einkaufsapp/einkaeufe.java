package com.example.einkaufsapp;

import android.database.Cursor;

import java.util.ArrayList;

//Speichert alle bestellungen in einer Arryliste
//
public class einkaeufe {
    ArrayList<einkauf> einkäufe;
    public einkaeufe(){
        einkäufe = new ArrayList<>();
    }

    public void add(einkauf x){
        einkäufe.add(x);
    }


    // Gibt alle bestellungen als Menschen lesbaren String in einem Arry aus für listenview befüllung
    public ArrayList<String> toStringList(){
        ArrayList<String> x = new ArrayList<>();
        for(einkauf i: einkäufe){
            x.add(i.toString());
        }
        return x;
    }

    // gibt die Letzte eingegeben Bestellung zurück
    public einkauf getLetzeBestellung(){
        if(einkäufe.isEmpty()){
            return null;
        }else{
            int i = einkäufe.size();
            return einkäufe.get(i-1);
        }
    }
    // kann direkt die rückgabe werte eines Cursors aus einer Datenbank verarbeiten
    public boolean importFromDb(Cursor cs){
        //Table Struktur OoO INTEGER, Ware TEXT, Anzahl INTEGER, Wichtig INTEGER
        if (cs != null){
            int OoOindex = cs.getColumnIndexOrThrow("OoO");
            int wareindex = cs.getColumnIndexOrThrow("Ware");
            int anzahlindex = cs.getColumnIndexOrThrow("Anzahl");
            int wichtigindex = cs.getColumnIndexOrThrow("Wichtig");
            if(cs.moveToFirst()){
                do{
                    boolean OoO = (cs.getInt(OoOindex)) == 1;
                    String ware = cs.getString(wareindex);
                    int anzahl = cs.getInt(anzahlindex);
                    boolean wichtig = (cs.getInt(wichtigindex)) == 1;
                    einkauf bestellung = new einkauf(OoO,ware,anzahl,wichtig);
                    einkäufe.add(bestellung);
                } while (cs.moveToNext());
                return true;
            }

        }
        return false;
    }
}
