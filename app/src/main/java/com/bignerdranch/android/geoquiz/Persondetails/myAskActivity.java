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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bignerdranch.android.geoquiz.Adapter.DataAdapter;
import com.bignerdranch.android.geoquiz.Fragment.Homepage;
import com.bignerdranch.android.geoquiz.Fragment.RecyclerItemClickListener;
import com.bignerdranch.android.geoquiz.The_details.theask_detailsActivity;
import com.bignerdranch.android.geoquiz.Models.AskBean;
import com.bignerdranch.android.geoquiz.Fragment.FullyLinearLayoutManager;
import com.bignerdranch.android.geoquiz.R;
import com.bignerdranch.android.geoquiz.login1Activity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class myAskActivity extends AppCompatActivity {
    private RecyclerView myask_Recycler;
    private SwipeRefreshLayout myask_SwipeRefreshLayout;
    private DataAdapter myask_Adapter;
    private List<AskBean> myask_Datas;
    private Toolbar myask_toolbar;
    private SearchView myask_SearchView;
    FullyLinearLayoutManager myask_LayoutManager = new FullyLinearLayoutManager(this);

    private static final String url = "http://yuguole.pythonanywhere.com/Iknow/myask";

    public static String myasktitleStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ask);

        initView();
    }

    private void initView() {
        myask_Recycler = (RecyclerView) findViewById(R.id.recyclerview_myask);
        //如果item的内容不改变view布局大小，那使用这个设置可以提高RecyclerView的效率
        myask_Recycler.setHasFixedSize(true);
        //myask_toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        //myask_toolbar.setTitle("我提出的问题");//标题
        //setSupportActionBar(myask_toolbar);

        //左边的小箭头（注意需要在setSupportActionBar(toolbar)之后才有效果）
        //myask_toolbar.setNavigationIcon(R.mipmap.ic_search);
        //myask_toolbar.inflateMenu(R.menu.menu_homepage);
        //myask_toolbar.setOnMenuItemClickListener((Toolbar.OnMenuItemClickListener) this);
        init();
        initListener();//刷新

        //点击跳转
        initClickitem();//点击事件

    }

    //点击跳转事件
    private void initClickitem() {
        myask_Recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(myAskActivity.this, myask_Recycler ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Toast.makeText(myAskActivity.this, "点击", Toast.LENGTH_SHORT).show();
                        // do whatever
                        final TextView lbtitle=(TextView)view.findViewById(R.id.item_title);
                        Homepage.asktitleStr=lbtitle.getText().toString();
                        Toast.makeText(myAskActivity.this, lbtitle.getText(), Toast.LENGTH_SHORT).show();
                        startAsk_homeActivity();

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        Toast.makeText(myAskActivity.this, "长按", Toast.LENGTH_SHORT).show();
                        // do whatever
                    }
                })
        );

    }

    private void startAsk_homeActivity() {
        Intent intent=new Intent(myAskActivity.this,theask_detailsActivity.class);
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
                            JSONArray ask = response.optJSONArray("content");

                            //String dataString = ask.toString();
                            Log.d(TAG, String.valueOf(response));

                            myask_Datas = new ArrayList<AskBean>();
                            for (int i = 0; i < ask.length(); i++) {
                                JSONObject jsonData = ask.optJSONObject(i);
                                JSONObject jsonData1 = jsonData.optJSONObject("fields");

                                AskBean data = new AskBean();
                                data.setTitle(jsonData1.optString("ask_title"));
                                data.setDetails(jsonData1.optString("ask_details"));
                                data.setAsktime(jsonData1.optString("ask_time"));
                                data.setAskuser(login1Activity.usernameStr);

                                myask_Datas.add(data);
                                //test.setText(ask.toString());
                                //Log.i(TAG,e();getMessage(),volleyError);
                            }
                            //Toast.makeText(getActivity(), mDatas.toString(), Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(myAskActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
                        myask_Adapter = new DataAdapter(myAskActivity.this, myask_Datas);
                        myask_Recycler.setAdapter(myask_Adapter);

                        myask_Recycler.setLayoutManager(myask_LayoutManager);
                        //noteRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                        //使用系统默认分割线
                        myask_Recycler.addItemDecoration(new DividerItemDecoration(myAskActivity.this, DividerItemDecoration.VERTICAL));
                        //mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(myAskActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, volleyError.getMessage(), volleyError);
            }
        });
        //(3)将请求需求加入到请求队列之中。
        queue.add(request);

    }

    private void initListener() {
        //下拉刷新
        myask_SwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout_myask);
        //设置 进度条的颜色变化，最多可以设置4种颜色
        myask_SwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#ff00ff"), Color.parseColor("#ff0f0f"), Color.parseColor("#0000ff"), Color.parseColor("#000000"));
        //setProgressViewOffset(boolean scale, int start, int end) 调整进度条距离屏幕顶部的距离
        initPullRefresh();

    }

    private void initPullRefresh() {
        myask_SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        init();
                        //刷新完成
                        myask_SwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(myAskActivity.this, "更新了数据", Toast.LENGTH_SHORT).show();
                    }

                }, 3000);

            }
        });
    }

}
