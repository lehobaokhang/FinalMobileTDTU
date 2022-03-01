package lehobaokhang.tdtu.finalterm.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import lehobaokhang.tdtu.finalterm.R;
import lehobaokhang.tdtu.finalterm.adapter.DiaryAdapter;
import lehobaokhang.tdtu.finalterm.model.Diary;

public class FragmentSearch extends Fragment {

    ArrayList<Diary> diaryList, result;
    ArrayList<String> mKeys = new ArrayList<>();
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;
    EditText etSearch;
    View view;
    RecyclerView recyclerView;
    RecyclerView.Adapter diaryAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getData();
        etSearch = view.findViewById(R.id.etSearch);

        recyclerView = view.findViewById(R.id.rvSearch);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        result = new ArrayList<>();
        diaryAdapter = new DiaryAdapter(this.getActivity(), result);
        recyclerView.setAdapter(diaryAdapter);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                result.removeAll(result);
                diaryAdapter.notifyDataSetChanged();
                if (etSearch.getText().toString().equals("") == false)
                    search(etSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    private void search(String etSearch) {
        for (Diary i:diaryList){
            if (i.getTitle().toUpperCase().indexOf(etSearch.toUpperCase()) >= 0 && result.indexOf(i) < 0) {
                result.add(i);
                diaryAdapter.notifyDataSetChanged();
            }
        }
    }

    private void getData() {
        diaryList = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        _myRef = mDatabase.getReference(uid);
        _myRef.child("Diary").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Diary d = snapshot.getValue(Diary.class);
                if (d != null){
                    diaryList.add(d);
                    String key = snapshot.getKey();
                    mKeys.add(key);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Diary d = snapshot.getValue(Diary.class);
                if (d == null || diaryList == null || diaryList.isEmpty())
                    return;
                int index = mKeys.indexOf(snapshot.getKey());
                diaryList.set(index, d);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Diary d = snapshot.getValue(Diary.class);
                if (d == null || diaryList == null || diaryList.isEmpty())
                    return;
                int index = mKeys.indexOf(snapshot.getKey());
                if (index != -1){
                    diaryList.remove(index);
                    mKeys.remove(index);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}