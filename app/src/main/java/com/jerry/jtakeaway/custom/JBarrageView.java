package com.jerry.jtakeaway.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.LinearLayout;

import com.jerry.jtakeaway.bean.model.Barrage;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("all")
public class JBarrageView extends LinearLayout {
    private List<Barrage> barrageList = new ArrayList<Barrage>();

    public JBarrageView(Context context) {
        super(context);
    }


}
