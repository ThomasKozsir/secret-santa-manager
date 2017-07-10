package de.noobits.secretsantamanager;

import android.content.SharedPreferences;
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


/**
 * Created by Thomas on 04.07.2017.
 */

public class AddSecretSantaActivity extends AppCompatActivity{
    private SharedPreferences preferences;
    public static final String PREFS_NAME = "My_Prefs";
    private ArrayList<Santa>  santaArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_secret_santa);

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

            //get santaArrayList from sharedPreferences
            Gson gson = new Gson();
            String santaArrayListJson = myPrefs.getString("santaList", "[]");
            santaArrayList = gson.fromJson(santaArrayListJson, new TypeToken<List<Santa>>(){}.getType());

            //create a Santa
            Santa newSanta = new Santa(forename, name, email);

            //add newSanta to the santaArrayList
            santaArrayList.add(newSanta);

            //convert newSanta with Gson into a JSON String
            santaArrayListJson = gson.toJson(santaArrayList);
            Log.d("clickSaveButton", santaArrayListJson);

            editor.putString("santaList", santaArrayListJson);
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
