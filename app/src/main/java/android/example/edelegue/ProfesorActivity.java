package android.example.edelegue;

import android.content.Intent;
import android.example.edelegue.ChatModule.MessageModel;
import android.example.edelegue.posts_operations.Add_post;
import android.example.edelegue.ui.professor_fragments.HomeFragment;
import android.example.edelegue.ui.professor_fragments.MessagesFragment;
import android.example.edelegue.ui.professor_fragments.PostsFragments;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;


public class ProfesorActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener , PostsFragments.OnFragmentInteractionListener, MessagesFragment.OnFragmentInteractionListener , NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView navigationView;
    NavController navController;
    ActionBarDrawerToggle toggle;

    TextView emailField , nameField;
    FirebaseUser firebaseUser;
    DocumentReference reference ;

    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_menu);

        //config dde la toolbare
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //config drawer layout
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                0,0);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home , new MessagesFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_message);
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_message, R.id.nav_myposts /*R.id.nav_signout*/)
                .setDrawerLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        // 4 - Handle Navigation Item Click
        int id = item.getItemId();
        switch(id){
            case R.id.nav_message:
                startActivity(new Intent(this, MessageModel.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home,new HomeFragment()).commit();
                break;

            case R.id.nav_myposts:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_post,new PostsFragments()).commit();
                break;
        }
        this.drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case  R.id.logout:
                FirebaseAuth.getInstance().signOut();
                // change this code beacuse your app will crash
                startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
        }

        return false;
    }


    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    public void ouvrir_add_post(View view) {
        Intent myIntent = new Intent(getBaseContext(),Add_post.class);
        startActivity(myIntent);
    }
}
