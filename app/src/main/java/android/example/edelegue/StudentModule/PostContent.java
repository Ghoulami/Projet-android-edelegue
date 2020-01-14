package android.example.edelegue.StudentModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.example.edelegue.ChatModule.MessageModel;
import android.example.edelegue.ChatModule.Model.User;
import android.example.edelegue.MainActivity;
import android.example.edelegue.ProfesorActivity;
import android.example.edelegue.R;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class PostContent extends AppCompatActivity {
    Intent intent;
    String postId;
    DocumentReference reference ;

    public TextView show_ObjTextView;
    public TextView show_body;
    public TextView show_auteur;
    public TextView show_date;
    public TextView show_fils;
    public String fileName , file_uri;
    MaterialButton downloadFile;

    CircleImageView profile_image;
    TextView username;
    FirebaseUser firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_content);

        downloadFile = findViewById(R.id.BT_DOWNLOAD);

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
                    String date = formater.format(document.getTimestamp("currentTime").toDate());

                    Post post = new Post(document.getId() , document.getString("user_id") , document.getString("content"), date , document.getString("img_url") , document.getString("title"), document.getString("file_name"));

                    show_ObjTextView = findViewById(R.id.post_adapter_tv_title);
                    show_ObjTextView.setText("Objet : "+ post.getObjet());

                    show_body = findViewById(R.id.post_adapter_tv_description);
                    show_body.setText(post.getBody());

                    show_date = findViewById(R.id.post_adapter_tv_date);
                    show_date.setText(post.getDatetime());

                    if (post.getFichier() != null){
                        downloadFile.setVisibility(View.VISIBLE);
                        show_fils = findViewById(R.id.post_adapter_tv_file);
                        show_fils.setText(post.getFichier());

                        file_uri = post.getFichier();
                        fileName = post.getFile_name();
                        Log.d("ddd" , fileName);
                    }


                    downloadFile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String[] arrOfStr = fileName.split("\\.");
                            Log.d("ddd" , fileName);
                            String name = arrOfStr[0];
                            String exten = arrOfStr[1];

                            Log.d("ddd" , file_uri);

                            downloadFile(PostContent.this, name , "."+exten, DIRECTORY_DOWNLOADS, file_uri);
                        }
                    });


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


    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {
        Log.d("dddd" , file_uri);
        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        downloadmanager.enqueue(request);
    }

}
