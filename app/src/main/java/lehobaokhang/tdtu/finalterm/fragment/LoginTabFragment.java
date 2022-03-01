package lehobaokhang.tdtu.finalterm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import lehobaokhang.tdtu.finalterm.R;
import   lehobaokhang.tdtu.finalterm.activity.ForgotPasswordActivity;
import lehobaokhang.tdtu.finalterm.activity.Lock;
import  lehobaokhang.tdtu.finalterm.activity.MainActivity;
import lehobaokhang.tdtu.finalterm.databinding.ActivityMainBinding;

public class LoginTabFragment extends Fragment {
    public static final int REQUEST_CODE_ADD_NOTE = 1;
    private static final int RC_SIGN_IN = 123;
    EditText email, pass;
    TextView forgotPass;
    Button login;
    LinearLayout googleLogin;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    private GoogleSignInClient googleSignInClient;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment,container, false);

        email = root.findViewById(R.id.inputEmail);
        pass = root.findViewById(R.id.inputPassword);
        forgotPass = root.findViewById(R.id.forgotPassword);
        login = root.findViewById(R.id.btnLogin);
        googleLogin = root.findViewById(R.id.googleLogin);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(), googleSignInOptions);

        if (firebaseUser!=null)
        {
            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            DatabaseReference _myRef;
            String uid = firebaseUser.getUid();
            _myRef = mDatabase.getReference(uid);
            _myRef.child("PassLock").child("pass").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try{
                        String pass = snapshot.getValue().toString();
                        Intent i = new Intent(getActivity(), Lock.class);
                        i.putExtra("Pass", pass);
                        startActivity(i);
                    } catch (Exception e){
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        email.setTranslationY(300);
        email.setAlpha(0);
        email.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

        pass.setTranslationY(300);
        pass.setAlpha(0);
        pass.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

        forgotPass.setTranslationY(300);
        forgotPass.setAlpha(0);
        forgotPass.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

        login.setTranslationY(300);
        login.setAlpha(0);
        login.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getActivity(), ForgotPasswordActivity.class),REQUEST_CODE_ADD_NOTE
                );
            }
        });
        
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString().trim();
                String password = pass.getText().toString().trim();
                
                if (mail.isEmpty() || password.isEmpty())
                    Toast.makeText(getContext(), "Email or Password is empty", Toast.LENGTH_SHORT).show();
                else {
                    firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                                checkMailVerfication();
                            else
                                Toast.makeText(getContext(), "Account doesn't exist", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        return root;
    }

    private void checkMailVerfication(){
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser.isEmailVerified()==true)
        {
            Toast.makeText(getContext(), "Logged In", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
        else {
            Toast.makeText(getContext(), "Verify your mail first", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
            email.setText("");
            email.requestFocus();
            pass.setText("");
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
                            // Sign in success, update UI with the signed-in user's information
                        } else {
                            Toast.makeText(getContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
