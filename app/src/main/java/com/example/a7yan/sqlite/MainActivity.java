package com.example.a7yan.sqlite;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    private EditText name,nickname;
    private ListView lv;
    private List<String> list;
    private ArrayAdapter<String> adapter;
    private SimpleCursorAdapter scadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (EditText) findViewById(R.id.name);
        name.setText("暂无数据");
        nickname = (EditText) findViewById(R.id.nickname);
        lv = (ListView) findViewById(R.id.lv);
        scadapter =new SimpleCursorAdapter(this,
                R.layout.list_user,
                null,
                new String[]{"name","nickname"},
                new int[]{R.id.tv_name,R.id.tv_nickname},
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
                );
        lv.setAdapter(scadapter);


        DBHelper helper = new DBHelper(this);
        //需要调用以下两个方法之一，才能创建数据库和表出来
        //正常情况下：getReadableDatabase 和 getWritableDatabase得到的结果一样
        //非正常情况,比如明确要求只读方式或者磁盘满了，用getReadableDatabase得到的是只读数据库
        //SQLiteDatabase readdb=helper.getReadableDatabase();
        db=helper.getWritableDatabase();
        //初始化listview
        //LoadData();
    }

    private void LoadData()
    {
        /*TextView temp_tv=new TextView(this);
        temp_tv.setText("暂无数据");
        lv.setEmptyView(name);*/

        list = new ArrayList<String>();
        //循环默认数据源

       /* for(int i=0;i<10;i++){
            list.add("Item"+i);
        }*/

        //读取SQLi特数据库

        Cursor cursor=db.query("person",null,"",new  String[]{},null,null,null);
        while (cursor.moveToNext()){
            int columnIndex =cursor.getColumnIndex("name");
            String name=cursor.getString(columnIndex);
            String nickname=cursor.getString(cursor.getColumnIndex("nickname"));
            list.add(name);
        }

        adapter =new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,list);

        lv.setAdapter(adapter);
    }

    public  void  insert(View view)
    {
        //String sql ="insert into person(name,nickname) values('7Yan','努力')";
        //db.execSQL(sql);
        String namev=name.getText().toString().trim();
        String nicknamev=nickname.getText().toString().trim();

        if(TextUtils.isEmpty(namev) || TextUtils.isEmpty(nicknamev))
        {
            Toast.makeText(this, "添加数据前请把名称和昵称填完.", Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues values=new ContentValues();
        values.put("name",namev);
        values.put("nickname",nicknamev);
        //返回值（行号）
        long lineid=db.insert("person",null,values);
        if(lineid > 0){
            list.add(namev);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "插入成功", Toast.LENGTH_SHORT).show();
        }
    }
    public  void  query(View view)
    {
        /*String nicknamev=nickname.getText().toString().trim();
        if(TextUtils.isEmpty(nicknamev))
        {
            Toast.makeText(this, "添加数据前请把名称和昵称填完.", Toast.LENGTH_SHORT).show();
            return;
        }
        Cursor cursor=db.query("person",null,"nickname=?",new  String[]{nicknamev},null,null,null);
        int i=0;
        while (cursor.moveToNext()){
            i++;
            int columnIndex =cursor.getColumnIndex("name");
            String name=cursor.getString(columnIndex);
            String nickname=cursor.getString(cursor.getColumnIndex("nickname"));
            Toast.makeText(this, "name=" + name + ", nickname=" + nickname, Toast.LENGTH_SHORT).show();
        }
        if(i==0){
            Toast.makeText(this, "数据库没有数据", Toast.LENGTH_SHORT).show();
        }*/

        Cursor cursor=db.query("person",null,"",new  String[]{},null,null,null);
        scadapter.swapCursor(cursor);
        scadapter.notifyDataSetChanged();
    }
    public  void  update(View view)
    {
        // update person name="辉" where nickname="努力"
        String namev=name.getText().toString().trim();
        String nicknamev=nickname.getText().toString().trim();

        if(TextUtils.isEmpty(namev) || TextUtils.isEmpty(nicknamev))
        {
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "添加数据前请把名称和昵称填完.", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues contentValues=new ContentValues();
        contentValues.put("name",namev);
        int lineid=db.update("person",contentValues,"nickname=?",new String[]{nicknamev});
        if(lineid>0){
            Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
        }

    }
    public  void  delete(View view)
    {
        //delete person where nickname="努力"
        String nicknamev=nickname.getText().toString().trim();
        if(TextUtils.isEmpty(nicknamev))
        {
            Toast.makeText(this, "添加数据前请把名称和昵称填完.", Toast.LENGTH_SHORT).show();
            return;
        }
        int lineid=db.delete("person","nickname=?",new String[]{nicknamev});
        if(lineid>0){
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
        }
    }
}
