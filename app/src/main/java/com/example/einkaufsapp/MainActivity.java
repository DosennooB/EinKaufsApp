package com.example.einkaufsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

// Verwaltet das hinzfügen von neuen Bestellungen
public class MainActivity extends AppCompatActivity {
    einkaeufe bestellungen;
    int füroma,füropa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean OoO, wichtig;
        String ware;
        int anzahl;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_main);

        //Erzeugen eine Daten Bank
        SQLiteDatabase db = null;
        try{
            db = openOrCreateDatabase("EinkaufsApp", MODE_PRIVATE, null);
            db.execSQL("CREATE TABlE IF NOT EXISTS Bestellungen (OoO INTEGER, Ware TEXT, Anzahl INTEGER, Wichtig INTEGER);");
        }catch (Exception e){
            Toast t = Toast.makeText(getApplicationContext(),
                    "lbestel",
                    Toast.LENGTH_SHORT);
        }
        finally {
            db.close();
        }

        //Instanz für alle Einkäufe die in der Aktiviti hinzugefügt wurden
        bestellungen = new einkaeufe();

        SharedPreferences sp = getSharedPreferences("LetzterEinkauf", 0);
        OoO = sp.getBoolean("OoO", false);
        wichtig = sp.getBoolean("wichtig", false);
        ware = sp.getString("Ware","");
        anzahl = sp.getInt("anzahl", -1);

        if(true){
            String lbestel ="";
            if(OoO){
                lbestel += "Oma: ";
            }else{
                lbestel += "Opa: ";
            }
            lbestel += ware+" x "+anzahl;
            if(wichtig){
                lbestel += " Wichtg";
            }
            Toast t = Toast.makeText(getApplicationContext(),
                    lbestel,
                    Toast.LENGTH_SHORT);
            t.show();
        }


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mi = new MenuInflater(this);
        mi.inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.mlist){
            startActivity(new Intent(this,ListActivity.class));

        }
        return true;
    }

    //fügt eine neue Bestellung der Datenbank hinzu und gibt diese in der Listview wieder
    public void clickOk(View v){
        int radio = ((RadioGroup)findViewById(R.id.OoO)).getCheckedRadioButtonId();
        String OoO = ((RadioButton)findViewById(radio)).getText().toString();
        String ware = ((EditText)findViewById(R.id.ware)).getText().toString();
        String anzahl = ((EditText)findViewById(R.id.anzahl)).getText().toString();
        boolean wichtig = ((CheckBox)findViewById(R.id.wichtig)).isChecked();

        try{
            int anzint = Integer.parseInt(anzahl);

            boolean OoObool = false;
            if(OoO.equals("Oma")){
               OoObool = true;
            }
            einkauf bestellung = new einkauf(OoObool,ware,anzint,wichtig);
            bestellungen.add(bestellung);
            SQLiteDatabase db = null;
            db = openOrCreateDatabase("EinkaufsApp", MODE_PRIVATE, null);
            db.execSQL("INSERT INTO Bestellungen VALUES ("+bestellung.toSqlValue()+");");
            db.close();

            if(OoO.equals("Oma")){
                füroma += 1;
                ((TextView)findViewById(R.id.anzOma)).setText("Für Oma: "+füroma);
            }else {
                füropa += 1;
                ((TextView) findViewById((R.id.anzOpa))).setText("Für Opa: " + füropa);
            }
        }catch(NumberFormatException e){
            Toast falschenummer = Toast.makeText(getApplicationContext(),
                    "Bitte geben Sie eine kleiner Zahl an",
                    Toast.LENGTH_SHORT);
            falschenummer.show();
        }

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, bestellungen.toStringList());
        ((ListView)findViewById(R.id.listwaren)).setAdapter(adapter);

    }

    //setzt die Einstellungen in den Ui Elementen auf standart zurück
    public void clickClear(View v){
        ((RadioGroup)findViewById(R.id.OoO)).check(R.id.oma);
        ((EditText)findViewById(R.id.ware)).setText("Ware");
        ((EditText)findViewById(R.id.anzahl)).setText("1");
        ((CheckBox)findViewById(R.id.wichtig)).setChecked(false);

    }
    //Speichert die Werte der Letzen eingegeben Bestellung in den SharedPreferences
    public void onStop() {
        super.onStop();

        einkauf e = bestellungen.getLetzeBestellung();
        if(e!=null){
            SharedPreferences sp = getSharedPreferences("LetzterEinkauf", 0);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("OoO", e.getOoO());
            editor.putString("Ware",e.getWare());
            editor.putInt("anzahl", e.getAnzahl());
            editor.putBoolean("wichtig", e.isWichtig());
            editor.commit();
        }
    }
}

