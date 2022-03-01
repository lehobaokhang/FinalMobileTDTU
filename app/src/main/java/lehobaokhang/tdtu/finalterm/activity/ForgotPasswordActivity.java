package lehobaokhang.tdtu.finalterm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import lehobaokhang.tdtu.finalterm.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText edt_MailtoRecover;
    TextView tv_backLogin;
    Button btnPassRecover;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        init();

        firebaseAuth = FirebaseAuth.getInstance();

        tv_backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnPassRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = edt_MailtoRecover.getText().toString().trim();
                if (mail.isEmpty())
                    Toast.makeText(getApplicationContext(), "Enter your mail first", Toast.LENGTH_SHORT).show();
                else
                {
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Mail sent, you can recover your password using mail", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Email is Wrong or Account not exist ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public void init()
    {
        edt_MailtoRecover = findViewById(R.id.edt_MailtoRecover);
        tv_backLogin = findViewById(R.id.tv_backLogin);
        btnPassRecover = findViewById(R.id.btnPassRecover);
    }
}