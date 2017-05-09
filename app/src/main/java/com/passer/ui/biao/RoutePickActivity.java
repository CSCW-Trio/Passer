package com.passer.ui.biao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.passer.PasserActivity;
import com.passer.R;
import com.passer.bean.SpotBean;
import com.passer.ui.young.AccessActivity;

import java.util.List;

public class RoutePickActivity extends AppCompatActivity
        implements MyAdapter.OnImageButtonClickListener, MyAdapter.OnTextViewClickListener {
    private static final int REQUEST_SpotBean_MESSAGE = 5;
    private static final int REQUEST_SpotBean_MESSAGE_START = 0;
    private static final int REQUEST_SpotBean_MESSAGE_DESTINATION = 1;
    private static final int REQUEST_SUGGESTION = 2;
    public static final String EXTRA_SPOT = "extra_SpotBean";
    public static final String EXTRA_SUGGESTION = "EXTRA_SUGGESTION";

    private static final String TAG = "biao";

    private MyMapFragment mMapFragment = null;
    private MyAdapter mMyAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        //获取地图控件引用
        mMapFragment = (MyMapFragment) getFragmentManager().findFragmentById(R.id.map);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new MyDivider(this));
        mMyAdapter = new MyAdapter(this);
        mMyAdapter.setOnImageButtonClickListener(this);
        mMyAdapter.setOnTextViewClickListener(this);
        mRecyclerView.setAdapter(mMyAdapter);
        mMyAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mMapFragment.invalidateUI();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                mMapFragment.invalidateUI();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                super.onItemRangeChanged(positionStart, itemCount, payload);
                mMapFragment.invalidateUI();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                mMapFragment.invalidateUI();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                mMapFragment.invalidateUI();
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                mMapFragment.invalidateUI();
            }
        });
        addItemTouchEvent(mRecyclerView);

    }

    private void addItemTouchEvent(RecyclerView recyclerView) {

        ItemTouchHelper.Callback callback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
                int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
                SpotBean removeSpotBean = mMyAdapter.removeSpotBean(fromPosition);
                mMyAdapter.addSpotBean(toPosition, removeSpotBean);
                mMyAdapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (position == 0 || position == RoutePickActivity.this.mRecyclerView.getChildCount() - 1) {
                    mMyAdapter.notifyDataSetChanged();
                    return;
                }
                mMyAdapter.removeSpotBean(position);
                mMyAdapter.notifyItemRemoved(position);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        SpotBean SpotBean = (SpotBean) data.getSerializableExtra(EXTRA_SPOT);
        if (requestCode == REQUEST_SpotBean_MESSAGE) {
            mMyAdapter.addSpotBean(SpotBean);
        } else if (requestCode == REQUEST_SpotBean_MESSAGE_START) {
            mMyAdapter.removeSpotBean(0);
            mMyAdapter.addSpotBean(0, SpotBean);
        } else if (requestCode == REQUEST_SpotBean_MESSAGE_DESTINATION) {
            mMyAdapter.removeSpotBean(mRecyclerView.getChildCount() - 1);
            mMyAdapter.addSpotBean(mRecyclerView.getChildCount() - 1, SpotBean);
        } else if (requestCode == REQUEST_SUGGESTION) {
            List<SpotBean> spotBeen =
                    (List<SpotBean>) data.getSerializableExtra(EXTRA_SUGGESTION);
            mMyAdapter.clearAllSpotBeans();
            mMyAdapter.addAllSpotBeans(spotBeen);
        }
        mMyAdapter.notifyDataSetChanged();

        mMapFragment.setSpots(mMyAdapter.getSpotBeans());
        mMapFragment.invalidateUI();
    }

    @Override
    public void onImageButtonClick(int position) {
        int requestCode = REQUEST_SpotBean_MESSAGE;
        if (position == 0) {
            requestCode = REQUEST_SpotBean_MESSAGE_START;
        } else if (position == mRecyclerView.getChildCount() - 1) {
            requestCode = REQUEST_SpotBean_MESSAGE_DESTINATION;
        }
        Intent intent = RoutePickDetailActivity.newIntent(RoutePickActivity.this);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onTextViewClick(View v, int position) {
        Toast.makeText(this, ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
    }

    public void askSuggestRoute(View view) {
        Intent intent = new Intent(this, AccessActivity.class);
        startActivityForResult(intent, REQUEST_SUGGESTION);

    }

    public void sureWithRoute(View view) {
        Intent intent = getIntent();
        intent.putExtra(PasserActivity.EXTRA_SPOTS, mMyAdapter.getSpotBeans());
        setResult(RESULT_OK, intent);
        this.finish();
    }

}
