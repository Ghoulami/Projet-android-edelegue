package android.example.edelegue.posts_operations;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.example.edelegue.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Add_post extends AppCompatActivity {

    private static final int CHOOSE_FILE_REQUESTCODE = 8777;
    Button send_btn, btn_choose;
    EditText title, content;
    TextView name_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        send_btn = findViewById(R.id.btn_file_send);
        btn_choose = findViewById(R.id.btn_file_choose);
        name_file = findViewById(R.id.name_file);

    }

    public void upload(View view) {
        String[] Types =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip",
                        "image/*"};
/*image/*|application/pdf|audio/*|application/zip|*/
        Intent intent= new Intent();
        intent.setType("");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent,"select a file"),CHOOSE_FILE_REQUESTCODE);

    }

    public void send(View view) {
    }
}
