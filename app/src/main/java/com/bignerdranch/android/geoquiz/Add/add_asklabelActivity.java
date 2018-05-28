package com.bignerdranch.android.geoquiz.Add;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bignerdranch.android.geoquiz.Adapter.LabeladdsearchAdapter;
import com.bignerdranch.android.geoquiz.Adapter.LabelnowAdapter;
import com.bignerdranch.android.geoquiz.Fragment.FullyLinearLayoutManager;
import com.bignerdranch.android.geoquiz.Fragment.RecyclerItemClickListener;
import com.bignerdranch.android.geoquiz.Models.LabelBean;
import com.bignerdranch.android.geoquiz.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class add_asklabelActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    private RecyclerView addasklabel_Recycler;
    private SwipeRefreshLayout addasklabel_SwipeRefreshLayout;
    private LabelnowAdapter addasklabel_Adapter;
    //private LabelnowAdapter addasklabel_Adapter;
    private List<LabelBean> addasklabel_Datas;
    private Toolbar addasklabel_toolbar;
    private SearchView addasklabel_SearchView;
    FullyLinearLayoutManager addasklabel_LayoutManager = new FullyLinearLayoutManager(this);

    private List<LabelBean> findList;
    private LabeladdsearchAdapter findListAdapter;

    private ImageButton addlabel;
    private ImageButton deletelabel;

    public static List<LabelBean> addLabel;//= new ArrayList<LabelBean>();

    //public static String The_LabelStr;
    private static final String showurl = "http://yuguole.pythonanywhere.com/Iknow/showlabel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_asklabel);

        initView();
    }

    private void initView() {
        addasklabel_Recycler = (RecyclerView) findViewById(R.id.recyclerview_addasklabel);
        //如果item的内容不改变view布局大小，那使用这个设置可以提高RecyclerView的效率
        addasklabel_Recycler.setHasFixedSize(true);
        addasklabel_toolbar = (Toolbar) findViewById(R.id.toolbar_addasklabel);
        addasklabel_toolbar.setTitle("添加问题标签");//标题

        addasklabel_toolbar.inflateMenu(R.menu.menu_addasklabel);
        addasklabel_toolbar.setOnMenuItemClickListener(this);

        //addlabel = (ImageButton) findViewById(R.id.item_searchlabeladd);
        //deletelabel = (ImageButton) findViewById(R.id.item_nowlabeldelete);


        //MenuItem searchItem=  addasklabel_toolbar.getMenu().findItem(R.id.addasklabel_search);//findViewById(R.id.homepage_search);
        addasklabel_SearchView = (SearchView) findViewById(R.id.addasklabel_search);
        initSearchView();//搜索

        //左边的小箭头（注意需要在setSupportActionBar(toolbar)之后才有效果）
        //myask_toolbar.setNavigationIcon(R.mipmap.ic_search);
        //myask_toolbar.inflateMenu(R.menu.menu_homepage);
        //myask_toolbar.setOnMenuItemClickListener((Toolbar.OnMenuItemClickListener) this);
        addLabel = new ArrayList<LabelBean>();
        init();
        initListener();//刷新
        initClickitem();//点击事件


    }


    private void initSearchView() {
        findList = new ArrayList<LabelBean>();
        addasklabel_SearchView.setQueryHint("你想知道标签");

        //Toast.makeText(getActivity(), mSearchView.toString(), Toast.LENGTH_SHORT).show();
        addasklabel_SearchView.setSubmitButtonEnabled(true);

        addasklabel_SearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (TextUtils.isEmpty(query)) {
                    Toast.makeText(add_asklabelActivity.this, "请输入查找内容！", Toast.LENGTH_SHORT).show();
                    addasklabel_Recycler.setAdapter(addasklabel_Adapter);
                } else {
                    findList.clear();
                    for (int i = 0; i < addasklabel_Datas.size(); i++) {
                        LabelBean information = addasklabel_Datas.get(i);
                        if (information.getLb_title().contains(query)) {
                            findList.add(information);
                        }
                    }
                    if (findList.size() == 0) {
                        Toast.makeText(add_asklabelActivity.this, "未搜索到结果", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.i("NewActivity", "###length=" + findList.size());
                        Toast.makeText(add_asklabelActivity.this, "查找成功", Toast.LENGTH_SHORT).show();
                        findListAdapter = new LabeladdsearchAdapter(add_asklabelActivity.this, findList);
                        //findListAdapter.set
                        addasklabel_Recycler.setAdapter(findListAdapter);
                        findListAdapter.notifyDataSetChanged();
                    }
                }

                return true;
            }

            //在输入时触发的方法，当字符真正显示到searchView中才触发，像是拼音，在输入法组词的时候不会触发
            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    addasklabel_Recycler.setAdapter(addasklabel_Adapter);
                } else {
                    findList.clear();
                    for (int i = 0; i < addasklabel_Datas.size(); i++) {
                        LabelBean information = addasklabel_Datas.get(i);
                        if (information.getLb_title().contains(newText)) {
                            findList.add(information);
                        }
                    }
                    findListAdapter = new LabeladdsearchAdapter(add_asklabelActivity.this, findList);
                    findListAdapter.notifyDataSetChanged();
                    addasklabel_Recycler.setAdapter(findListAdapter);
                    //findListAdapter.set
                }
                return false;
            }
        });

        addasklabel_Recycler.setLayoutManager(addasklabel_LayoutManager);
        //noteRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        //使用系统默认分割线
        addasklabel_Recycler.addItemDecoration(new DividerItemDecoration(add_asklabelActivity.this, DividerItemDecoration.VERTICAL));
        //mAdapter.notifyDataSetChanged();

        //mAdapter=new DataAdapter(getActivity(),)
    }


    private void initClickitem() {

        //点击跳转
        addasklabel_Recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(add_asklabelActivity.this, addasklabel_Recycler, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Toast.makeText(add_asklabelActivity.this, "点击", Toast.LENGTH_SHORT).show();
                        // do whatever
                        final TextView lbtitle = (TextView) view.findViewById(R.id.item_label_title);
                        LabelBean data = new LabelBean();
                        data.setLb_title(lbtitle.getText().toString());

                        addLabel.add(data);
                        //The_LabelStr=lbtitle.getText().toString();
                        Toast.makeText(add_asklabelActivity.this, "添加了" + lbtitle.getText().toString(), Toast.LENGTH_SHORT).show();
                        //startLabel_askActivity();
                        addoneLabel();

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Toast.makeText(add_asklabelActivity.this, "长按", Toast.LENGTH_SHORT).show();
                        // do whatever
                    }
                })
        );

    }

    private void addoneLabel() {
        addasklabel_Adapter = new LabelnowAdapter(add_asklabelActivity.this, addLabel);
        addasklabel_Recycler.setAdapter(addasklabel_Adapter);

        addasklabel_Recycler.setLayoutManager(addasklabel_LayoutManager);
        //noteRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        //使用系统默认分割线
        addasklabel_Recycler.addItemDecoration(new DividerItemDecoration(add_asklabelActivity.this, DividerItemDecoration.VERTICAL));
        addasklabel_Adapter.notifyDataSetChanged();
    }


    private void initListener() {
        //下拉刷新
        addasklabel_SwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout_addasklabel);
        //设置 进度条的颜色变化，最多可以设置4种颜色
        addasklabel_SwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#ff00ff"), Color.parseColor("#ff0f0f"), Color.parseColor("#0000ff"), Color.parseColor("#000000"));
        //setProgressViewOffset(boolean scale, int start, int end) 调整进度条距离屏幕顶部的距离
        initPullRefresh();
    }

    private void initPullRefresh() {
        addasklabel_SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        init();
                        //刷新完成
                        addasklabel_SwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(add_asklabelActivity.this, "更新了数据", Toast.LENGTH_SHORT).show();
                    }

                }, 3000);

            }
        });
    }

    //获取所有标签数据
    private void init() {
        RequestQueue queue = Volley.newRequestQueue(this);
        //JSONArray ask=null;
//       (2)使用相应的请求需求
        Toast.makeText(this, showurl, Toast.LENGTH_SHORT).show();

        Map<String, String> map = new HashMap<>();
        //map.put("username", login1Activity.usernameStr);

        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, showurl, paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null && response.length() > 0) {
                            JSONArray label = response.optJSONArray("label");

                            //String dataString = ask.toString();
                            Log.d(TAG, String.valueOf(response));

                            addasklabel_Datas = new ArrayList<LabelBean>();
                            for (int i = 0; i < label.length(); i++) {
                                JSONObject jsonData = label.optJSONObject(i);
                                LabelBean data = new LabelBean();
                                data.setLb_title(jsonData.optString("labeltitle"));

                                addasklabel_Datas.add(data);
                                //test.setText(ask.toString());
                                //Log.i(TAG,e();getMessage(),volleyError);
                            }
                            //Toast.makeText(getActivity(), mDatas.toString(), Toast.LENGTH_SHORT).show();
                        }

                        //Toast.makeText(add_asklabelActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
                        //addasklabel_Adapter = new LabelAdapter(add_asklabelActivity.this, addasklabel_Datas);
                        //addasklabel_Recycler.setAdapter(addasklabel_Adapter);

                        //addasklabel_Recycler.setLayoutManager(addasklabel_LayoutManager);
                        //noteRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                        //使用系统默认分割线
                        // addasklabel_Recycler.addItemDecoration(new DividerItemDecoration(add_asklabelActivity.this, DividerItemDecoration.VERTICAL));
                        //mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(add_asklabelActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, volleyError.getMessage(), volleyError);
            }
        });
        //(3)将请求需求加入到请求队列之中。
        queue.add(request);

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.add_asklabel:
                //Toast.makeText(add_asklabelActivity.this, addLabel.toString(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(add_asklabelActivity.this, addAskActivity.class);
                //Intent i = new Intent(getActivity(), addAskActivity.class);
                startActivity(i);
                break;

        }
        return true;
    }

}
