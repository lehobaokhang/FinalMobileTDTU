package lehobaokhang.tdtu.finalterm.adapter;

import android.content.Context;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lehobaokhang.tdtu.finalterm.R;
import lehobaokhang.tdtu.finalterm.activity.DetailDiary;
import lehobaokhang.tdtu.finalterm.model.Diary;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Diary> diaryList;
    boolean isEnable = false;
    boolean isSelectedAll = false;
    private ArrayList<Diary> selectedList = new ArrayList<>();


    public DiaryAdapter(@NonNull Context context, ArrayList<Diary> diaryList) {
        this.diaryList = diaryList;
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvDate, tvShortContent;
        ImageView ivReaction, ivWeather;
        CardView cvDiary;
        LinearLayout card_background;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tvDate);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvShortContent = itemView.findViewById(R.id.tvShortContent);
            ivReaction = itemView.findViewById(R.id.ivReaction);
            ivWeather = itemView.findViewById(R.id.ivWeather);
            cvDiary = itemView.findViewById(R.id.cvDiary);
            card_background = itemView.findViewById(R.id.card_background_element);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Diary d = diaryList.get(position);
        int index = position;

        String[] month = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String[] date = d.getDate().split("/");
        Spannable spn = new SpannableString(date[0] + " " + month[Integer.parseInt(date[1]) - 1] + ", " + date[2]);
        spn.setSpan(new RelativeSizeSpan(2f), 0,2, 0);
        spn.setSpan(new ForegroundColorSpan(Color.RED), 0, 2, 0);
        spn.setSpan(new StyleSpan(Typeface.BOLD), 0, 2, 0);
        holder.tvDate.setText(spn);

        String[] content = d.getContent().split(" ");
        String shortContent = new String();
        for (int i = 0;i<Math.min(5, content.length);i++){
            shortContent += content[i];
        }
        holder.tvShortContent.setText(shortContent);
        holder.tvTitle.setText(d.getTitle());

        switch (d.getReaction()){
            case "angry":
                holder.ivReaction.setImageResource(R.drawable.angry);
                break;
            case "happy":
                holder.ivReaction.setImageResource(R.drawable.happy);
                break;
            case "sad":
                holder.ivReaction.setImageResource(R.drawable.sad);
                break;
            case "surprise":
                holder.ivReaction.setImageResource(R.drawable.surprise);
                break;
            case "tired":
                holder.ivReaction.setImageResource(R.drawable.tired);
                break;
            case "veryhappy":
                holder.ivReaction.setImageResource(R.drawable.veryhappy);
                break;
            case "worry":
                holder.ivReaction.setImageResource(R.drawable.worry);
                break;
            default:
                holder.ivReaction.setImageResource(R.drawable.happy);
        }

        switch (d.getWeather()) {
            case "cloud":
                holder.ivWeather.setImageResource(R.drawable.ic_baseline_cloud_24);
                break;
            case "rain":
                holder.ivWeather.setImageResource(R.drawable.rain);
                break;
            case "sunny":
                holder.ivWeather.setImageResource(R.drawable.ic_baseline_wb_sunny_24);
                break;
            default:
                holder.ivWeather.setImageResource(R.drawable.ic_baseline_wb_sunny_24);
        }

        switch (d.getColor()){
            case "#FFFFFFFF":
                holder.card_background.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                break;
            case "#F3E6E3":
                holder.card_background.setBackgroundColor(Color.parseColor("#F3E6E3"));
                break;
            case "#EFBBCF":
                holder.card_background.setBackgroundColor(Color.parseColor("#EFBBCF"));
                break;
            case "#D6B0B1":
                holder.card_background.setBackgroundColor(Color.parseColor("#D6B0B1"));
                break;
            case "#CCF6C8":
                holder.card_background.setBackgroundColor(Color.parseColor("#CCF6C8"));
                break;
            case "#FFE0AC":
                holder.card_background.setBackgroundColor(Color.parseColor("#FFE0AC"));
                break;
        }

        holder.cvDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDetail(d);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (!diaryList.isEmpty())
            return diaryList.size();
        else
            return 0;
    }

    private void onClickDetail(Diary d){
        Intent i = new Intent(mContext, DetailDiary.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("object_diary", d);
        i.putExtras(bundle);
        mContext.startActivity(i);
    }
}
