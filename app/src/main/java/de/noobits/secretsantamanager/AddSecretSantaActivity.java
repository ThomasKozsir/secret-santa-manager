package de.noobits.secretsantamanager;

import android.content.Intent;
import android.content.SyncStatusObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


/**
 * Created by Thomas on 04.07.2017.
 */

public class AddSecretSantaActivity extends AppCompatActivity{
    private Button cancelButton;
    private EditText EditText_LastName, EditText_FirstName, EditText_Email;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_secret_santa);

        cancelButton = (Button)findViewById(R.id.button_cancel);
    }

    /**
     * Go back to main activity. Redundant to the hardware back button.
     * @param v
     */
    public void clickCancelButton(View v){
        Intent intent = new Intent(AddSecretSantaActivity.this, MainActivity.class);
        AddSecretSantaActivity.this.startActivity(intent);
    }

    /**
     * Takes the data from the form and saves it to the listview.
     * Also saves the data persistent in shared preferences.
     * @param v
     */
    public void clickSaveButton(View v){

        //create a new array with Santa data
        String[] data = new String[]{
                ((EditText) findViewById(R.id.edit_text_forename)).getText().toString(),
                ((EditText) findViewById(R.id.edit_text_name)).getText().toString(),
                ((EditText) findViewById(R.id.edit_text_email)).getText().toString()
        };

        //...and give it to the mainActivity to add it there to the list
        Intent intent = new Intent(AddSecretSantaActivity.this, MainActivity.class);
        intent.putExtra("santaData", data);
        AddSecretSantaActivity.this.startActivity(intent);

        //TODO: save data of items persistent in shared preferences
    }




}
