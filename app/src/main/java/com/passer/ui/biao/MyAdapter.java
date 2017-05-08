package com.passer.ui.biao;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.passer.R;
import com.passer.bean.SpotBean;

import java.util.ArrayList;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
    private ArrayList<SpotBean> mSpots = new ArrayList<>();
    private Context mContext;
    private OnImageButtonClickListener onImageButtonClickListener;

    interface OnImageButtonClickListener {
        void onImageButtonClick(int position);
    }

    MyAdapter(Context context) {
        SpotBean spot0 = new SpotBean("按此输入起点", -1, -1, "");
        SpotBean spot1 = new SpotBean("按此添加路径点", -1, -1, "");
        SpotBean spot2 = new SpotBean("按此添加目的地", -1, -1, "");
        mSpots.add(spot0);
        mSpots.add(spot1);
        mSpots.add(spot2);
        mContext = context;
    }

    void setOnImageButtonClickListener(OnImageButtonClickListener onImageButtonClickListener) {
        this.onImageButtonClickListener = onImageButtonClickListener;
    }

    void addSpot(SpotBean spot) {
        mSpots.add(mSpots.size() - 2, spot);
        notifyItemInserted(mSpots.size() - 2);
        notifyItemRangeChanged(mSpots.size() - 3, 3);
    }

    void addSpot(int position, SpotBean spot) {
        mSpots.add(position, spot);
    }

    SpotBean removeSpot(int position) {
        return mSpots.remove(position);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_add_spot, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.bindHolder(mSpots.get(position));
    }

    @Override
    public int getItemCount() {
        return mSpots.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        private ImageButton mImageButton;
        private SpotBean mSpot;

        MyHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text_view);
            mImageButton = (ImageButton) itemView.findViewById(R.id.image_button);
            mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageButtonClickListener != null) {
                        onImageButtonClickListener.onImageButtonClick(getAdapterPosition());
                    }
                }
            });
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, mTextView.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        void bindHolder(final SpotBean spot) {
            mTextView.setText(spot.getName());
        }
    }
}
