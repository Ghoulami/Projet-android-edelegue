package android.example.edelegue.ChatModule.Fragment;

import android.example.edelegue.ChatModule.Adapter.UserAdapter;
import android.example.edelegue.ChatModule.Model.Chat;
import android.example.edelegue.ChatModule.Model.User;
import android.example.edelegue.R;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<User> mUsers;

    FirebaseUser fuser;
    CollectionReference reference;

    private List<String> usersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();
        reference = FirebaseFirestore.getInstance().collection("Chats");

        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@NonNull QuerySnapshot value,
                                @NonNull FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("userFragment", "Listen failed.", e);
                    return;
                }
                for (QueryDocumentSnapshot document : value) {
                    Chat chat = document.toObject(Chat.class);

                    if (chat.getSender().equals(fuser.getUid())){
                        usersList.add(chat.getReceiver());
                    }

                    if (chat.getReceiver().equals(fuser.getUid())){
                        usersList.add(chat.getSender());
                    }
                }

                readChat();
            }

        });

        return view;
    }

    private void readChat(){
        mUsers = new ArrayList<User>();
        CollectionReference reference = FirebaseFirestore.getInstance().collection("Users");

        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@NonNull QuerySnapshot value,
                                @NonNull FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("userFragment", "Listen failed.", e);
                    return;
                }
                User user = new User();
                mUsers.clear();
                for (QueryDocumentSnapshot document : value) {
                    user = document.toObject(User.class);
                    for (String id : usersList){
                        if(user.getId().equals(id)){
                            if(mUsers.size() != 0){
                                for(User user1 : mUsers){
                                    if(!user.getId().equals(user1.getId())){
                                        mUsers.add(user);
                                    }
                                }
                            }else{
                                mUsers.add(user);
                            }
                        }
                    }

                }
                userAdapter = new UserAdapter(getContext() , mUsers , true);
                recyclerView.setAdapter(userAdapter);
            }
        });

    }

}
