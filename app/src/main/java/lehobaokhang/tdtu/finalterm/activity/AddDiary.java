package lehobaokhang.tdtu.finalterm.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import lehobaokhang.tdtu.finalterm.R;
import lehobaokhang.tdtu.finalterm.adapter.SpinnerAdaper;
import lehobaokhang.tdtu.finalterm.model.Diary;

public class AddDiary extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    TextView tvDateAdd, tvTitleAdd, tvContentAdd;
    String date, reactionSelected, weatherSelected;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Spinner spinnerReaction, spinnerWeather;
    SpinnerAdaper spinnerAdaperReaction, spinnerAdaperWeather;
    int[] imgReaction = {R.drawable.veryhappy, R.drawable.happy, R.drawable.sad, R.drawable.tired, R.drawable.worry, R.drawable.angry, R.drawable.surprise};
    int[] imgWeather = {R.drawable.ic_baseline_cloud_24, R.drawable.ic_baseline_wb_sunny_24, R.drawable.rain};
    ArrayList<String> reactionDescription = new ArrayList<String>(Arrays.asList("veryhappy", "happy", "sad", "tired", "worry", "angry", "surpise"));
    ArrayList<String> weatherDescription = new ArrayList<String>(Arrays.asList("cloud", "sunny", "rain"));
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;
    boolean flag = false;
    Diary d;
    FloatingActionButton btnMic;
    ConstraintLayout Diary_color_add;
    String colorpick = "#FFFFFF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
        
        inIt();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            d = (Diary) bundle.getSerializable("object_diary");
            getData(d);
        } else {
            flag = true;
            date = simpleDateFormat.format(new Date());
            formatDate(date);
        }

        tvDateAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate();
            }
        });

        spinnerAdaperReaction = new SpinnerAdaper(AddDiary.this, imgReaction);
        spinnerAdaperWeather = new SpinnerAdaper(AddDiary.this, imgWeather);

        spinnerReaction.setAdapter(spinnerAdaperReaction);
        spinnerWeather.setAdapter(spinnerAdaperWeather);

        spinnerReaction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reactionSelected = reactionDescription.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        spinnerWeather.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                weatherSelected =  weatherDescription.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        btnMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });
    }

    private void selectDate() {
        Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i, i1, i2);
                date = simpleDateFormat.format(calendar.getTime());
                formatDate(date);
            }
        }, nam, thang, ngay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void inIt() {
        tvDateAdd = findViewById(R.id.tvDateAdd);
        tvTitleAdd = findViewById(R.id.tvTitleAdd);
        tvContentAdd = findViewById(R.id.tvContentAdd);
        spinnerReaction = findViewById(R.id.spinnerReaction);
        spinnerWeather = findViewById(R.id.spinnerWeather);
        btnMic = findViewById(R.id.btnMic);
        Diary_color_add = findViewById(R.id.Diary_color_add);
    }

    private void formatDate(String d){
        String[] month = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String[] date = d.split("/");
        Spannable spn = new SpannableString(date[0] + " " + month[Integer.parseInt(date[1]) - 1] + ", " + date[2]);
        spn.setSpan(new RelativeSizeSpan(2f), 0,2, 0);
        spn.setSpan(new ForegroundColorSpan(Color.RED), 0, 2, 0);
        spn.setSpan(new StyleSpan(Typeface.BOLD), 0, 2, 0);
        tvDateAdd.setText(spn);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_diary, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_save){
            setData();
            finish();
        }
        if (item.getItemId() == R.id.menu_bg_change) {

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
            android.view.View bottomView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.background_change, (LinearLayout) findViewById(R.id.bg_setting));
            bottomView.findViewById(R.id.view).setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    colorpick = "#FFFFFFFF";
                    Diary_color_add.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                }
            });
            bottomView.findViewById(R.id.view1).setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    colorpick = "#F3E6E3";
                    Diary_color_add.setBackgroundColor(Color.parseColor("#F3E6E3"));
                }
            });
            bottomView.findViewById(R.id.view2).setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    colorpick = "#EFBBCF";
                    Diary_color_add.setBackgroundColor(Color.parseColor("#EFBBCF"));
                }
            });
            bottomView.findViewById(R.id.view3).setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    colorpick = "#D6B0B1";
                    Diary_color_add.setBackgroundColor(Color.parseColor("#D6B0B1"));
                }
            });
            bottomView.findViewById(R.id.view4).setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    colorpick = "#CCF6C8";
                    Diary_color_add.setBackgroundColor(Color.parseColor("#CCF6C8"));
                }
            });
            bottomView.findViewById(R.id.view5).setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    colorpick = "#FFE0AC";
                    Diary_color_add.setBackgroundColor(Color.parseColor("#FFE0AC"));
                }
            });
            bottomSheetDialog.setContentView(bottomView);
            bottomSheetDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getData(Diary d){
        date = d.getDate();
        formatDate(date);
        spinnerReaction.setSelection(reactionDescription.indexOf(reactionSelected));
        spinnerWeather.setSelection(weatherDescription.indexOf(weatherSelected));
        tvTitleAdd.setText(d.getTitle());
        tvContentAdd.setText(d.getContent());
        Diary_color_add.setBackgroundColor(Color.parseColor(d.getColor()));
    }

    public void setData(){
        if (flag)
            d = new Diary();
        d.setTitle(tvTitleAdd.getText().toString());
        d.setContent(tvContentAdd.getText().toString());
        d.setDate(date);
        d.setReaction(reactionSelected);
        d.setWeather(weatherSelected);
        d.setColor(colorpick);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        _myRef = mDatabase.getReference(uid);
        if (flag){
            String _id = _myRef.push().getKey();
            d.setId(_id);
            _myRef.child("Diary").child(_id).setValue(d);
        }
        else{
            _myRef.child("Diary").child(d.getId()).setValue(d);
            Intent i =new Intent(AddDiary.this, DetailDiary.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("object_diary", d);
            i.putExtras(bundle);
            startActivity(i);
        }
    }

    private void speak () {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak something");
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT){
            if (resultCode == RESULT_OK && data != null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                tvContentAdd.setText(tvContentAdd.getText().toString() + " " + Objects.requireNonNull(result).get(0));
            }
        }
    }
}