package lehobaokhang.tdtu.finalterm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import lehobaokhang.tdtu.finalterm.activity.AddDiary;
import lehobaokhang.tdtu.finalterm.adapter.DiaryAdapter;
import lehobaokhang.tdtu.finalterm.model.Diary;

public class FragmentHome extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter diaryAdapter;
    RecyclerView.LayoutManager layoutManager;
    View view;

    ArrayList<Diary> diaryList;
    ArrayList<String> mKeys = new ArrayList<>();
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;
    Boolean flagSort = false;

    public FragmentHome(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = view.findViewById(R.id.diaryList);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        getData();
        diaryAdapter = new DiaryAdapter(this.getActivity(), diaryList);
        recyclerView.setAdapter(diaryAdapter);
    }

    private void getData() {
        diaryList = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        _myRef = mDatabase.getReference(uid).child("Diary");
        _myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Diary d = snapshot.getValue(Diary.class);
                if (d != null){
                    diaryList.add(d);
                    String key = snapshot.getKey();
                    mKeys.add(key);
                    try {
                        sortDirayList();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    diaryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Diary d = snapshot.getValue(Diary.class);
                if (d == null || diaryList == null || diaryList.isEmpty())
                    return;
                int index = mKeys.indexOf(snapshot.getKey());
                diaryList.set(index, d);
                try {
                    sortDirayList();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                diaryAdapter.notifyDataSetChanged();
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
                try {
                    sortDirayList();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                diaryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sortDirayList() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (int i = 0;i<diaryList.size() - 1;i++){
            int index = i;
            for (int j = i+1;j<diaryList.size();j++){
                if (flagSort && simpleDateFormat.parse(diaryList.get(index).getDate()).
                        compareTo(simpleDateFormat.parse(diaryList.get(j).getDate())) > 0){
                    index = j;
                }
                else if (!flagSort && simpleDateFormat.parse(diaryList.get(index).getDate()).
                        compareTo(simpleDateFormat.parse(diaryList.get(j).getDate())) < 0){
                    index = j;
                }
            }
            Diary temp = diaryList.get(index);
            diaryList.set(index, diaryList.get(i));
            diaryList.set(i, temp);

            String temp1 = mKeys.get(index);
            mKeys.set(index, mKeys.get(i));
            mKeys.set(i, temp1);
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add){
            Intent i = new Intent(getActivity(), AddDiary.class);
            startActivity(i);
        }
        if (item.getItemId() == R.id.menu_sort){
            if (!flagSort){
                flagSort = true;
                try {
                    sortDirayList();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                diaryAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "Sắp xếp theo ngày cũ nhất", Toast.LENGTH_SHORT).show();
            }
            else {
                flagSort = false;
                try {
                    sortDirayList();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                diaryAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "Sắp xếp theo ngày mới nhất", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}