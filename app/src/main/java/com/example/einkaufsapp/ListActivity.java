package com.example.einkaufsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
//Verwaltet die besytellungen aus der Db
public class ListActivity extends AppCompatActivity {
    einkaeufe bestellungen;
    final String[] columns ={"OoO", "Ware", "Anzahl", "Wichtig"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        bestellungen = new einkaeufe();

    }

    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater mi = new MenuInflater(this);
        mi.inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.maktivity){
            startActivity(new Intent(this,MainActivity.class));
        }
        return true;
    }
    //Zeigt alle Bestellungen in der Datenbank an
    public void clickAllShow(View v){
        bestellungen = new einkaeufe();
        SQLiteDatabase db = null;
        try{
            db = openOrCreateDatabase("EinkaufsApp", MODE_PRIVATE, null);
            Cursor cs;
            cs = db.rawQuery("Select * from Bestellungen", null);
            bestellungen.importFromDb(cs);
        }catch (Exception e){
            Toast falschenummer = Toast.makeText(getApplicationContext(),
                    "Fehler beim Auslesen der Datenbank",
                    Toast.LENGTH_SHORT);
            falschenummer.show();
        }
        finally {
            db.close();
        }
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, bestellungen.toStringList());
        ((ListView)findViewById(R.id.lanzeige)).setAdapter(adapter);
    }
    //Zeigt Bestellungen in der datenbank an die für Oma sind
    public void clickFuerOma(View v){
        bestellungen = new einkaeufe();
        SQLiteDatabase db = null;
        try{
            db = openOrCreateDatabase("EinkaufsApp", MODE_PRIVATE, null);
            Cursor cs;
            cs = db.query("Bestellungen", columns,"OoO = 1",null, null, null,null);
            bestellungen.importFromDb(cs);
        }catch (Exception e){
            Toast falschenummer = Toast.makeText(getApplicationContext(),
                    "Fehler beim suchen in der Datenbank",
                    Toast.LENGTH_SHORT);
            falschenummer.show();
        }
        finally {
            db.close();
        }
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, bestellungen.toStringList());
        ((ListView)findViewById(R.id.lanzeige)).setAdapter(adapter);
    }
    //Exportiert die die Elemente der Instans Bestellungen in eine Txt Datei
    public void clickExport(View v){
        FileOutputStream fos = null;
        PrintWriter pw = null;
        try{
            fos = this.openFileOutput("test.txt", 0);
            pw = new PrintWriter(fos);
        }catch(Exception e){
            Toast falschenummer = Toast.makeText(getApplicationContext(),
                    "Fehler: Es konnte keine Datei erstellt werden",
                    Toast.LENGTH_SHORT);
            falschenummer.show();
        }
        try{
            ArrayList<String> e = bestellungen.toStringList();
            for(String i: e){
                pw.println(i);
            }
            Toast falschenummer = Toast.makeText(getApplicationContext(),
                    "Es wurden "+e.size()+" Datensetze in die Datei test.txt geschrieben",
                    Toast.LENGTH_SHORT);
            falschenummer.show();
        }catch(Exception e){
            Toast falschenummer = Toast.makeText(getApplicationContext(),
                    "Fehler: Es konnten keine Daten in die Datei test.txt geschrieben werden",
                    Toast.LENGTH_SHORT);
            falschenummer.show();
        }

        try{
            pw.close();
            fos.close();
        }catch (Exception e){
            Toast falschenummer = Toast.makeText(getApplicationContext(),
                    "Fehler: Die Datein konnte nicht erfolgrich geschlossen werden",
                    Toast.LENGTH_SHORT);
            falschenummer.show();
        }
    }
    //Zeigt die Txt datei wieder in der Listview an
    public void clickShowTxt(View v){
        FileInputStream fis = null;
        BufferedReader br = null;
        ArrayList<String> txtlist = new ArrayList<>();
        txtlist.add("Ausgabe der TXT Datei kann nicht gespeichert werden");
        try{
            fis = this.openFileInput("test.txt");
            br = new BufferedReader(new InputStreamReader(fis));
        }catch(Exception e){
            Toast falschenummer = Toast.makeText(getApplicationContext(),
                    "Fehler Die datei existiert nicht oder kann nicht geöffnet werden",
                    Toast.LENGTH_SHORT);
            falschenummer.show();
        }

        try{
            String line;
            while ((line = br.readLine()) != null){
                txtlist.add(line);
            }
        }catch(Exception e){
            Toast falschenummer = Toast.makeText(getApplicationContext(),
                    "Fehler Datei kann nicht gelesen werden",
                    Toast.LENGTH_SHORT);
            falschenummer.show();
        }

        try{
            br.close();
            fis.close();
        }catch (Exception e){
            Toast falschenummer = Toast.makeText(getApplicationContext(),
                    "Fehler Datei kann nicht geschlossen werden",
                    Toast.LENGTH_SHORT);
            falschenummer.show();
        }

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, txtlist);
        ((ListView)findViewById(R.id.lanzeige)).setAdapter(adapter);
    }

}