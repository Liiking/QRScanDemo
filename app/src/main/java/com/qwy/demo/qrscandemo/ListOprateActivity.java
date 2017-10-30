package com.qwy.demo.qrscandemo;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListOprateActivity extends AppCompatActivity {

    private SlideAndDragListView listView;
    private List<String> list = new ArrayList<>();
    private int oldPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_oprate);

        for (int i = 0; i < 20; i++) {
            list.add("position" + i);
        }

        listView = (SlideAndDragListView) findViewById(R.id.listView);

        Menu menu = new Menu(true, true, 0);//the second parameter is whether can slide over
        menu.addItem(new MenuItem.Builder().setWidth(90)//set Width
                .setText("删除")//set text string
                .setTextColor(Color.RED)//set text color
                .setTextSize(20)//set text size
                .build());
        menu.addItem(new MenuItem.Builder().setWidth(120)
//                .setBackground(new ColorDrawable(Color.BLACK))
                .setText("编辑")//set text string
                .setTextColor(Color.BLUE)//set text color
                .setDirection(MenuItem.DIRECTION_RIGHT)//set direction (default DIRECTION_LEFT)
//                .setIcon(getResources().getDrawable(R.mipmap.ic_launcher))// set icon
                .build());
        menu.addItem(new MenuItem.Builder().setWidth(120)
//                .setBackground(new ColorDrawable(Color.BLACK))
                .setText("完成")//set text string
                .setTextColor(Color.GREEN)//set text color
                .setDirection(MenuItem.DIRECTION_RIGHT)//set direction (default DIRECTION_LEFT)
//                .setIcon(getResources().getDrawable(R.mipmap.ic_launcher))// set icon
                .build());
//set in sdlv
        listView.setMenu(menu);
        listView.setOnSlideListener(new SlideAndDragListView.OnSlideListener() {
            @Override
            public void onSlideOpen(View view, View parentView, int position, int direction) {

            }

            @Override
            public void onSlideClose(View view, View parentView, int position, int direction) {

            }
        });
        listView.setOnMenuItemClickListener(new SlideAndDragListView.OnMenuItemClickListener() {
            @Override
            public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
                switch (direction) {
                    case MenuItem.DIRECTION_LEFT:
                        switch (buttonPosition) {
                            case 0://One
                                if (itemPosition >= 0 && itemPosition < list.size()) {
                                    list.remove(itemPosition);
                                }
                                return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
                        }
                        break;
                    case MenuItem.DIRECTION_RIGHT:
                        switch (buttonPosition) {
                            case 0://icon
                                Toast.makeText(getApplicationContext(), "编辑" + itemPosition, Toast.LENGTH_SHORT).show();
                                return Menu.ITEM_SCROLL_BACK;
                            case 1:
                                return Menu.ITEM_SCROLL_BACK;
                        }
                        break;
                    default:
                        return Menu.ITEM_NOTHING;
                }
                return Menu.ITEM_NOTHING;
            }
        });
        listView.setOnDragListener(new SlideAndDragListView.OnDragListener() {
            @Override
            public void onDragViewStart(int position) {
                oldPos = position;
            }

            @Override
            public void onDragViewMoving(int position) {

            }

            @Override
            public void onDragViewDown(int position) {
                if (position != oldPos) {
                    if (position >= 0 && position < 20) {
                        Collections.swap(list, oldPos, position);
                        oldPos = -1;
                    }
                }
            }
        }, list);

        listView.setAdapter(new MyAdapter());
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.item_listview, null);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.tv);
            tv.setText(list.get(position));
            return convertView;
        }
    }

}
