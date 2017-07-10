package de.noobits.secretsantamanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncStatusObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;


/**
 * Created by Thomas on 04.07.2017.
 */

public class AddSecretSantaActivity extends AppCompatActivity{
    private Button cancelButton;
    private EditText EditText_LastName, EditText_FirstName, EditText_Email;
    private SharedPreferences preferences;
    public static final String PREFS_NAME = "My_Prefs";
    private ArrayList<Santa>  santaArrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_secret_santa);

        cancelButton = (Button)findViewById(R.id.button_cancel);
        preferences = getPreferences(MODE_PRIVATE);
    }

    /**
     * Go back to main activity. Redundant to the hardware back button.
     * @param v
     */
    public void clickCancelButton(View v){
        finish();
    }

    /**
     * Takes the data from the form and creates a new Santa with it.
     * Also saves the data persistent in shared preferences as object.
     * @param v
     */
    public void clickSaveButton(View v){

        String forename =  ((EditText) findViewById(R.id.edit_text_forename)).getText().toString();
        String name = ((EditText) findViewById(R.id.edit_text_name)).getText().toString();
        String email = ((EditText) findViewById(R.id.edit_text_email)).getText().toString();

        if(forename.equals("") || name.equals("") || email.equals("")){
            Toast.makeText(getApplicationContext(), R.string.fillFormMessage, Toast.LENGTH_LONG).show();
        }else {
            SharedPreferences myPrefs = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = myPrefs.edit();

            //create a Santa
            Santa newSanta = new Santa(forename, name, email);

            //convert newSanta with Gson into a JSON String
            Gson gson = new Gson();
            String santaJson = gson.toJson(newSanta);
            Log.d("clickSaveButton", santaJson);

            editor.putString("newSanta", santaJson);
            editor.commit();

            //close this activity
            finish();
        }
    }

    /**
     * Loads the saved list from sharedpreferences.
     */
    private void loadSavedSantas(){
        Gson gson = new Gson();

        //TODO: open sharedpreferences and get the list from there to save it in this class's list
        String santaListJson = preferences.getString("santaList", "");
        santaArrayList = gson.fromJson(santaListJson, new TypeToken<List<Santa>>(){}.getType());

    }


}
