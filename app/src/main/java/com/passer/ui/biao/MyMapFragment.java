package com.passer.ui.biao;

import android.graphics.Color;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapFragment;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.passer.R;
import com.passer.bean.SpotBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyMapFragment extends MapFragment
        implements Serializable {
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
    }

    public void setupAMapUI(boolean flag) {
        if (!flag) return;
        UiSettings mUiSettings = mAMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);
        mUiSettings.setCompassEnabled(false);
        mUiSettings.setMyLocationButtonEnabled(true); //显示默认的定位按钮
        mUiSettings.setScaleControlsEnabled(false);//控制比例尺控件是否显示
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
                .addAll(latLngs).width(10).color(Color.argb(255, 1, 1, 1)));
    }

    private void drawMarkers(List<SpotBean> SpotBeans, AMap aMap) {
        SpotBean spotBean;
        for (int i = 0; i < SpotBeans.size(); i++) {
            spotBean = SpotBeans.get(i);
            MarkerOptions markerOption = new MarkerOptions();
            markerOption.title(spotBean.getName())
                    .position(spotBean.getLatLng())
                    .snippet(spotBean.getSnippet())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                    .draggable(true);
            aMap.addMarker(markerOption);
        }
    }

    private void adjustCamera(List<SpotBean> SpotBeans, AMap aMap) {
        if (SpotBeans.size() >= 2) {
            LatLngBounds latlngBounds = new LatLngBounds(SpotBeans.get(0).getLatLng(), SpotBeans.get(1).getLatLng());
            for (int i = 2; i < SpotBeans.size(); i++) {
                latlngBounds = latlngBounds.including(SpotBeans.get(i).getLatLng());
            }
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, 10));

        } else if (SpotBeans.size() == 1) {
            CameraUpdate cameraUpdate =
                    CameraUpdateFactory.newCameraPosition(new CameraPosition(
                            SpotBeans.get(0).getLatLng(), 18, 30, 30));
            aMap.animateCamera(cameraUpdate);
        } else {
            aMap.reloadMap();
        }
    }
}
