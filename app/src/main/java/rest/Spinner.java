package rest;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.abhinav.tsdhub.R;

/**
 * Created by abhinav on 28-12-2016.
 */

public class Spinner extends AppCompatActivity {
    TextView network;
    ProgressBar spinner;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.a_spinner);
        spinner=(ProgressBar)findViewById(R.id.loadingSpinner);
        network=(TextView)findViewById(R.id.network);
    }
}
