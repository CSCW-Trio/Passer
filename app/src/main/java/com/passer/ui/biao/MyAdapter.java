package com.passer.ui.biao;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.passer.R;
import com.passer.bean.SpotBean;

import java.util.ArrayList;
import java.util.List;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
    private ArrayList<SpotBean> mSpotBeans = new ArrayList<>();
    private Context mContext;
    private OnImageButtonClickListener onImageButtonClickListener;
    private OnTextViewClickListener onTextViewClickListener;

    interface OnImageButtonClickListener {
        void onImageButtonClick(int position);
    }

    void setOnTextViewClickListener(OnTextViewClickListener onTextViewClickListener) {
        this.onTextViewClickListener = onTextViewClickListener;
    }

    interface OnTextViewClickListener {
        void onTextViewClick(View v, int position);
    }

    MyAdapter(Context context) {
        SpotBean SpotBeanStart = new SpotBean("按此输入起点", -1, -1, "");
        SpotBean SpotBeanAdd = new SpotBean("按此添加路径点", -1, -1, "");
        SpotBean SpotBeanEnd = new SpotBean("按此添加目的地", -1, -1, "");
        mSpotBeans.add(SpotBeanStart);
        mSpotBeans.add(SpotBeanAdd);
        mSpotBeans.add(SpotBeanEnd);
        mContext = context;
    }

    void setOnImageButtonClickListener(OnImageButtonClickListener onImageButtonClickListener) {
        this.onImageButtonClickListener = onImageButtonClickListener;
    }

    ArrayList<SpotBean> getSpotBeans() {
        ArrayList<SpotBean> SpotBeans = new ArrayList<>();
        for (int i = 0; i < mSpotBeans.size(); i++) {
            String temp = mSpotBeans.get(i).getName();
            if (!temp.equals("按此输入起点") &&
                    !temp.equals("按此添加路径点") &&
                    !temp.equals("按此添加目的地")) {
                SpotBeans.add(mSpotBeans.get(i));
            }
        }
        return SpotBeans;
    }

    void addSpotBean(SpotBean SpotBean) {
        mSpotBeans.add(mSpotBeans.size() - 2, SpotBean);
        notifyItemInserted(mSpotBeans.size() - 2);
        notifyItemRangeChanged(mSpotBeans.size() - 3, 3);
    }

    void addAllSpotBeans(List<SpotBean> SpotBeans) {
        mSpotBeans.addAll(SpotBeans);
    }

    void clearAllSpotBeans() {
        mSpotBeans.clear();
    }

    void addSpotBean(int position, SpotBean SpotBean) {
        mSpotBeans.add(position, SpotBean);
    }

    SpotBean removeSpotBean(int position) {
        return mSpotBeans.remove(position);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_add_spot, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.bindHolder(mSpotBeans.get(position));
    }

    @Override
    public int getItemCount() {
        return mSpotBeans.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        private ImageButton mImageButton;
        private SpotBean mSpotBean;

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
                    if (onTextViewClickListener != null) {
                        onTextViewClickListener.onTextViewClick(v,getAdapterPosition());
                    }
                }
            });
        }

        void bindHolder(final SpotBean SpotBean) {
            mTextView.setText(SpotBean.getName());
        }
    }
}
