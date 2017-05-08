package com.passer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.passer.R;
import com.passer.bean.SpotBean;
import com.passer.ui.biao.RoutePickActivity;
import com.passer.ui.young.AccessActivity;
import com.passer.util.StaticValue;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by 47420 on 2017/4/16.
 */

public class AccessAdapter extends RecyclerView.Adapter {
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_ITEM = 2;
    public static final int TYPE_DIVIDER = 3;
    public static final int TYPE_FEATURE = 4;
    private Context mContext;
    private String[] mStrings;
    private String[] str_my_road, str_name, str_addr;
    private int[] mImages;
    private int[] int_price;
    private int[] int_img;
    public int int_position;//记录在需要展示的数据之前有多少条数据

    public AccessAdapter(Context context, String[] strings, String[] str_my_roads, int[] ints,
                         int[] int_imgs, String[] str_test, String[] str_road, int[] prices) {
        mContext = context;
        mStrings = strings;
        str_my_road = str_my_roads;
        mImages = ints;
        int_img = int_imgs;
        int_price = prices;
        str_name = str_test;
        str_addr = str_road;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                View headView = View.inflate(parent.getContext(), R.layout.item_header, null);
                return new HeaderHolder(headView);
            case TYPE_ITEM:
                View itemView = View.inflate(parent.getContext(), R.layout.item_item, null);
                return new ItemHolder(itemView);
            case TYPE_DIVIDER:
                View dividerView = View.inflate(parent.getContext(), R.layout.item_divider, null);
                return new DividerHolder(dividerView);
            case TYPE_FEATURE:
                View featureView = View.inflate(parent.getContext(), R.layout.item_feature, null);
                return new FeatureHolder(featureView);
            default:
                return null;
        }
    }

    /**
     * 不同的布局绑定不同的数据，位置
     *
     * @param holder   布局控件的绑定
     * @param position 数据的位置
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderHolder) {
            bindHeaderHoder((HeaderHolder) holder, position);
        } else if (holder instanceof ItemHolder) {
            bindItemHolder(((ItemHolder) holder), position);
        } else if (holder instanceof DividerHolder) {
            bindDividerHolder((DividerHolder) holder, position);
        } else if (holder instanceof FeatureHolder) {
            bindFeatureHolder((FeatureHolder) holder, position);
        }
    }

    private void bindItemHolder(ItemHolder holder, int position) {
        holder.tv_item_item2.setText(str_my_road[position - 5]);
        holder.tv_item_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpotBean spotBean;
                ArrayList<SpotBean> list_spotbean = new ArrayList<SpotBean>();
                spotBean = new SpotBean("广东工业大学", 23.0333740, 113.3972800, "广东工业大学");
                list_spotbean.add(spotBean);
                spotBean = new SpotBean("小洲村早茶店", 23.0591000, 113.3583280, "小洲村早茶店");
                list_spotbean.add(spotBean);
                spotBean = new SpotBean("雨萌山房", 23.0118340, 113.3955580, "雨萌山房");
                list_spotbean.add(spotBean);
                spotBean = new SpotBean("高高新天地", 23.010280, 113.3920240, "高高新天地");
                list_spotbean.add(spotBean);
                StaticValue.setList_spotBean(list_spotbean);

                //跳转
                if (mContext instanceof AccessActivity) {
                    Toast.makeText(mContext, "跳转", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, RoutePickActivity.class);
                    intent.putExtra(RoutePickActivity.EXTRA_SUGGESTION, StaticValue.getList_spotBean());
                    ((AccessActivity) mContext).setResult(Activity.RESULT_OK, intent);
                    ((AccessActivity) mContext).finish();
                }
            }
        });
    }

    private void bindHeaderHoder(HeaderHolder holder, final int position) {
        holder.tv_item_header.setText(mStrings[position]);
        holder.img_item_header.setImageResource(mImages[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mStrings[position], Toast.LENGTH_SHORT).show();
            }
        });
        Log.i(TAG, "bindHeaderHoder: " + position);
    }

    private void bindDividerHolder(DividerHolder holder, int position) {
        if (position == 4) {
            holder.tv_item_divider.setText("热门路线");
        } else {
            holder.tv_item_divider.setText("附近景点推荐");
        }
    }

    private void bindFeatureHolder(FeatureHolder holder, int positon) {
        int i = positon - 12;
        Log.i(TAG, "bindFeatureHolder: " + i);
        holder.img_item.setImageResource(int_img[i]);
        holder.tv_name.setText(str_name[i]);
        holder.tv_addr.setText(str_addr[i]);
        holder.tv_price.setText("" + int_price[i] + ".00元");
    }

    @Override
    public int getItemCount() {
        return mStrings.length + str_my_road.length + 2 + 10;
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {
        private TextView tv_item_header;
        private ImageView img_item_header;

        public HeaderHolder(View headView) {
            super(headView);
            tv_item_header = (TextView) headView.findViewById(R.id.tv_item_header);
            img_item_header = (ImageView) headView.findViewById(R.id.img_item_header);
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        private TextView tv_item_item2;

        public ItemHolder(View itemView) {
            super(itemView);
            tv_item_item2 = (TextView) itemView.findViewById(R.id.tv_item_item2);
        }
    }

    private class FeatureHolder extends RecyclerView.ViewHolder {
        private ImageView img_item;
        private TextView tv_name;
        private TextView tv_addr;
        private TextView tv_price;

        public FeatureHolder(View itemView) {
            super(itemView);
            img_item = (ImageView) itemView.findViewById(R.id.image);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_addr = (TextView) itemView.findViewById(R.id.tv_addr);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
        }
    }

    private class DividerHolder extends RecyclerView.ViewHolder {
        private TextView tv_item_divider;

        public DividerHolder(View itemView) {
            super(itemView);
            tv_item_divider = (TextView) itemView.findViewById(R.id.tv_item_divider);
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    switch (type) {
                        case TYPE_HEADER:
                            return 2;
                        case TYPE_ITEM:
                            return 8;
                        case TYPE_DIVIDER:
                            return 8;
                        case TYPE_FEATURE:
                            return 8;
                        default:
                            Log.i(TAG, "getSpanSize: default" + position);
                            return 1;
                    }
                }
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position <= 3) {
            return TYPE_HEADER;
        } else if (position == 4) {
            return TYPE_DIVIDER;
        } else if (position <= 10 && position > 4) {
            return TYPE_ITEM;
        } else if (position == 11) {
            return TYPE_DIVIDER;
        } else {
            return TYPE_FEATURE;
        }
    }


}
