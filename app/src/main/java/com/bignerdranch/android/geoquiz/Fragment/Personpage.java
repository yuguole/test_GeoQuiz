package com.bignerdranch.android.geoquiz.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bignerdranch.android.geoquiz.Persondetails.myReplyActivity;
import com.bignerdranch.android.geoquiz.all_labelActivity;
import com.bignerdranch.android.geoquiz.Persondetails.myAskActivity;
import com.bignerdranch.android.geoquiz.R;

public class Personpage extends Fragment {

    private Button myInfo,myLabel,myAsk,myReply;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_personpage, container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        myLabel=(Button)getActivity().findViewById(R.id.person_mylabel);
        myAsk=(Button)getActivity().findViewById(R.id.person_myask);
        myReply=(Button)getActivity().findViewById(R.id.person_myreply);

        myLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "mylabel", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(),all_labelActivity.class);
                startActivity(i);
            }
        });

        myAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "myask", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(),myAskActivity.class);
                startActivity(i);
            }
        });

        myReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "mylabel", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(),myReplyActivity.class);
                startActivity(i);
            }
        });
    }
}
