package com.passer.ui.biao;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.passer.R;

import java.util.ArrayList;

public class RoutePickActivity extends AppCompatActivity implements MyAdapter.OnImageButtonClickListener {
    private static final int REQUEST_SPOT_MESSAGE = 5;
    private static final int REQUEST_SPOT_MESSAGE_START = 0;
    private static final int REQUEST_SPOT_MESSAGE_DESTINATION = 1;
    public static final String EXTRA_SPOT = "extra_spot";

    private MapView mMapView = null;
    private AMap mAMap;
    private ArrayList<LatLng> mLatLngs = new ArrayList<>();
    private MyAdapter mMyAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        setupAMap();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new MyDivider(this));
        mMyAdapter = new MyAdapter(this);
        mMyAdapter.setOnImageButtonClickListener(this);
        mRecyclerView.setAdapter(mMyAdapter);
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
                Spot removeSpot = mMyAdapter.removeSpot(fromPosition);
                mMyAdapter.addSpot(toPosition, removeSpot);
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
                mMyAdapter.removeSpot(position);
                mMyAdapter.notifyItemRemoved(position);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setupAMap() {
        //初始化地图控制器对象
        mAMap = mMapView.getMap();
        mAMap.setMyLocationStyle(getLocationStyle());//设置定位蓝点的Style
        // mAMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        mAMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mAMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                addMarkerToMap(latLng);
            }

        });
    }

    private MyLocationStyle getLocationStyle() {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        //设置定位蓝点的icon图标方法，需要用到BitmapDescriptor类对象作为参数。
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);// 只定位一次
//        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        return myLocationStyle;
    }

    private void addMarkerToMap(LatLng latLng) {
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.title("new marker")
                .position(latLng)
                .snippet("latLng：" + "(" + latLng.latitude + "," + latLng.longitude + ")")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                .draggable(true);
        mAMap.addMarker(markerOption);
        mLatLngs.add(latLng);
        mAMap.addPolyline(new PolylineOptions()
                .addAll(mLatLngs).width(10).color(Color.argb(255, 1, 1, 1)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        Spot spot = (Spot) data.getSerializableExtra(EXTRA_SPOT);
        if (requestCode == REQUEST_SPOT_MESSAGE) {
            mMyAdapter.addSpot(spot);
        } else if (requestCode == REQUEST_SPOT_MESSAGE_START) {
            mMyAdapter.removeSpot(0);
            mMyAdapter.addSpot(0, spot);
        } else if (requestCode == REQUEST_SPOT_MESSAGE_DESTINATION) {
            mMyAdapter.addSpot(mRecyclerView.getChildCount() - 1, spot);
            mMyAdapter.removeSpot(mRecyclerView.getChildCount() - 1);
        }
        mMyAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onImageButtonClick(int position) {
        int requestCode = REQUEST_SPOT_MESSAGE;
        if (position == 0) {
            requestCode = REQUEST_SPOT_MESSAGE_START;
        } else if (position == mRecyclerView.getChildCount() - 1) {
            requestCode = REQUEST_SPOT_MESSAGE_DESTINATION;
        }
        Intent intent = RoutePickDetailActivity.newIntent(RoutePickActivity.this);
        startActivityForResult(intent, requestCode);
    }
}
