package com.kami.learneverything.com.kami.part;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.NumberPicker;

import com.kami.learneverything.R;

public class TestMyView extends AppCompatActivity {

    private NumberPicker numberPicker;
    private NumberPicker mYearNP;
    private NumberPicker mTermNP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_my_view);

        initView();


    }

    private void initView() {
        mYearNP = (NumberPicker) findViewById(R.id.np_year);
        String[] yearStrs = {"2013-2014","2014-2015","2015-2016"};
        mYearNP.setDisplayedValues(yearStrs);
        mYearNP.setMinValue(0);
        mYearNP.setMaxValue(yearStrs.length-1);
        mYearNP.setValue(1);

        mTermNP = (NumberPicker) findViewById(R.id.np_term);
        mTermNP.setMinValue(1);
        mTermNP.setMaxValue(2);

    }
}
