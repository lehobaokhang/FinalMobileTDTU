package lehobaokhang.tdtu.finalterm.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import lehobaokhang.tdtu.finalterm.R;
import lehobaokhang.tdtu.finalterm.model.Diary;

public class DetailDiary extends AppCompatActivity {

    Diary d;
    TextView tvDateDetail, tvTitleDetail, tvContentDetail;
    ImageView ivReactionDetail, ivWeatherDetail;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;
    LinearLayout Diary_color_detail;

    ///
    String colorpick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_diary);
        inIt();
        getData();
        setData();
    }

    public void inIt(){
        tvContentDetail = findViewById(R.id.tvContentDetail);
        tvTitleDetail = findViewById(R.id.tvTitleDetail);
        tvDateDetail = findViewById(R.id.tvDateDetail);
        ivReactionDetail = findViewById(R.id.ivReactionDetail);
        ivWeatherDetail = findViewById(R.id.ivWeatherDetail);
        Diary_color_detail = findViewById(R.id.Diary_color_detail);
    }

    public void getData(){
        Bundle bundle = getIntent().getExtras();
        if (bundle==null)
            return;
        d = (Diary) bundle.getSerializable("object_diary");
    }

    public void setData(){
        String[] month = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String[] date = d.getDate().split("/");
        Spannable spn = new SpannableString(date[0] + " " + month[Integer.parseInt(date[1]) - 1] + ", " + date[2]);
        spn.setSpan(new RelativeSizeSpan(2f), 0,2, 0);
        spn.setSpan(new ForegroundColorSpan(Color.RED), 0, 2, 0);
        spn.setSpan(new StyleSpan(Typeface.BOLD), 0, 2, 0);
        tvDateDetail.setText(spn);

        tvContentDetail.setText(d.getContent());
        tvTitleDetail.setText(d.getTitle());

        switch (d.getReaction()){
            case "angry":
                ivReactionDetail.setImageResource(R.drawable.angry);
                break;
            case "happy":
                ivReactionDetail.setImageResource(R.drawable.happy);
                break;
            case "sad":
                ivReactionDetail.setImageResource(R.drawable.sad);
                break;
            case "surprise":
                ivReactionDetail.setImageResource(R.drawable.surprise);
                break;
            case "tired":
                ivReactionDetail.setImageResource(R.drawable.tired);
                break;
            case "veryhappy":
                ivReactionDetail.setImageResource(R.drawable.veryhappy);
                break;
            case "worry":
                ivReactionDetail.setImageResource(R.drawable.worry);
                break;
            default:
                ivReactionDetail.setImageResource(R.drawable.happy);
        }

        switch (d.getWeather()) {
            case "cloud":
                ivWeatherDetail.setImageResource(R.drawable.ic_baseline_cloud_24);
                break;
            case "rain":
                ivWeatherDetail.setImageResource(R.drawable.rain);
                break;
            case "sunny":
                ivWeatherDetail.setImageResource(R.drawable.ic_baseline_wb_sunny_24);
                break;
            default:
                ivWeatherDetail.setImageResource(R.drawable.ic_baseline_wb_sunny_24);
        }

        switch (d.getColor()){
            case "#FFFFFFFF":
                Diary_color_detail.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                break;
            case "#F3E6E3":
                Diary_color_detail.setBackgroundColor(Color.parseColor("#F3E6E3"));
                break;
            case "#EFBBCF":
                Diary_color_detail.setBackgroundColor(Color.parseColor("#EFBBCF"));
                break;
            case "#D6B0B1":
                Diary_color_detail.setBackgroundColor(Color.parseColor("#D6B0B1"));
                break;
            case "#CCF6C8":
                Diary_color_detail.setBackgroundColor(Color.parseColor("#CCF6C8"));
                break;
            case "#FFE0AC":
                Diary_color_detail.setBackgroundColor(Color.parseColor("#FFE0AC"));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_diary, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_edit){
            finish();
            onClickEdit(d);
        }
        if (item.getItemId() == R.id.menu_delete){
            onClickDelete(d);
        }
        if (item.getItemId() == R.id.menu_close){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void onClickEdit(Diary d){
        Intent i =new Intent(this, AddDiary.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_diary", d);
        i.putExtras(bundle);
        startActivity(i);
    }

    private void onClickDelete(Diary d) {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure...?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        _myRef = mDatabase.getReference("Diary");
                        _myRef.child(d.getId()).removeValue();
                        finish();
                    }
                })
                .setNegativeButton("Cancel",null)
                .show();
    }
}