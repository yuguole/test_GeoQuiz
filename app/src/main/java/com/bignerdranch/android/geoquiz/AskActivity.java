package com.bignerdranch.android.geoquiz;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bignerdranch.android.geoquiz.login1Activity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AskActivity extends  AppCompatActivity implements View.OnClickListener{

    private EditText asktitle;
    private EditText askdetails;
    private Button ask_add;

    public String asktitleStr,askdetailStr,usernameStr;
    public String Url = "http://yuguole.pythonanywhere.com/Iknow/addask";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);

        asktitle=(EditText)findViewById(R.id.asktitile);
        askdetails=(EditText)findViewById(R.id.askdetail);
        ask_add=(Button)findViewById(R.id.ask_add);

        ask_add.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id =view.getId();
        switch (id){
            case R.id.ask_add:
                //Toast.makeText(this, Url, Toast.LENGTH_SHORT).show();
                Addask(Url);
                break;

            default:
                break;
        }
    }

    private void Addask(String url){
        asktitleStr = asktitle.getText().toString().trim();
        askdetailStr = askdetails.getText().toString().trim();
//       (1)使用的方法 创建一个请求队列

        RequestQueue queue = Volley.newRequestQueue(this);
//       (2)使用相应的请求需求

        Map<String, String> map = new HashMap<>();
        map.put("asktitle", asktitleStr);
        map.put("askdetail", askdetailStr);
        map.put("askuser",login1Activity.usernameStr);



        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int status = response.optInt("status");
//                判断注册的状态
                        if (status == 200||status == 300||status == 510) {
                            Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
                            onBackPressed();
//                    QQ名字或者账号重复
                        } else if (status == 500) {
                            Toast.makeText(getApplicationContext(), "问题名重复", Toast.LENGTH_SHORT).show();
//                    出错了
                        } else if (status == 400) {
                            Toast.makeText(getApplicationContext(), "提问失败", Toast.LENGTH_SHORT).show();
                        }
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

    public class myAsyncTask extends AsyncTask<String, Void, Void> {
        //在线程之中所执行的方法
        @Override
        protected Void doInBackground(String... params) {
            Addask(params[0]);
            return null;
        }
    }

}
