package android.example.edelegue.StudentModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.example.edelegue.ChatModule.MessageModel;
import android.example.edelegue.ChatModule.Model.User;
import android.example.edelegue.MainActivity;
import android.example.edelegue.ProfesorActivity;
import android.example.edelegue.R;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostContent extends AppCompatActivity {
    Intent intent;
    String postId;
    DocumentReference reference ;

    public TextView show_ObjTextView;
    public TextView show_body;
    public TextView show_auteur;
    public TextView show_date;
    public TextView show_fils;

    CircleImageView profile_image;
    TextView username;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_content);
        intent = getIntent();
        postId = intent.getStringExtra("post");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // and this
                startActivity(new Intent(PostContent.this, StudentActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseFirestore.getInstance().collection("Users").document(firebaseUser.getUid());


        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@NonNull DocumentSnapshot snapshot, @NonNull FirebaseFirestoreException e) {
                if (snapshot != null && snapshot.exists()) {
                    User user = snapshot.toObject(User.class);
                    username.setText(user.getUsername());
                    if (user.getImageURL().equals("default")){
                        profile_image.setImageResource(R.drawable.ic_action_contact);
                    } else {
                        //change this
                        Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                    }
                }
            }
        });

        reference = FirebaseFirestore.getInstance().collection("Posts").document(postId);

        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@NonNull DocumentSnapshot document, @NonNull FirebaseFirestoreException e) {

                if (document != null && document.exists()) {
                    SimpleDateFormat formater = new SimpleDateFormat("'le' dd/MM/yyyy 'à' hh:mm");
                    String date = formater.format(document.getTimestamp("datetime").toDate());

                    Post post = new Post(document.getId() , document.getString("auteur") , document.getString("body"), date , document.getString("fichier") , document.getString("objet"));

                    show_ObjTextView = findViewById(R.id.post_adapter_tv_title);
                    show_ObjTextView.setText("Objet : "+ post.getObjet());

                    show_body = findViewById(R.id.post_adapter_tv_description);
                    show_body.setText(post.getBody());

                    show_date = findViewById(R.id.post_adapter_tv_date);
                    show_date.setText(post.getDatetime());

                    show_fils = findViewById(R.id.post_adapter_tv_file);
                    show_fils.setText(post.getFichier());

                    DocumentReference referenceDoc = FirebaseFirestore.getInstance().collection("Users").document(post.getAuteur());
                    referenceDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot document = task.getResult();
                            String auteur = document.getString("username");
                            show_auteur = findViewById(R.id.post_adapter_tv_auteur);
                            show_auteur.setText("Pr. "+auteur);
                        }
                    });
                } else {
                    Log.d("chanegListner", "Current data: null");
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case  R.id.logout:
                FirebaseAuth.getInstance().signOut();
                // change this code beacuse your app will crash
                startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                return true;
        }

        return false;
    }
}
