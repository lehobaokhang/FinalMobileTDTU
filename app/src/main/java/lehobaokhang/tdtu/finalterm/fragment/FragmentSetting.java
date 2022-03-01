package lehobaokhang.tdtu.finalterm.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import lehobaokhang.tdtu.finalterm.R;
import lehobaokhang.tdtu.finalterm.activity.Lock;
import lehobaokhang.tdtu.finalterm.activity.LoginActivity;

import android.content.SharedPreferences;

public class FragmentSetting extends Fragment {

    TextView tvUserName;
    ListView lvSetting;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inIt();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        tvUserName.setText(user.getEmail());
        String[] setting = {"Đặt nhắc nhở", "Sao lưu", "Khôi phục","Light Mode", "Đặt mật khẩu khóa", "Đăng xuất"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, setting);

        //Dark mode
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("share", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final  boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);



        lvSetting.setAdapter(adapter);
        lvSetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Toast.makeText(getActivity(), "Đặt nhắc nhở", Toast.LENGTH_SHORT).show();
                } else if (i == 1) {
                    Toast.makeText(getActivity(), "Sao lưu", Toast.LENGTH_SHORT).show();
                } else if (i == 2) {
                    Toast.makeText(getActivity(), "Khôi phục", Toast.LENGTH_SHORT).show();
                } else if (i == 3) {

                    if (setting[i].equals("Light Mode")) {

                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        editor.apply();
                        setting[i] = "Dark Mode";
                        //editor.putBoolean("isDarkModeOn", false);


                    } else if (setting[i].equals("Dark Mode")) {

                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        editor.apply();
                        setting[i] = "Light Mode";
                        //editor.putBoolean("isDarkModeOn", true);


                    }
                    adapter.notifyDataSetChanged();
                }

                else if (i == 4){
                    Intent intent = new Intent(getActivity(), Lock.class);
                    intent.putExtra("Pass", "12345");
                    startActivity(intent);
                }
                else if (i == 5){
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
    }


    public void inIt(){
        tvUserName = view.findViewById(R.id.tvUserName);
        lvSetting = view.findViewById(R.id.lvSetting);
    }
}