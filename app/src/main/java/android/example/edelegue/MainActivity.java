package android.example.edelegue;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private RadioGroup mRadioButton;

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
        mRadioButton = findViewById(R.id.RG_singIn);


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
                    Intent intentConnexion = new Intent(this, Connexion.class);
                    intentConnexion.putExtra("email" , email);
                    intentConnexion.putExtra("password" , password);
                    intentConnexion.putExtra("profile" , profile.getText().toString());
                    startActivity(intentConnexion);
                    //signInUser( email , password, profile.getText().toString());
                }
            }
        } else {
            //Toast.makeText(this, "s'il vous plaît insérer votre Email", Toast.LENGTH_LONG).show();
            mEmailEditText.setError("s'il vous plaît insérer votre Email");
            mEmailEditText.requestFocus();
        }
    }

    public void authCheck(FirebaseUser currentUser){

        String uID = currentUser.getUid();
        FirebaseFirestore.getInstance().collection("users").document(uID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.getString("profile").equals("Etudiant")){
                            Intent myIntent = new Intent(MainActivity.this , StudentActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            myIntent.putExtra("email" , documentSnapshot.getString("Email"));
                            myIntent.putExtra("User_Name" , documentSnapshot.getString("User_Name"));
                            myIntent.putExtra("profile" , documentSnapshot.getString("profile"));
                            startActivity(myIntent);
                        }else{
                            Intent myIntent = new Intent(MainActivity.this , ProfesorActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            myIntent.putExtra("email" , documentSnapshot.getString("Email"));
                            myIntent.putExtra("User_Name" , documentSnapshot.getString("User_Name"));
                            myIntent.putExtra("profile" , documentSnapshot.getString("profile"));
                            startActivity(myIntent);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        Intent myIntent = new Intent(MainActivity.this , MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        Log.d("dddd" , e.getMessage());
                        startActivity(myIntent);
                    }
                });


    }
    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uID = currentUser.getUid();
            FirebaseFirestore.getInstance().collection("users").document(uID).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.getString("profile").equals("Etudiant")){
                                Intent myIntent = new Intent(MainActivity.this , StudentActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                myIntent.putExtra("email" , documentSnapshot.getString("Email"));
                                myIntent.putExtra("User_Name" , documentSnapshot.getString("User_Name"));
                                myIntent.putExtra("profile" , documentSnapshot.getString("profile"));
                                startActivity(myIntent);
                            }else{
                                Intent myIntent = new Intent(MainActivity.this , ProfesorActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                myIntent.putExtra("email" , documentSnapshot.getString("Email"));
                                myIntent.putExtra("User_Name" , documentSnapshot.getString("User_Name"));
                                myIntent.putExtra("profile" , documentSnapshot.getString("profile"));
                                startActivity(myIntent);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent(MainActivity.this , MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            Log.d("dddd" , e.getMessage());
                            startActivity(myIntent);
                        }
                    });
        }

    }*/
}
