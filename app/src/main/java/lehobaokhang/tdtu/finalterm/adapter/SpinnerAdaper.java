package lehobaokhang.tdtu.finalterm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import lehobaokhang.tdtu.finalterm.R;

public class SpinnerAdaper extends BaseAdapter {
    Context mContext;
    int[] img;

    public SpinnerAdaper(Context mContext, int[] img){
        this.mContext = mContext;
        this.img = img;
    }

    @Override
    public int getCount() {
        return img.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.spinner_item, null);
        ImageView iv_add = view.findViewById(R.id.iv_add);
        iv_add.setImageResource(img[i]);
        return view;
    }
}
