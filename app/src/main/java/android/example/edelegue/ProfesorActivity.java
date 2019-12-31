package android.example.edelegue;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfesorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesor);

        Intent in = getIntent();
        TextView T = findViewById(R.id.textView1);
        T.setText("email : " + in.getStringExtra("email") + "\nUser Name : " + in.getStringExtra("User_Name") + "\nProfile : " +in.getStringExtra("profile") );
    }
}
