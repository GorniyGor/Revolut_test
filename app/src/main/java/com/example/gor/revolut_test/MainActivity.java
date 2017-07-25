package com.example.gor.revolut_test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerViewTop;
    RecyclerView mRecyclerViewBottom;
    private RecyclerAdapter mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //----Service---------------------------------------

        //----
        //---initialData----

        //----RecyclerAdapter----
        mRecyclerAdapter = new RecyclerAdapter(getLayoutInflater());
        //---First recycler------
        mRecyclerViewTop = (RecyclerView)findViewById(R.id.id_recycler_top);
        mRecyclerViewTop.setAdapter(mRecyclerAdapter);
        mRecyclerViewTop.setLayoutManager(
                new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        mRecyclerViewTop.setHasFixedSize(true);
        //---Second recycler------
        mRecyclerViewBottom = (RecyclerView)findViewById(R.id.id_recycler_bottom);
        mRecyclerViewBottom.setAdapter(mRecyclerAdapter);
        mRecyclerViewBottom.setLayoutManager(
                new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        mRecyclerViewBottom.setHasFixedSize(true);


    }
}
