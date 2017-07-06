package de.noobits.secretsantamanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;


/**
 * Created by Thomas on 04.07.2017.
 */

public class AddSecretSantaActivity extends AppCompatActivity{
    private Button cancelButton;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_secret_santa);

        cancelButton = (Button)findViewById(R.id.button_cancel);
    }


}
