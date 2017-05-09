package com.passer.ui.biao;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapFragment;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.NaviPara;
import com.amap.api.maps.model.PolylineOptions;
import com.passer.R;
import com.passer.bean.SpotBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyMapFragment extends MapFragment
        implements Serializable, AMap.InfoWindowAdapter {
    private AMap mAMap;
    private ArrayList<SpotBean> mSpotBeans = new ArrayList<>();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setupAMap();
        setupAMapUI(true);
    }

    public void setSpots(ArrayList<SpotBean> mSpotBeans) {
        this.mSpotBeans = mSpotBeans;
    }

    public void invalidateUI() {
        mAMap.clear();
        drawMarkers(mSpotBeans, mAMap);
        drawPolyline(mSpotBeans, mAMap);
        adjustCamera(mSpotBeans, mAMap);
    }

    private void setupAMap() {
        // 初始化地图控制器对象
        mAMap = getMap();
        mAMap.setMyLocationStyle(getLocationStyle());//设置定位蓝点的Style
        // mAMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        mAMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mAMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });
        mAMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
    }

    public void setupAMapUI(boolean flag) {
        if (!flag) return;
        UiSettings mUiSettings = mAMap.getUiSettings();
        mUiSettings.setCompassEnabled(false);
        mUiSettings.setMyLocationButtonEnabled(true); //显示默认的定位按钮
        mUiSettings.setScaleControlsEnabled(false);//控制比例尺控件是否显示
        mUiSettings.setZoomControlsEnabled(false);
    }

    private MyLocationStyle getLocationStyle() {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        //设置定位蓝点的icon图标方法，需要用到BitmapDescriptor类对象作为参数。
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.place));
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);// 只定位一次
//        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        return myLocationStyle;
    }

    private void drawPolyline(List<SpotBean> SpotBeans, AMap aMap) {
        List<LatLng> latLngs = new ArrayList<>();
        for (int i = 0; i < SpotBeans.size(); i++) {
            latLngs.add(SpotBeans.get(i).getLatLng());
        }
        aMap.addPolyline(new PolylineOptions()
                .addAll(latLngs).width(10)
                .setDottedLine(true)//设置虚线
                .color(Color.RED));
    }

    private void drawMarkers(List<SpotBean> SpotBeans, AMap aMap) {
        SpotBean spotBean;
        for (int i = 0; i < SpotBeans.size(); i++) {
            spotBean = SpotBeans.get(i);
            MarkerOptions markerOption = new MarkerOptions();
            markerOption.title(spotBean.getName())
                    .position(spotBean.getLatLng())
                    .snippet(spotBean.getSnippet())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .draggable(true);
            aMap.addMarker(markerOption);
        }
    }

    private void adjustCamera(List<SpotBean> SpotBeans, AMap aMap) {
        zoomToSpan();
    }

    private void zoomToSpan() {
        //移动镜头到当前的视角。
        try {
            if (mSpotBeans != null && mSpotBeans.size() > 0) {
                if (mAMap == null)
                    return;
                if (mSpotBeans.size() == 1) {
                    mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mSpotBeans.get(0).getLatitude(),
                            mSpotBeans.get(0).getLongitude()), 18f));
                } else {
                    LatLngBounds bounds = getLatLngBounds();
                    mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private LatLngBounds getLatLngBounds() {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < mSpotBeans.size(); i++) {
            b.include(new LatLng(mSpotBeans.get(i).getLatitude(),
                    mSpotBeans.get(i).getLongitude()));
        }
        return b.build();
    }

    // 调起高德地图导航功能，如果没安装高德地图，会进入异常，可以在异常中处理，调起高德地图app的下载页面
    public void startAMapNavi(Marker marker) {
        // 构造导航参数
        NaviPara naviPara = new NaviPara();
        // 设置终点位置
        naviPara.setTargetPoint(marker.getPosition());
        // 设置导航策略，这里是避免拥堵
        naviPara.setNaviStyle(NaviPara.DRIVING_AVOID_CONGESTION);

        // 调起高德地图导航
        try {
            AMapUtils.openAMapNavi(naviPara, getActivity().getApplicationContext());
        } catch (com.amap.api.maps.AMapException e) {

            // 如果没安装会进入异常，调起下载页面
            AMapUtils.getLatestAMapApp(getActivity().getApplicationContext());

        }

    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(final Marker marker) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.item_info_window,
                null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(marker.getTitle());

        TextView snippet = (TextView) view.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());
        ImageButton button = (ImageButton) view
                .findViewById(R.id.start_amap_app);
        // 调起高德地图app
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAMapNavi(marker);
            }
        });
        return view;
    }


}
