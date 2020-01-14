package android.example.edelegue.ui.professor_fragments;

import android.example.edelegue.PostAdapter;
import android.example.edelegue.StudentModule.Post;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.example.edelegue.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;

    private PostAdapter postAdapter;
    private List<Post> mPosts;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.studientPost_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));


        mPosts = new ArrayList<>();

        readPosts();

        return view;

    }


    private void readPosts() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference reference = FirebaseFirestore.getInstance().collection("Posts");

        reference.orderBy("currentTime", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@NonNull QuerySnapshot value,
                                @NonNull FirebaseFirestoreException e) {

                if (FirebaseAuth.getInstance().getCurrentUser() != null){
                    mPosts.clear();
                    for (QueryDocumentSnapshot document : value) {

                        SimpleDateFormat formater = new SimpleDateFormat("'le' dd/MM/yyyy 'à' HH:mm");
                        String date = formater.format(document.getTimestamp("currentTime").toDate());

                        Post post = new Post(document.getId() , document.getString("user_id") , document.getString("content"), date , document.getString("img_url") , document.getString("title") , document.getString("file_name"));

                        assert post != null;
                        assert firebaseUser != null;

                        mPosts.add(post);

                    }
                }


                postAdapter = new PostAdapter(mPosts);
                recyclerView.setAdapter(postAdapter);
            }
        });

    }

    public interface OnFragmentInteractionListener {

    }
}





