package de.noobits.secretsantamanager;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton addButton;
    private ArrayList<Santa> santaArrayList;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private String[] santaData;
    private Santa newSanta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = (FloatingActionButton) findViewById(R.id.fab_add);
        listView = (ListView) findViewById(R.id.ListViewSecretSantas);

        santaArrayList = new ArrayList<Santa>();

        tryGetNewSantaData(savedInstanceState);
    }



    public void clickAddButton(View v){
        Intent intent = new Intent(MainActivity.this, AddSecretSantaActivity.class);
        MainActivity.this.startActivity(intent);
    }

    /**
     * Try to get new santa data if returned from AddSecretSantaActivity.
     * @param savedInstanceState
     */
    private void tryGetNewSantaData(Bundle savedInstanceState){
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                    santaData = (String[])extras.get("santaData");
            }
        } else {
            santaData= (String[]) savedInstanceState.getSerializable("santaData");
        }

        putSantaToList();
    }

    /**
     * Adds a new Santa to the list. Resets the newSanta after that.
     */
    private void putSantaToList(){
        if(newSanta != null){
            Santa newSanta = new Santa(((EditText)findViewById(R.id.edit_text_forename)).getText().toString(),
                    ((EditText)findViewById(R.id.edit_text_name)).getText().toString(),
                    ((EditText)findViewById(R.id.edit_text_email)).getText().toString());
            santaArrayList.add(newSanta);

            //adapter = new ArrayAdapter<String>(//TODO: fill with parameters);
            listView.setAdapter(adapter);

            //TODO: initialise adapter and add it to the listView

        }


    }
}
