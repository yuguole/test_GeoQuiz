package com.bignerdranch.android.geoquiz.The_details;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bignerdranch.android.geoquiz.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class thereply_detailsActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    private TextView thereply_user;
    private TextView thereply_time;
    private TextView thereply_details;

    private Toolbar thereply_toolbar;

    private String there_asktitle;
    //private Button button_addreply;

    private static final String url = "http://yuguole.pythonanywhere.com/Iknow/thereply";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thereply_details);

        initView();
    }

    private void initView() {
        thereply_user=(TextView)findViewById(R.id.thereply_user);
        thereply_time=(TextView)findViewById(R.id.thereply_time);
        thereply_details=(TextView)findViewById(R.id.thereply_details);

        thereply_toolbar = (Toolbar) findViewById(R.id.toolbar_thereply);
        //thereply_toolbar.setTitle();//标题

        thereply_toolbar.inflateMenu(R.menu.menu_thereply);
        thereply_toolbar.setOnMenuItemClickListener(this);

        initText();
    }

    private void initText() {
        //(1)使用的方法 创建一个请求队列
        RequestQueue queue = Volley.newRequestQueue(this);
//       (2)使用相应的请求需求
        //Toast.makeText(this, urlask, Toast.LENGTH_SHORT).show();

        Map<String, String> map = new HashMap<>();
        map.put("replyid", theask_detailsActivity.replyIDStr);


        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null && response.length() > 0) {

                            int id=response.optInt("id");
                            theask_detailsActivity.replyIDStr=""+id;

                            JSONObject there_user = response.optJSONObject("re_user");
                            thereply_user.setText(there_user.optString("username"));
                            thereply_details.setText(response.optString("re_details"));
                            thereply_time.setText(response.optString("re_time"));

                            JSONObject there_ask = response.optJSONObject("re_ask");
                            there_asktitle=there_ask.optString("ask_title");
                            thereply_toolbar.setTitle(there_asktitle);//标题
                        }
                        //Toast.makeText(getActivity(), mDatas.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        //(3)将请求需求加入到请求队列之中。
        queue.add(request);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
