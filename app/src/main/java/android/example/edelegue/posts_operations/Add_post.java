package android.example.edelegue.posts_operations;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.example.edelegue.R;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;



public class Add_post extends AppCompatActivity {
    private StorageReference folder;

    private static final int CHOOSE_FILE_REQUESTCODE = 8777;
    Button send_btn, btn_choose;
    EditText title, content;
    TextView name_file;
    TextView date_post;
    private Uri filePath;
    FirebaseFirestore db;
    FirebaseUser curentUser;
    FirebaseAuth mAuth;
    String Uri_file;
    String file_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        //intialise the frebsare instance
        folder = FirebaseStorage.getInstance().getReference().child("imageFolder");

        send_btn = findViewById(R.id.btn_file_send);
        btn_choose = findViewById(R.id.btn_file_choose);
        name_file = findViewById(R.id.name_file);
        title = findViewById(R.id.title_post);
        date_post = findViewById(R.id.date_post);

        content = findViewById(R.id.content_post);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        curentUser = mAuth.getCurrentUser();

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        SimpleDateFormat formater = new SimpleDateFormat("'le' dd/MM/yyyy 'à' hh:mm");
        String date = formater.format(Timestamp.now().toDate());
        date_post.setText(date);

    }

    public void upload(View view) {
        String[] Types =
                {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip",
                        "image/*"};
        /*image/*|application/pdf|audio/*|application/zip|*/
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "select a file"), CHOOSE_FILE_REQUESTCODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_FILE_REQUESTCODE && resultCode == RESULT_OK && data != null && data.getData() != null) {


            filePath = data.getData();
            Cursor returnCursor =
                    getContentResolver().query(filePath, null, null, null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();

            name_file.setText(returnCursor.getString(nameIndex));
            file_name = returnCursor.getString(nameIndex);

            Toast.makeText(getApplicationContext(), "le fichier est importé avec succes", Toast.LENGTH_LONG).show();

            final Uri imageurl = data.getData();
            final StorageReference fileimage = folder.child("image n :"+imageurl.getLastPathSegment());
            fileimage.putFile(imageurl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileimage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri_file = String.valueOf(uri);

                        }
                    });
                }
            });


        } else {
            Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();

        }
    }



    public void send(View view) {


        Post post1 = new Post(title.getText().toString(),content.getText().toString(),Uri_file,curentUser.getUid(),Timestamp.now() , file_name);
        db.collection("Posts").document().set(post1)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "data stored sussfully", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
