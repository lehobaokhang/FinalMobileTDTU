package lehobaokhang.tdtu.finalterm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import  lehobaokhang.tdtu.finalterm.R;

public class SignUpTabFragment extends Fragment {
    EditText email, pass, username;
    Button signup;

    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.sign_up_tab_fragment,container, false);

        email = root.findViewById(R.id.newEmail);
        pass = root.findViewById(R.id.newPassword);
        username = root.findViewById(R.id.userName);
        signup = root.findViewById(R.id.btnSignUp);

        firebaseAuth = FirebaseAuth.getInstance();

        email.setTranslationY(300);
        email.setAlpha(0);
        email.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

        pass.setTranslationY(300);
        pass.setAlpha(0);
        pass.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

        username.setTranslationY(300);
        username.setAlpha(0);
        username.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

        signup.setTranslationY(300);
        signup.setAlpha(0);
        signup.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString().trim();
                String password = pass.getText().toString().trim();
                String user_name = username.getText().toString().trim();

                if (mail.isEmpty() || password.isEmpty() || user_name.isEmpty())
                    Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                else if (password.length()<8)
                    Toast.makeText(getContext(), "Password should greater than 8 digits", Toast.LENGTH_SHORT).show();
                else {
                    firebaseAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                sendEmailVertification();
                            }
                            else
                                Toast.makeText(getContext(), "Failed to Register", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        return root;
    }

    private void sendEmailVertification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getContext(), "Verification Email is sent, Verify and Login again", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    email.setText("");
                    email.requestFocus();
                    pass.setText("");
                    username.setText("");
                }
            });
        }
        else
            Toast.makeText(getContext(), "Failed to send Verification Email", Toast.LENGTH_SHORT).show();
    }
}
