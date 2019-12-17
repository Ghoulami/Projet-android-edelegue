package android.example.edelegue;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



public class Inscription extends AppCompatActivity {
    private TextInputEditText mEmailEditText;
    private TextInputEditText mPasswordEditText;
    private TextInputEditText mConfPasswordEditText;
    private TextInputEditText mUsernameEditText;
    private RadioGroup mRadioButton;

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
        mRadioButton = findViewById(R.id.GB_etudiant);
    }

    public void inscription(View view) {
        String email = mEmailEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString();
        String confPassword = mConfPasswordEditText.getText().toString();
        String userName = mUsernameEditText.getText().toString();
        int profile = mRadioButton.getCheckedRadioButtonId();

        if (!email.isEmpty()) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "la format de l'email n'est pas validé", Toast.LENGTH_LONG).show();
                mEmailEditText.setError("Enter Email");
                mEmailEditText.requestFocus();
            }
        } else {
            Toast.makeText(this, "s'il vous plaît insérer votre Email", Toast.LENGTH_LONG).show();
        }
        if (password.isEmpty() || password.length() < 8) {
            Toast.makeText(this, "le mot de passe doit contenir au moins 8 caractères", Toast.LENGTH_LONG).show();
        }else if (!password.equals(confPassword)) {
            Toast.makeText(this, "s'il vous plaît confirmer votre mot de passe", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "bien", Toast.LENGTH_LONG).show();
        }
    }
}
