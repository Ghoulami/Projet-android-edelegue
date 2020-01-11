package android.example.edelegue;

import android.content.Intent;
import android.example.edelegue.ChatModule.MessageModel;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser currentUser;
    private TextInputEditText mEmailEditText;
    private TextInputEditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null){
            authCheck(currentUser);
        }else{
            setContentView(R.layout.activity_main);
        }

        mEmailEditText = findViewById(R.id.email_signIn_edit_text);
        mPasswordEditText = findViewById(R.id.password_signIn_edit_text);
    }



    public void connexion(View view) {
        String email = mEmailEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString();

        if (!email.isEmpty()) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                mEmailEditText.setError("la format de l'email n'est pas validé");
                mEmailEditText.requestFocus();
            }
            else{
                if (password.isEmpty() || password.length() < 8) {
                    mPasswordEditText.setError("le mot de passe doit contenir au moins 8 caractères");
                    mPasswordEditText.requestFocus();
                } else {
                    Intent intentConnexion = new Intent(this, Connexion.class);
                    intentConnexion.putExtra("email" , email);
                    intentConnexion.putExtra("password" , password);
                    startActivity(intentConnexion);
                }
            }
        } else {
            mEmailEditText.setError("s'il vous plaît insérer votre Email");
            mEmailEditText.requestFocus();
        }
    }

    public void authCheck(FirebaseUser currentUser){

        String uID = currentUser.getUid();
        FirebaseFirestore.getInstance().collection("Users").document(uID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.getString("profile").equals("Etudiant")){
                            Intent myIntent = new Intent(MainActivity.this , ProfesorActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(myIntent);
                        }else{
                            Intent myIntent = new Intent(MainActivity.this , ProfesorActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(myIntent);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        Intent myIntent = new Intent(MainActivity.this , MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(myIntent);
                    }
                });


    }

}
