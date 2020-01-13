package android.example.edelegue;

import android.content.Intent;
import android.example.edelegue.ChatModule.MessageModel;
import android.example.edelegue.StudentModule.Post;
import android.example.edelegue.StudentModule.PostContent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<Post> mPosts;

    public PostAdapter(List<Post> mPosts){
        this.mPosts = mPosts;
    }

    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final PostAdapter.ViewHolder holder, int position) {
        final Post post = mPosts.get(position);
        DocumentReference referenceDoc = FirebaseFirestore.getInstance().collection("Users").document(post.getAuteur());
        referenceDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                String auteur = document.getString("username");
                holder.show_ObjTextView.setText("Object : " + post.getObjet());
                holder.show_body.setText(post.getBody());
                holder.show_auteur.setText("Pr. "+auteur);
                holder.show_date.setText(post.getDatetime().toString());
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), PostContent.class);
                intent.putExtra("post",post.getId());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPosts.size() ;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_ObjTextView;
        public TextView show_body;
        public TextView show_auteur;
        public TextView show_date;



        public ViewHolder(View itemView) {
            super(itemView);

            show_ObjTextView = itemView.findViewById(R.id.post_adapter_tv_title);
            show_body = itemView.findViewById(R.id.post_adapter_tv_description);
            show_auteur = itemView.findViewById(R.id.post_adapter_tv_auteur);
            show_date = itemView.findViewById(R.id.post_adapter_tv_date);

        }
    }
}
