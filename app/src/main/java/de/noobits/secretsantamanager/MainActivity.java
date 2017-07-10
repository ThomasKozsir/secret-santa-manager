package de.noobits.secretsantamanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton addButton;
    private ArrayList<Santa> santaArrayList;    //set of all santas who need a receiver (all members at the beginning)
    private ArrayList<Santa> receiverArrayList; //set of people who have no santa yet
    private int santaCounter;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private String[] listViewContent;
    private Santa newSanta;
    private SharedPreferences myPrefs;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private Random randomGen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        santaCounter = 0;
        gson = new Gson();
        myPrefs = getSharedPreferences(AddSecretSantaActivity.PREFS_NAME, 0);
        editor = myPrefs.edit();


        addButton = (FloatingActionButton) findViewById(R.id.fab_add);
        listView = (ListView) findViewById(R.id.ListViewSecretSantas);
        santaArrayList = new ArrayList<Santa>();
        loadSavedSantas();
        //updateSantaListView();

    }

    @Override
    protected void onResume(){
        super.onResume();
        putSantaToList();
        loadSavedSantas();
        //updateSantaListView();
    }

    /**
     * Chooses a random Santa out of the set and assign him to another member.
     */
    public void startAssignment(){
        receiverArrayList = new ArrayList<Santa>();
        int santaIndex, receiverIndex;
        Santa chosenSanta;
        Santa chosenReceiver;

        //fill the receiver list with all santas, because every santa is also a receiver
        for(Santa temp : santaArrayList){
            receiverArrayList.add(temp);
        }
        //get the amount of initial santas once, before we take em out one for one, to assign them.
        santaCounter = santaArrayList.size();

        for(int i = 0; i< santaCounter; i++){
            santaIndex = randomGen.nextInt(santaArrayList.size());
            receiverIndex = randomGen.nextInt(receiverArrayList.size());
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_SUBJECT, R.string.emailAssignmentSubject);

            //get random santa and receiver, check if they arent the same
            do {
                chosenSanta = santaArrayList.get(santaIndex);
                chosenReceiver = receiverArrayList.get(receiverIndex);
            }while(chosenReceiver.equals(chosenSanta));

            //remove santa from santaList and receiver from receiverList
            santaArrayList.remove(santaIndex);
            receiverArrayList.remove(receiverIndex);

            //send email with the name of the chosen receiver to santa
            intent.putExtra(Intent.EXTRA_TEXT, chosenReceiver.getFirstName() + ", " + chosenReceiver.getLastName());

            //catch app-crash, when theres no app to open the intent
            if(intent.resolveActivity(getPackageManager()) != null){
                startActivity(intent);
            }
            santaCounter--;
        }

        Toast.makeText(getApplicationContext(), R.string.assignmentDoneToast, Toast.LENGTH_LONG);
    }

    /**
     * Loads the saved list from sharedpreferences.
     */
    private void loadSavedSantas(){
        //TODO: open sharedpreferences and get the list from there to save it in this class's list
        String santaListJson = myPrefs.getString("santaList", "");
        santaArrayList = gson.fromJson(santaListJson, ArrayList.class);
    }

    /**
     * Initialises or updates the listView with the names.
     */
    private void updateSantaListView(){
        //concatenate name and forename of each santa and save it in a array called listViewContent
        int santaCounter = santaArrayList.size();
        listViewContent = new String[santaCounter];

        for(int i = 0; i < santaCounter; i++){
            Log.d("updateSantaListView", "loop nr.:" + i);
            listViewContent[i] = (String)(santaArrayList.get(i+1).getFirstName() + " " + santaArrayList.get(i+1).getLastName());
        }
        //TODO: fill listView with items containing santa attributes forename and name

        //adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, listViewContent );
        //listView.setAdapter(adapter);
    }


    public void clickAddButton(View v){
        Intent intent = new Intent(MainActivity.this, AddSecretSantaActivity.class);
        MainActivity.this.startActivity(intent);
    }


    /**
     * Adds a new Santa to the list if it exists. Resets the newSanta after that.
     */
    private void putSantaToList(){

        //get the newSanta from sharedpreferences
        String santaJson = myPrefs.getString("newSanta", "");  //getString(String key, String default)
        newSanta = gson.fromJson(santaJson, Santa.class);          //fromJson(String json, Object.class)


        if(newSanta != null){
            //add the new santa to the list
            santaArrayList.add(newSanta);

            //convert the Santalist to a JSON string
            String santaListJson = gson.toJson(santaArrayList);

            //save santalist in sharedpreferences
            editor.putString("santaList", santaListJson);
            editor.commit();

            //feedback for user, santa has been added
            Toast.makeText(getApplicationContext(), newSanta.getFirstName() + " has been added", Toast.LENGTH_LONG).show();
        }
        //TODO: Bugfixing - when starting the app, last added Santa is shown in toast
        newSanta = null;
        myPrefs.edit().remove("newSanta");
        editor.commit();
    }
}
