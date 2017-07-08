package de.noobits.secretsantamanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton addButton;
    private ArrayList<Santa> santaArrayList;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private Santa newSanta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = (FloatingActionButton) findViewById(R.id.fab_add);
        listView = (ListView) findViewById(R.id.ListViewSecretSantas);
        santaArrayList = new ArrayList<Santa>();


    }

    @Override
    protected void onResume(){
        super.onResume();
        putSantaToList();
    }


    public void clickAddButton(View v){
        Intent intent = new Intent(MainActivity.this, AddSecretSantaActivity.class);
        MainActivity.this.startActivity(intent);
    }


    /**
     * Adds a new Santa to the list if it exists. Resets the newSanta after that.
     */
    private void putSantaToList(){
        SharedPreferences myPrefs = getSharedPreferences(AddSecretSantaActivity.PREFS_NAME, 0);

        //get the newSanta from sharedpreferences
        Gson gson = new Gson();
        String santaJson = myPrefs.getString("newSanta", "");  //getString(String key, String default)
        newSanta = gson.fromJson(santaJson, Santa.class);          //fromJson(String json, Object.class)


        if(newSanta != null){
            santaArrayList.add(newSanta);

            //adapter = new ArrayAdapter<String>(//TODO: fill with parameters);
            listView.setAdapter(adapter);

            //feedback for user, santa has been added
            Toast.makeText(getApplicationContext(), "Santa " + newSanta.getFirstName() + " has been added", Toast.LENGTH_LONG).show();

            //TODO: initialise adapter and add it to the listView
        }
        newSanta = null;
    }
}
