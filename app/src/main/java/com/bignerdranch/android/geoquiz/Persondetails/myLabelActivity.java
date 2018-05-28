package com.bignerdranch.android.geoquiz.Persondetails;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bignerdranch.android.geoquiz.Adapter.LabelAdapter;
import com.bignerdranch.android.geoquiz.all_labelActivity;
import com.bignerdranch.android.geoquiz.Fragment.RecyclerItemClickListener;
import com.bignerdranch.android.geoquiz.The_details.thelabel_askActivity;
import com.bignerdranch.android.geoquiz.Fragment.FullyLinearLayoutManager;
import com.bignerdranch.android.geoquiz.Models.LabelBean;
import com.bignerdranch.android.geoquiz.R;
import com.bignerdranch.android.geoquiz.login1Activity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class myLabelActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    private RecyclerView mylabel_Recycler;
    private SwipeRefreshLayout mylabel_SwipeRefreshLayout;
    private LabelAdapter mylabel_Adapter;
    private List<LabelBean> mylabel_Datas;
    private Toolbar mylabel_toolbar;
    private SearchView mylabel_SearchView;
    FullyLinearLayoutManager mylabel_LayoutManager = new FullyLinearLayoutManager(this);

    private static final String url = "http://yuguole.pythonanywhere.com/Iknow/mylabel";


    public static String TheLabelStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_label);

        initView();
    }

    //初始化
    private void initView() {
        mylabel_Recycler = (RecyclerView) findViewById(R.id.recyclerview_mylabel);
        //如果item的内容不改变view布局大小，那使用这个设置可以提高RecyclerView的效率
        mylabel_Recycler.setHasFixedSize(true);
        mylabel_toolbar = (Toolbar) findViewById(R.id.toolbar_mylabel);
        mylabel_toolbar.setTitle("我关注的标签");//标题

        mylabel_toolbar.inflateMenu(R.menu.menu_addlabel);
        mylabel_toolbar.setOnMenuItemClickListener(this);
        //setSupportActionBar(myask_toolbar);

        //左边的小箭头（注意需要在setSupportActionBar(toolbar)之后才有效果）
        //myask_toolbar.setNavigationIcon(R.mipmap.ic_search);
        //myask_toolbar.inflateMenu(R.menu.menu_homepage);
        //myask_toolbar.setOnMenuItemClickListener((Toolbar.OnMenuItemClickListener) this);
        init();
        initListener();//刷新
        initClickitem();//点击事件
    }

    //点击item跳转
    private void initClickitem() {
        //点击跳转
        mylabel_Recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(myLabelActivity.this, mylabel_Recycler ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Toast.makeText(myLabelActivity.this, "点击", Toast.LENGTH_SHORT).show();
                        // do whatever
                        final TextView lbtitle=(TextView)view.findViewById(R.id.item_label_title);
                        TheLabelStr=lbtitle.getText().toString();
                        Toast.makeText(myLabelActivity.this, TheLabelStr, Toast.LENGTH_SHORT).show();
                        startLabel_askActivity();
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        Toast.makeText(myLabelActivity.this, "长按", Toast.LENGTH_SHORT).show();
                        // do whatever
                    }
                })
        );
    }

    //点击标签跳转到该标签下所有问题页面
    private void startLabel_askActivity() {

        Intent intent=new Intent(myLabelActivity.this, thelabel_askActivity.class);
        startActivity(intent);
    }

    private void init() {
        RequestQueue queue = Volley.newRequestQueue(this);
        //JSONArray ask=null;
//       (2)使用相应的请求需求
        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();

        Map<String, String> map = new HashMap<>();
        map.put("username", login1Activity.usernameStr);

        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null && response.length() > 0) {
                            JSONArray label = response.optJSONArray("user_label");

                            //String dataString = ask.toString();
                            Log.d(TAG, String.valueOf(response));

                            mylabel_Datas = new ArrayList<LabelBean>();
                            for (int i = 0; i < label.length(); i++) {
                                JSONObject jsonData = label.optJSONObject(i);

                                LabelBean data = new LabelBean();
                                data.setLb_title(jsonData.optString("lb_title"));


                                mylabel_Datas.add(data);
                                //test.setText(ask.toString());
                                //Log.i(TAG,e();getMessage(),volleyError);
                            }
                            //Toast.makeText(getActivity(), mDatas.toString(), Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(myLabelActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
                        mylabel_Adapter = new LabelAdapter(myLabelActivity.this,mylabel_Datas);
                        mylabel_Recycler.setAdapter(mylabel_Adapter);

                        mylabel_Recycler.setLayoutManager(mylabel_LayoutManager);
                        //noteRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                        //使用系统默认分割线
                        mylabel_Recycler.addItemDecoration(new DividerItemDecoration(myLabelActivity.this, DividerItemDecoration.VERTICAL));
                        //mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(myLabelActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, volleyError.getMessage(), volleyError);
            }
        });
        //(3)将请求需求加入到请求队列之中。
        queue.add(request);

    }

    private void initListener() {
        //下拉刷新
        mylabel_SwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout_mylabel);
        //设置 进度条的颜色变化，最多可以设置4种颜色
        mylabel_SwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#ff00ff"), Color.parseColor("#ff0f0f"), Color.parseColor("#0000ff"), Color.parseColor("#000000"));
        //setProgressViewOffset(boolean scale, int start, int end) 调整进度条距离屏幕顶部的距离
        initPullRefresh();

    }

    private void initPullRefresh() {
        mylabel_SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        init();
                        //刷新完成
                        mylabel_SwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(myLabelActivity.this, "更新了数据", Toast.LENGTH_SHORT).show();
                    }

                }, 3000);

            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_label:
                Toast.makeText(myLabelActivity.this, "Clicked", Toast.LENGTH_LONG).show();
                Intent i = new Intent(myLabelActivity.this, all_labelActivity.class);
                startActivity(i);
                break;

        }
        return false;
    }
}
