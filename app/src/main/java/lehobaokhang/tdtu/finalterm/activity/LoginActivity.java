package lehobaokhang.tdtu.finalterm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import lehobaokhang.tdtu.finalterm.R;
import  lehobaokhang.tdtu.finalterm.adapter.LoginAdapter;

public class LoginActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    LoginAdapter adapter;
    TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null){
            setContentView(R.layout.activity_login);
            init();
            tabLayout.addTab(tabLayout.newTab().setText("Login"));
            tabLayout.addTab(tabLayout.newTab().setText("Sign up"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            adapter = new LoginAdapter(getSupportFragmentManager(), this,tabLayout.getTabCount());
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            tabLayout.setTranslationY(300);
            tabLayout.setAlpha(0);
            tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
        }
        else{
            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            DatabaseReference _myRef;
            String uid = firebaseUser.getUid();
            _myRef = mDatabase.getReference(uid);
            _myRef.child("PassLock").child("pass").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try{
                        String pass = snapshot.getValue().toString();
                        Intent i = new Intent(LoginActivity.this, Lock.class);
                        i.putExtra("Pass", pass);
                        startActivity(i);
                        return;
                    } catch (Exception e){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void init(){
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_paper);
        forgotPassword = findViewById(R.id.forgotPassword);
    }
}