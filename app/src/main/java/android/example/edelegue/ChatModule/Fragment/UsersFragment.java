package android.example.edelegue.ChatModule.Fragment;

import android.example.edelegue.ChatModule.Adapter.UserAdapter;
import android.example.edelegue.ChatModule.Model.User;
import android.example.edelegue.R;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {
    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<User> mUsers;

    public EditText search_users;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();

        readUsers();

        search_users = view.findViewById(R.id.search_users);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    private void searchUsers(String s) {

        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseFirestore.getInstance().collection("Users").orderBy("search")
                .startAt(s)
                .endAt(s+"\uf8ff");


        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@NonNull QuerySnapshot value,
                                @NonNull FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("userFragment", "Listen failed.", e);
                    return;
                }
                mUsers.clear();
                for (QueryDocumentSnapshot document : value) {

                    Log.d("dddd" , Integer.toString(value.size()));

                    User user = document.toObject(User.class);
                    Log.d("dddd" ,user.getId());

                    assert user != null;
                    assert fuser != null;
                    if (!user.getId().equals(fuser.getUid())) {
                        mUsers.add(user);
                    }else {
                        Log.d("chanegListner", "Current data: null");
                    }
                }

                userAdapter = new UserAdapter(getContext(), mUsers, false);
                recyclerView.setAdapter(userAdapter);
            }
        });

    }

    private void readUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference reference = FirebaseFirestore.getInstance().collection("Users");

        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@NonNull QuerySnapshot value,
                                @NonNull FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("userFragment", "Listen failed.", e);
                    return;
                }
                if (search_users.getText().toString().equals("")) {
                    mUsers.clear();
                    for (QueryDocumentSnapshot document : value) {

                        Log.d("dddd", Integer.toString(value.size()));

                        User user = document.toObject(User.class);
                        Log.d("dddd", user.getId());

                        assert user != null;
                        assert firebaseUser != null;
                        if (!user.getId().equals(firebaseUser.getUid())) {
                            mUsers.add(user);
                        } else {
                            Log.d("chanegListner", "Current data: null");
                        }
                    }
                }

                userAdapter = new UserAdapter(getContext(), mUsers, false);
                recyclerView.setAdapter(userAdapter);
            }
        });

    }

}
