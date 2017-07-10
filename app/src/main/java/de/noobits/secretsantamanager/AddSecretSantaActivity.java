package de.noobits.secretsantamanager;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Created by Thomas on 04.07.2017.
 */

public class AddSecretSantaActivity extends AppCompatActivity{
    private SharedPreferences preferences;
    public static final String PREFS_NAME = "My_Prefs";
    private ArrayList<Santa>  santaArrayList;
    private boolean formValid;
    private int debugCounter = 1; //TODO: delete after alpha

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_secret_santa);
        formValid = false;
        preferences = getPreferences(MODE_PRIVATE);

        createListeners();
    }

    /**
     * Empty the form and go back to main activity.
     * @param v
     */
    public void clickCancelButton(View v){

        finish();
    }

    /**
     * Creates a new Santa by input data.
     * Also saves the data persistent in shared preferences as a santa-object.
     * @param v
     */
    public void clickSaveButton(View v){

        String forename =  ((EditText) findViewById(R.id.edit_text_forename)).getText().toString();
        String name = ((EditText) findViewById(R.id.edit_text_name)).getText().toString();
        String email = ((EditText) findViewById(R.id.edit_text_email)).getText().toString();

        if(forename.equals("") || name.equals("") || email.equals("") || !formValid){
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

    /**
     * Resets the EditTextcontaining the first name.
     * @param v
     */
    public void clickResetFirstName(View v){
        ((TextView)findViewById(R.id.edit_text_forename)).setText("");
    }

    /**
     * Resets the EditText containing the last name.
     * @param v
     */
    public void clickResetName(View v){
        ((TextView)findViewById(R.id.edit_text_name)).setText("");
    }

    /**
     * Resets the EditText containing the email.
     * @param v
     */
    public void clickResetEmail(View v){
        ((TextView)findViewById(R.id.edit_text_email)).setText("");
    }

    /**
     * Initializes TextChangedListeners.
     */
    public void createListeners(){
        final EditText editText_email = (EditText)findViewById(R.id.edit_text_email);
        editText_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //check if the email adress contains an ampersand
                if(!Pattern.matches(".+@.+", editText_email.getText())){
                    findViewById(R.id.text_view_email_error_hint).setVisibility(View.VISIBLE);
                    formValid = false;
                }else if(editText_email.getText().equals("")){
                    findViewById(R.id.text_view_email_error_hint).setVisibility(View.VISIBLE);
                    formValid = false;
                }else{
                    findViewById(R.id.text_view_email_error_hint).setVisibility(View.INVISIBLE);
                    formValid = true;
                }
            }
        });
    }

    /**
     * Debugging button to fill in the form correct quickly. //TODO: delete method after alpha
     */
    public void quickFill(View v){
        ((EditText)findViewById(R.id.edit_text_forename)).setText("Max");
        ((EditText)findViewById(R.id.edit_text_name)).setText("Mustermann");
        ((EditText)findViewById(R.id.edit_text_email)).setText("Alabastia@pokeliga.com");
    }

}
