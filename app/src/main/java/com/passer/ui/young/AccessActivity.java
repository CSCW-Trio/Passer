package com.passer.ui.young;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.passer.R;
import com.passer.adapter.AccessAdapter;
import com.passer.ui.base.BaseActivity;


public class AccessActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_access;
    }

    @Override
    public void initViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_main);
        String[] strings = new String[]{"风景", "美食", "娱乐", "酒店"};
        String[] str_my_roads = new String[]{
                "1广东工业大学--南亭村--中大金逸电影院--高高商业中心",
                "2华南理工大学--北亭村贡茶--广大商业中心",
                "3小洲村早茶店--余萌山房--高高金逸电影院",
                "4广东工业大学--小洲村早茶店--余萌山房--高高商业中心",
                "5小洲村早茶店--余萌山房--金逸电影院--gogo商业中心",
                "6中山大学--南亭村--金逸电影院--余萌山房"};
        int[] int_imgs = new int[]{
                R.drawable.img1,
                R.drawable.img2,
                R.drawable.img3,
                R.drawable.img4,
                R.drawable.img5,
                R.drawable.img6,
                R.drawable.img7,
                R.drawable.img8,
                R.drawable.img9,
                R.drawable.img10
        };
        String[] str_names = new String[]{
                "长隆欢乐世界特惠",
                "神木文化园",
                "同程驿站岭南印象园店",
                "广州长隆国际大马戏",
                "番禹长兴乐园",
                "广东科学中心印象冰雕",
                "番禺百万葵园",
                "海傍水乡",
                "全国3D魔幻艺术展番禺站",
                "南粤苑"
        };
        String[] str_addr = new String[]{
                "广东省广州市番禺区迎宾路",
                "广东省广州市番禺区市南路桥南街草河村（德怡大桥旁）",
                "岭南印象园南风窗咖啡馆处",
                "广东省广州市番禺区迎宾路长隆欢乐世界东门侧",
                "广州番禺区南村镇兴业大道旁",
                "广州番禺区大学城广东科学中心",
                "广东省广州市近郊南沙区万顷沙镇新垦15涌",
                "番禺区亚运大道亚运村西侧",
                "广东省广州市番禺区市桥街富华中路56号儿童新天地二楼",
                "广东省广州市番禺沙湾镇紫坭村大川岗"

        };

        int[] int_prices = new int[]{
                100,
                50,
                15,
                45,
                55,
                10,
                10,
                25,
                88,
                150
        };
        int[] images = new int[]{R.drawable.image_feature, R.drawable.image_food,
                R.drawable.image_amuse, R.drawable.image_hotel};
        AccessAdapter adapter = new AccessAdapter(this, strings, str_my_roads, images,int_imgs,str_names,
                str_addr,int_prices);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 8, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setupView(Bundle bundle) {

    }
}
