package com.xiaobai.dropmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.xiaobai.library.DropMenu;
import com.xiaobai.library.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button =  findViewById(R.id.btn_show);
        button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               show();
           }
       });
    }

    private void show() {
        DropMenu.Builder builder = new DropMenu.Builder();
        final List<MenuItem> menuItemList = new ArrayList<>();
        menuItemList.add(new MenuItem("android"));
        menuItemList.add(new MenuItem("java"));
        menuItemList.add(new MenuItem("c++"));
        menuItemList.add(new MenuItem("swift"));
        menuItemList.add(new MenuItem("php"));
        builder.context(this)
                .anchorView(button)
                .menuItems(menuItemList)
                .offsetx(0)
                .showIcon(false)
                .onMenuItemClickListener(new DropMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        Toast.makeText(MainActivity.this, menuItemList.get(position).getText().length(), Toast.LENGTH_SHORT).show();
                    }
                }).createNoOffset();
    }
}
