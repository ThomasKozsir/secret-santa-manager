package de.noobits.secretsantamanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.ContactsContract;
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
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private final String SMTPSERVER = "smtp.1blu.de";
    private final String NOREPLY = "noreply@noobits.de";
    private final int SMTPPORT = 587;
    private final String password = "Q64j_4=lZ.e%;)@";
    private FloatingActionButton addButton, shuffleButton;
    private ArrayList<Santa> santaArrayList ;    //set of all santas who need a receiver (all members at the beginning)
    private ArrayList<Santa> receiverArrayList; //set of people who have no santa yet
    private int santaCounter;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private String[] listViewContent;
    private String mailText;
    private SharedPreferences myPrefs;
    private Gson gson;
    private Random randomGen;
    private int santaIndex;
    private int receiverIndex;
    private Santa chosenSanta;
    private Santa chosenReceiver;
    //santas are keys while receivers are values
    private HashMap<String, String> pairList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        randomGen = new Random();
        santaCounter = 0;
        santaIndex = 0;
        receiverIndex = 0;
        chosenSanta = null;
        chosenReceiver = null;
        pairList = new HashMap<>();
        gson = new Gson();
        myPrefs = getSharedPreferences(AddSecretSantaActivity.PREFS_NAME, 0);


        addButton = (FloatingActionButton) findViewById(R.id.fab_add);
        shuffleButton = (FloatingActionButton)findViewById(R.id.fab_shuffle);
        listView = (ListView) findViewById(R.id.ListViewSecretSantas);
        //longclick on shuffle fab clears the santaList
        addOnClickListeners();
        santaArrayList = new ArrayList<Santa>();
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadSavedSantas();
        updateSantaListView();
    }

    /**
     * Chooses a random Santa out of the set and assign him to another member.
     */
    public void startAssignment(View v) throws Exception{
        loadSavedSantas();
        //only do anything if there are at least 3 people in the list
        if(!(santaArrayList == null) && santaArrayList.size() > 2) {

            createReceiverList();

            //get the amount of initial santas once, before we take em out one for one, to assign them.
            santaCounter = santaArrayList.size();

            for (int i = 0; i < santaCounter; i++) {
                defineRandomPairs();

                while(checkIfPairIsInvalid()){
                    pairList.remove(chosenSanta.getEmail());
                    defineRandomPairs();
                }

                mailText = chosenReceiver.getFirstName() + chosenReceiver.getLastName() + " (" + chosenReceiver.getEmail() + ")";
                new EmailController(SMTPSERVER, SMTPPORT, NOREPLY, chosenSanta.getEmail(), password).sendEmail(getResources().getString(R.string.emailAssignmentSubject), mailText);

                //remove santa from santaList and receiver from receiverList
                santaArrayList.remove(santaIndex);
                receiverArrayList.remove(receiverIndex);
            }

            showPairList();

            Toast.makeText(getApplicationContext(), R.string.assignmentDoneToast, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, R.string.cant_assign_message, Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Debug information. Shows random assigned pair list.
     */
    private void showPairList(){
        Log.d("pairList", pairList.toString());
    }

    /**
     * Creates a random pair of "santa - receiver" and saves it into pairList.
     * This aint persistent though.
     */
    private void defineRandomPairs(){
        santaIndex = randomGen.nextInt(santaArrayList.size());
        receiverIndex = randomGen.nextInt(receiverArrayList.size());
        chosenSanta = santaArrayList.get(santaIndex);
        chosenReceiver = receiverArrayList.get(receiverIndex);
        pairList.put(chosenSanta.getEmail(), chosenReceiver.getEmail());
    }

    /**
     * Create a list for gift receivers. This is in fact a second santaList, because every santa
     * also is a receiver
     */
    private void createReceiverList(){
        //create and fill a receiver list with all santas, because every santa is also a receiver
        receiverArrayList = new ArrayList<Santa>();
        for (Santa temp : santaArrayList) {
            receiverArrayList.add(temp);
        }
    }

    /**
     * sends email notification to a santa, containing information about his gift receiver.
     * @param santaIndex
     */
    private void sendMailWithIntent(int santaIndex){
        //TODO: delete this method after implementing the noreply@noobits.de mailservice.
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto: " + santaArrayList.get(santaIndex).getEmail()));
        intent.putExtra(Intent.EXTRA_SUBJECT, "your secret santa gift receiver");

        //send email with the name of the chosen receiver to santa
        intent.putExtra(Intent.EXTRA_TEXT, chosenReceiver.getFirstName() + ", " + chosenReceiver.getLastName());
        if (intent != null) {
            startActivity(intent);//null pointer check in case package name was not found
        }
    }

    /**
     * checks the pair for equality and mutual order.
     * @return true if santa and receiver are the same or already in pairList, but in mutual order
     */
    private boolean checkIfPairIsInvalid(){

        if (chosenSanta.equals(chosenReceiver)
                || (pairList.containsKey(chosenReceiver.getEmail())
                && pairList.get(chosenReceiver.getEmail()).equals(chosenSanta.getEmail()))) {
            return true;
        }
        return false;
    }

    /**
     * Loads the saved list from sharedpreferences.
     */
    private void loadSavedSantas(){
        String santaListJson = myPrefs.getString("santaList", "");
        santaArrayList = gson.fromJson(santaListJson, new TypeToken<List<Santa>>(){}.getType());
    }

    /**
     * Initialises or updates the listView with the names.
     */
    private void updateSantaListView(){
        //concatenate name and forename of each santa and save it in a array called listViewContent
        if(santaArrayList != null) {
            int santaCounter = santaArrayList.size();
            listViewContent = new String[santaCounter];

            for (int i = 0; i < santaCounter; i++) {
                listViewContent[i] = santaArrayList.get(i).getFirstName() + " " + santaArrayList.get(i).getLastName();
            }

            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listViewContent);
            listView.setAdapter(adapter);
        }else{
            String[] empty = new String[0];
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, empty);
            listView.setAdapter(adapter);
        }
    }


    public void clickAddButton(View v){
        Intent intent = new Intent(MainActivity.this, AddSecretSantaActivity.class);
        MainActivity.this.startActivity(intent);
    }

    /**
     * Adds onClickListeners to buttons and listitems.
     */
    public void addOnClickListeners(){
        shuffleButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.remove("santaList");
                editor.apply();
                loadSavedSantas();
                updateSantaListView();
                return false;
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: create and open a contextmenu to edit/delete items
                return false;
            }
        });
    }


}
