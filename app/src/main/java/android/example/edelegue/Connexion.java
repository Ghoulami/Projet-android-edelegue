package android.example.edelegue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.example.edelegue.ChatModule.MessageModel;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class Connexion extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private String uID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        Intent intentConnexion = getIntent();
        String email = intentConnexion.getStringExtra("email");
        String password = intentConnexion.getStringExtra("password");
        String profile = intentConnexion.getStringExtra("profile");

        signInUser( email , password, profile);
    }

    public void signInUser(String email , String password , String profil){

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            uID = mAuth.getCurrentUser().getUid();
                            Toast.makeText(Connexion.this, uID, Toast.LENGTH_LONG).show();
                            FirebaseFirestore.getInstance().collection("Users").document(uID).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.getString("profile").equals("Etudiant")){
                                                Intent myIntent = new Intent(Connexion.this , ProfesorActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                Toast.makeText(Connexion.this,  documentSnapshot.getString("User_Name"), Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(Connexion.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                                startActivity(myIntent);
                                            }else{
                                                Intent myIntent = new Intent(Connexion.this , ProfesorActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                Toast.makeText(Connexion.this,  documentSnapshot.getString("User_Name"), Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(Connexion.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                                startActivity(myIntent);
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Connexion.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                            Intent myIntent = new Intent(Connexion.this , MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            Log.d("dddd" , e.getMessage());
                                            startActivity(myIntent);
                                        }
                                    });

                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Connexion.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent(Connexion.this , MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(myIntent);
                        }
                    }
                });
    }
}