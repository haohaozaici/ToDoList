package com.example.hao.todolist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.hao.todolist.data.DataGoal;
import com.example.hao.todolist.data.Goal;
import com.google.gson.Gson;
import com.yalantis.beamazingtoday.interfaces.AnimationType;
import com.yalantis.beamazingtoday.interfaces.BatModel;
import com.yalantis.beamazingtoday.listeners.BatListener;
import com.yalantis.beamazingtoday.listeners.OnOutsideClickedListener;
import com.yalantis.beamazingtoday.ui.adapter.BatAdapter;
import com.yalantis.beamazingtoday.ui.animator.BatItemAnimator;
import com.yalantis.beamazingtoday.ui.widget.BatRecyclerView;
import com.yalantis.beamazingtoday.util.TypefaceUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnOutsideClickedListener {

    String dataStirng;
    private BatRecyclerView mBatRecyclerView;
    private BatAdapter mBatAdapter;
    private List<BatModel> mGoals = new ArrayList<BatModel>() {
        {
            add(new Goal("first"));
        }

    };
    private DataGoal mData = new DataGoal();
    private BatItemAnimator mBatItemAnimator;
    private BatListener mBatListener = new BatListener() {
        @Override
        public void add(String s) {
            Goal goal = new Goal(s);
            mData.mGoalList.add(0, goal);
            saveData();

            mGoals.add(0, goal);
            mBatAdapter.notify(AnimationType.ADD, 0);
        }

        @Override
        public void delete(int position) {
            mData.mGoalList.remove(position);
            saveData();

            mGoals.remove(position);
            mBatAdapter.notify(AnimationType.REMOVE, position);
        }

        @Override
        public void move(int from, int to) {
            if (from >= 0 && to >= 0) {
                mBatItemAnimator.setPosition(to);
                BatModel model = mGoals.get(from);
                mGoals.remove(from);
                mGoals.add(to, model);

                //data change
                Goal goal = mData.mGoalList.get(from);
                mData.mGoalList.remove(from);
                mData.mGoalList.add(to, goal);

                mBatAdapter.notify(AnimationType.MOVE, from, to);

                if (from == 0 || to == 0) {
                    mBatRecyclerView.getView().scrollToPosition(Math.min(from, to));
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        ((TextView) findViewById(R.id.text_title)).setTypeface(TypefaceUtil.getTypeface(this));

        mBatItemAnimator = new BatItemAnimator();
        mBatAdapter = new BatAdapter(mGoals, mBatListener, mBatItemAnimator).setOnOutsideClickListener(this);

        mBatRecyclerView = (BatRecyclerView) findViewById(R.id.bat_recycler_view);
        mBatRecyclerView.getView().setLayoutManager(new LinearLayoutManager(this));
        mBatRecyclerView.getView().setAdapter(mBatAdapter);


    }

    private void loadData() {
        SharedPreferences dataSP = getSharedPreferences("data", MODE_PRIVATE);
        dataStirng = dataSP.getString("goals", "");
        if (!dataStirng.equals("")) {
            Gson gson = new Gson();
            mData = gson.fromJson(dataStirng, DataGoal.class);
            for (int i = 0; i < mData.mGoalList.size(); i++) {
                mGoals.add(mData.mGoalList.get(i));
            }

        }

    }

    private void saveData() {
        Gson gson = new Gson();
        String jsonObj = gson.toJson(mData);

        SharedPreferences data = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putString("goals", jsonObj);
        editor.apply();
    }

    @Override
    public void onOutsideClicked() {
        mBatRecyclerView.revertAnimation();
    }
}
