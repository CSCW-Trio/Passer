package com.passer.ui.biao;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.passer.R;
import com.passer.bean.SpotBean;
import com.passer.util.DialogUtil;

import java.util.ArrayList;

public class RoutePickDetailActivity extends AppCompatActivity
        implements PoiSearch.OnPoiSearchListener, AMap.OnMarkerClickListener {
    private MapView mMapView;
    private Button mSureButton;
    private AMap mAMap;
    private Marker mSelectedMarker;
    private ImageView mSearchBackImageView, mSearchClearImageView;
    private EditText mSearchEditText;
    private FloatingActionButton mFab;
    private FrameLayout mSearchFrame;
    private Dialog mDialog;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, RoutePickDetailActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_pick_detail);


        mSearchFrame = (FrameLayout) findViewById(R.id.frame_layout_empty);
        mSearchFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchFrame.setVisibility(View.GONE);
                mFab.show();
            }
        });

        setupSearchComponents();

        mFab = (FloatingActionButton) findViewById(R.id.floating_action_button);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchFrame.setVisibility(View.VISIBLE);
                mSearchEditText.requestFocus();
                mFab.hide();
            }
        });


        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        setupAMap();
        setupAMapUI();

        mSureButton = (Button) findViewById(R.id.sure_destination_button);
        mSureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mSelectedMarker.getTitle();
                double latitude = mSelectedMarker.getPosition().latitude;
                double longitude = mSelectedMarker.getPosition().longitude;
                String snippet = mSelectedMarker.getSnippet();
                SpotBean spot = new SpotBean(title, latitude, longitude, snippet);
                Intent data = new Intent();
                data.putExtra(RoutePickActivity.EXTRA_SPOT, spot);
                setResult(RESULT_OK, data);
                RoutePickDetailActivity.this.finish();
            }
        });


    }

    private void setupAMap() {
        //初始化地图控制器对象
        mAMap = mMapView.getMap();
        // mAMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        mAMap.setMyLocationStyle(getLocationStyle());
        mAMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mAMap.setOnMarkerClickListener(this);
    }

    private MyLocationStyle getLocationStyle() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);// 只定位一次
        //设置定位蓝点的icon图标方法，需要用到BitmapDescriptor类对象作为参数。
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
//        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        return myLocationStyle;
    }

    private void setupAMapUI() {
        UiSettings mUiSettings = mAMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);
        mUiSettings.setCompassEnabled(false);
        mUiSettings.setMyLocationButtonEnabled(true); //显示默认的定位按钮
        mUiSettings.setScaleControlsEnabled(true);//控制比例尺控件是否显示
    }


    private void setupSearchComponents() {

        mSearchBackImageView = (ImageView) findViewById(R.id.search_back);
        mSearchClearImageView = (ImageView) findViewById(R.id.search_text_clear);
        mSearchEditText = (EditText) findViewById(R.id.search_edit);
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.equals("")) mSearchClearImageView.setVisibility(View.GONE);
                else mSearchClearImageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSearchClearImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchEditText.setText("");
                mSearchClearImageView.setVisibility(View.GONE);
            }
        });
        mSearchBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchFrame.setVisibility(View.GONE);
            }
        });
        mSearchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); //得到InputMethodManager的实例
                if (imm.isActive()) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);//关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
                }

            }
        });
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                mSearchFrame.setVisibility(View.GONE);
                String keyWord = String.valueOf(mSearchEditText.getText());
                searchPoi(keyWord, "广州");
                mDialog = DialogUtil.createProgressDialog(RoutePickDetailActivity.this, "", "");
                mDialog.show();
                return true;
            }
        });

    }

    private void searchPoi(String keyWord, String region) {
        PoiSearch.Query query = new PoiSearch.Query(keyWord, "", region);
        // keyWord 表示搜索字符串，
        // 第二个参数表示 POI 搜索类型，二者选填其一，
        // POI 搜索类型共分为以下20种：汽车服务|汽车销售|
        // 汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
        // 住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
        // 金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
        // cityCode 表示 POI 搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.setPageSize(10);// 设置每页最多返回多少条 poiitem
        query.setPageNum(1);//设置查询页码

        PoiSearch poiSearch = new PoiSearch(RoutePickDetailActivity.this, query);
        poiSearch.setOnPoiSearchListener(RoutePickDetailActivity.this);
        poiSearch.searchPOIAsyn();
    }

    private void searchSurroundingPoi(Location location) {
        PoiSearch.Query query = new PoiSearch.Query("", "餐饮服务");
        query.setPageSize(10);// 设置每页最多返回多少条 poiitem
        query.setPageNum(1);//设置查询页码

        PoiSearch poiSearch = new PoiSearch(RoutePickDetailActivity.this, query);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(location.getLatitude(),
                location.getLongitude()), 1000));//设置周边搜索的中心点以及半径
        poiSearch.setOnPoiSearchListener(RoutePickDetailActivity.this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int resultCode) {
        if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
        if (resultCode == 1000) {
            ArrayList<PoiItem> poiItems = poiResult.getPois();
            if (poiItems != null && poiItems.size() > 0) {
                mAMap.clear();// 清理之前的图标
                PoiOverlay poiOverlay = new PoiOverlay(mAMap, poiItems);
                poiOverlay.removeFromMap();
                poiOverlay.addToMap();
                poiOverlay.zoomToSpan();
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

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
    public boolean onMarkerClick(Marker marker) {
        if (mSelectedMarker != null) mSelectedMarker.hideInfoWindow();
        mSelectedMarker = marker;
        mSelectedMarker.showInfoWindow();
        Toast.makeText(this, marker.getSnippet(), Toast.LENGTH_SHORT).show();
        mSureButton.setEnabled(true);
        return true;
    }

}
