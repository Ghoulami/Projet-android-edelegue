package android.example.edelegue.ChatModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.example.edelegue.ChatModule.Fragment.ChatsFragment;
import android.example.edelegue.ChatModule.Fragment.UsersFragment;
import android.example.edelegue.ChatModule.Model.User;
import android.example.edelegue.MainActivity;
import android.example.edelegue.ProfesorActivity;
import android.example.edelegue.R;
import android.example.edelegue.StudentModule.StudentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import java.util.ArrayList;
import java.util.HashMap;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageModel extends AppCompatActivity {
    CircleImageView profile_image;
    TextView username;

    FirebaseUser firebaseUser;
    DocumentReference reference ;
    String profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseFirestore.getInstance().collection("Users").document(firebaseUser.getUid());

        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@NonNull DocumentSnapshot snapshot, @NonNull FirebaseFirestoreException e) {

                if (snapshot != null && snapshot.exists()) {
                    User user = snapshot.toObject(User.class);
                    profile = user.getProfile();
                    username.setText(user.getUsername());
                    if (user.getImageURL().equals("default")){
                        profile_image.setImageResource(R.drawable.ic_action_contact);
                    } else {
                        //change this
                        Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                    }
                } else {
                    Log.d("chanegListner", "Current data: null");
                }
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status("offline");
                // and this
                if (profile.equals("Enseignant")){
                    startActivity(new Intent(MessageModel.this, ProfesorActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
                else{
                    startActivity(new Intent(MessageModel.this, StudentActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
                finish();
            }
        });


        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);





        final TabLayout tabLayout = findViewById(R.id.tab_layout);
        final ViewPager viewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new ChatsFragment(), "DISC");

        viewPagerAdapter.addFragment(new UsersFragment(), "Contacts");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        // Ctrl + O

        @NonNull
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    private void status(String status){
        reference = FirebaseFirestore.getInstance().collection("Users").document(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.update(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

}
