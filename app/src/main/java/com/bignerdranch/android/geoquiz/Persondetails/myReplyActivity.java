package com.bignerdranch.android.geoquiz.Persondetails;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
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
import com.bignerdranch.android.geoquiz.Adapter.myReplyAdapter;
import com.bignerdranch.android.geoquiz.Fragment.FullyLinearLayoutManager;
import com.bignerdranch.android.geoquiz.Fragment.RecyclerItemClickListener;
import com.bignerdranch.android.geoquiz.Models.ReplyBean;
import com.bignerdranch.android.geoquiz.R;
import com.bignerdranch.android.geoquiz.The_details.theask_detailsActivity;
import com.bignerdranch.android.geoquiz.The_details.thereply_detailsActivity;
import com.bignerdranch.android.geoquiz.login1Activity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class myReplyActivity extends AppCompatActivity {
    private RecyclerView myreply_Recycler;
    private SwipeRefreshLayout myreply_SwipeRefreshLayout;
    private myReplyAdapter myreply_Adapter;
    private List<ReplyBean> myreply_Datas;
    //private Toolbar myask_toolbar;
    //private SearchView myask_SearchView;
    FullyLinearLayoutManager myreply_LayoutManager = new FullyLinearLayoutManager(this);

    private static final String url = "http://yuguole.pythonanywhere.com/Iknow/myreply";

    //public static String myasktitleStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reply);
        initView();
    }

    private void initView() {
        myreply_Recycler = (RecyclerView) findViewById(R.id.recyclerview_myreply);
        //如果item的内容不改变view布局大小，那使用这个设置可以提高RecyclerView的效率
        myreply_Recycler.setHasFixedSize(true);
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
        myreply_Recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(myReplyActivity.this, myreply_Recycler,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Toast.makeText(myReplyActivity.this, "点击", Toast.LENGTH_SHORT).show();
                        // do whatever
                        final TextView re_id=(TextView)view.findViewById(R.id.item_myreply_reid);
                        theask_detailsActivity.replyIDStr=re_id.getText().toString();
                        Toast.makeText(myReplyActivity.this, re_id.getText(), Toast.LENGTH_SHORT).show();
                        start_thereplyActivity();

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        Toast.makeText(myReplyActivity.this, "长按", Toast.LENGTH_SHORT).show();
                        // do whatever
                    }
                })
        );

    }

    private void start_thereplyActivity() {
        Intent intent=new Intent(myReplyActivity.this,thereply_detailsActivity.class);
        startActivity(intent);
    }

    private void init() {
        RequestQueue queue = Volley.newRequestQueue(this);
        //JSONArray ask=null;
//       (2)使用相应的请求需求
        //Toast.makeText(this, url, Toast.LENGTH_SHORT).show();

        Map<String, String> map = new HashMap<>();
        map.put("username", login1Activity.usernameStr);

        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null && response.length() > 0) {
                            int status = response.optInt("status");
                            if (status == 200) {
                                JSONArray reply = response.optJSONArray("myreply");

                                //String dataString = ask.toString();
                                Log.d(TAG, String.valueOf(response));

                                myreply_Datas = new ArrayList<ReplyBean>();
                                for (int i = 0; i < reply.length(); i++) {
                                    JSONObject jsonData = reply.optJSONObject(i);
                                    //JSONObject jsonData1 = jsonData.optJSONObject("fields");

                                    ReplyBean data = new ReplyBean();
                                    //String reuser=QuizActivity.UseridToUsername(userid);
                                    //UseridToName(id);
                                    //int replyid=jsonData.optInt("replyid");
                                    //String reid=""+replyid;
                                    data.setRe_id(jsonData.optInt("replyid"));
                                    data.setRe_ask(jsonData.optString("replyask"));
                                    data.setRe_details(jsonData.optString("replydetail"));
                                    data.setRe_time(jsonData.optString("replytime"));

                                    myreply_Datas.add(data);
                                    //test.setText(ask.toString());
                                    //Log.i(TAG,e();getMessage(),volleyError);
                                }
                                //Toast.makeText(myReplyActivity.this, myreply_Datas.toString(), Toast.LENGTH_SHORT).show();
                                myreply_Adapter = new myReplyAdapter(myReplyActivity.this, myreply_Datas);
                                myreply_Recycler.setAdapter(myreply_Adapter);

                                myreply_Recycler.setLayoutManager(myreply_LayoutManager);
                                //noteRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                                //使用系统默认分割线
                                myreply_Recycler.addItemDecoration(new DividerItemDecoration(myReplyActivity.this, DividerItemDecoration.VERTICAL));
                                //mAdapter.notifyDataSetChanged();
                                //Toast.makeText(getActivity(), mDatas.toString(), Toast.LENGTH_SHORT).show();
                            }

                            //Toast.makeText(getActivity(), mDatas.toString(), Toast.LENGTH_SHORT).show();
                        }

                        //Toast.makeText(theask_detailsActivity.this, "加载成功", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //Toast.makeText(myReplyActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, volleyError.getMessage(), volleyError);
            }
        });
        //(3)将请求需求加入到请求队列之中。
        queue.add(request);
    }

    private void initListener() {
        //下拉刷新
        myreply_SwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout_myreply);
        //设置 进度条的颜色变化，最多可以设置4种颜色
        myreply_SwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#ff00ff"), Color.parseColor("#ff0f0f"), Color.parseColor("#0000ff"), Color.parseColor("#000000"));
        //setProgressViewOffset(boolean scale, int start, int end) 调整进度条距离屏幕顶部的距离
        initPullRefresh();

    }

    private void initPullRefresh() {
        myreply_SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        init();
                        //刷新完成
                        myreply_SwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(myReplyActivity.this, "更新了数据", Toast.LENGTH_SHORT).show();
                    }

                }, 3000);

            }
        });
    }

}
