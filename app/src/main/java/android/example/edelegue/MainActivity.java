package android.example.edelegue;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputEditText mEmailEditText;
    private TextInputEditText mPasswordEditText;
    private RadioGroup mRadioButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmailEditText = findViewById(R.id.email_signIn_edit_text);
        mPasswordEditText = findViewById(R.id.password_signIn_edit_text);
        mRadioButton = findViewById(R.id.RG_singIn);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
    }

    public void inscription(View view) {
        Intent myIntent = new Intent(MainActivity.this , Inscription.class);
        startActivity(myIntent);
    }

    public void connexion(View view) {
        String email = mEmailEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString();
        RadioButton profile = findViewById(mRadioButton.getCheckedRadioButtonId());

        if (!email.isEmpty()) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                //Toast.makeText(this, "la format de l'email n'est pas validé", Toast.LENGTH_LONG).show();
                mEmailEditText.setError("la format de l'email n'est pas validé");
                mEmailEditText.requestFocus();
            }
            else{
                if (password.isEmpty() || password.length() < 8) {
                    //Toast.makeText(this, "le mot de passe doit contenir au moins 8 caractères", Toast.LENGTH_LONG).show();
                    mPasswordEditText.setError("le mot de passe doit contenir au moins 8 caractères");
                    mPasswordEditText.requestFocus();
                } else {
                    signInUser( email , password, profile.getText().toString());
                }
            }
        } else {
            //Toast.makeText(this, "s'il vous plaît insérer votre Email", Toast.LENGTH_LONG).show();
            mEmailEditText.setError("s'il vous plaît insérer votre Email");
            mEmailEditText.requestFocus();
        }
    }

    public void signInUser(String email , String password , String profil){

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                        Intent myIntent = new Intent(MainActivity.this , Acceuil.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(myIntent);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent myIntent = new Intent(MainActivity.this , Acceuil.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
        }

    }
}
