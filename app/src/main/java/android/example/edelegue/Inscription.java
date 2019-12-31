package android.example.edelegue;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;



public class Inscription extends AppCompatActivity {
    private TextInputEditText mEmailEditText;
    private TextInputEditText mPasswordEditText;
    private TextInputEditText mConfPasswordEditText;
    private TextInputEditText mUsernameEditText;
    private RadioGroup mRadioButton;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        mAuth = FirebaseAuth.getInstance();

        mEmailEditText = findViewById(R.id.email_register_edit_text);
        mPasswordEditText = findViewById(R.id.password_register_edit_text);
        mConfPasswordEditText = findViewById(R.id.Conf_password_register_edit_text);
        mUsernameEditText = findViewById(R.id.username_register_edit_text);
        mRadioButton = findViewById(R.id.RG_register);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }

    public void inscription(View view) {
        String email = mEmailEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString();
        String confPassword = mConfPasswordEditText.getText().toString();
        String userName = mUsernameEditText.getText().toString();
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
                }else if (!password.equals(confPassword)) {
                    //Toast.makeText(this, "s'il vous plaît confirmer votre mot de passe", Toast.LENGTH_LONG).show();
                    mConfPasswordEditText.setError("s'il vous plaît confirmer votre mot de passe");
                    mPasswordEditText.requestFocus();
                } else {
                    //registerUser(userName , password, email, profile.getText().toString());
                }
            }
        } else {
            //Toast.makeText(this, "s'il vous plaît insérer votre Email", Toast.LENGTH_LONG).show();
            mEmailEditText.setError("s'il vous plaît insérer votre Email");
            mEmailEditText.requestFocus();
        }
    }

    /*
    private void registerUser(final String userName , String password, final String email, final String profile) {
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        User user = new User(userName, email, profile);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Inscription.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                    Intent myIntent = new Intent(Inscription.this , Acceuil.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    progressBar.setVisibility(View.GONE);
                                    startActivity(myIntent);
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(Inscription.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(Inscription.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
    }*/
}
