package lehobaokhang.tdtu.finalterm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hanks.passcodeview.PasscodeView;

import lehobaokhang.tdtu.finalterm.R;
import lehobaokhang.tdtu.finalterm.fragment.FragmentSetting;

public class Lock extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;
    PasscodeView pcv_lock;
    String passcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        pcv_lock = findViewById(R.id.pcv_lock);
        String passcode = getIntent().getExtras().getString("Pass");
        if (passcode.length() < 6)
        {
            pcv_lock.setPasscodeLength(6)
                    .setListener(new PasscodeView.PasscodeViewListener() {
                        @Override
                        public void onFail() {
                            finish();
                        }
                        @Override
                        public void onSuccess(String number) {
                            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = user.getUid();
                            _myRef = mDatabase.getReference(uid);
                            _myRef.child("PassLock").child("pass").setValue(number);
                            finish();
                            finish();
                        }
                    });
        }
        else {
            pcv_lock.setPasscodeLength(6)
                    .setLocalPasscode(passcode)
                    .setListener(new PasscodeView.PasscodeViewListener() {
                        @Override
                        public void onFail() {
                            Toast.makeText(Lock.this, "Mật khẩu sai", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(String number) {
                            Intent i = new Intent(Lock.this, MainActivity.class);
                            startActivity(i);
                        }
                    });
        }
    }

}