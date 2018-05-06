package com.bignerdranch.android.geoquiz.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bignerdranch.android.geoquiz.AskActivity;
import com.bignerdranch.android.geoquiz.R;
import com.bignerdranch.android.geoquiz.login1Activity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

import static com.android.volley.VolleyLog.TAG;
import static com.android.volley.VolleyLog.e;

public class Homepage extends Fragment  {
    private RecyclerView noteRecycler;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DataAdapter mAdapter;
    private List<AskBean> mDatas;

    FullyLinearLayoutManager mLayoutManager = new FullyLinearLayoutManager(getActivity());

    //public JSONArray ask=null;


    private ArrayList<String> date;

    //Volley begin
    //private RequestQueue mQueue;
    private static final String url="http://yuguole.pythonanywhere.com/Iknow/showask";






    private Button Ask;
    //private TextView test;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_homepage, container,false);
        //提问按钮
        Ask =(Button) view.findViewById(R.id.ask);


        noteRecycler = (RecyclerView) view.findViewById(R.id.recyclerview);
        //如果item的内容不改变view布局大小，那使用这个设置可以提高RecyclerView的效率
        //noteRecycler.setHasFixedSize(true);
        //设置为垂直
        init();
        //mAdapter =  new DataAdapter(getActivity(),mDatas);
        //noteRecycler.setAdapter(mAdapter);

        //initdata();



        //initListener();
        //mSwipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    private void initListener() {
        //下拉刷新
        mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipelayout);
        //设置 进度条的颜色变化，最多可以设置4种颜色
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#ff00ff"),Color.parseColor("#ff0f0f"),Color.parseColor("#0000ff"),Color.parseColor("#000000"));
        //setProgressViewOffset(boolean scale, int start, int end) 调整进度条距离屏幕顶部的距离
        initPullRefresh();

    }

    private void initPullRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        init();
                        //刷新完成
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "更新了数据", Toast.LENGTH_SHORT).show();
                    }

                }, 3000);

            }
        });
    }

    private void init() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        //JSONArray ask=null;
//       (2)使用相应的请求需求
        Toast.makeText(getActivity(), url, Toast.LENGTH_SHORT).show();

        Map<String, String> map = new HashMap<>();

        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response!=null&&response.length()>0){
                            JSONArray ask = response.optJSONArray("ask");

                            String dataString = ask.toString();
                            Log.d(TAG, String.valueOf(response));

                            mDatas = new ArrayList<AskBean>();
                            for (int i = 0; i < ask.length(); i++) {
                                JSONObject jsonData = ask.optJSONObject(i);
                                AskBean data = new AskBean();
                                data.setTitle(jsonData.optString("title"));
                                data.setDetails(jsonData.optString("details"));

                                mDatas.add(data);
                                //test.setText(ask.toString());
                                //Log.i(TAG,e();getMessage(),volleyError);
                            }
                            //Toast.makeText(getActivity(), mDatas.toString(), Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(getActivity(), mDatas.toString(), Toast.LENGTH_SHORT).show();
                        mAdapter =  new DataAdapter(getActivity(),mDatas);
                        noteRecycler.setAdapter(mAdapter);

                        noteRecycler.setLayoutManager(mLayoutManager);
                        //noteRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                        //使用系统默认分割线
                        noteRecycler.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
                        //mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_SHORT).show();
                Log.i(TAG,volleyError.getMessage(),volleyError);
            }
        });
        //(3)将请求需求加入到请求队列之中。
        queue.add(request);


        //App.getRequeatQueue().add(jsonrequest);
    }




    //提问按钮跳转
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Button button = (Button) getActivity().findViewById(R.id.ask);
        Ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(), AskActivity.class);
                startActivity(i);
            }
        });

        initListener();


    }

}
/*
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources()
                        .getDisplayMetrics()));
        //设置监听器，这里就简单的每当刷新(圆形进度条出现)时，延迟5秒将刷新状态改为false，即刷新结束。进度条也会消失
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG," onRefresh() now:"+mSwipeRefreshLayout.isRefreshing());
                mSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                },5000);
            }
        });
        */
