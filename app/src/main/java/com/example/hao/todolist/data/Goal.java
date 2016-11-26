package com.example.hao.todolist.data;

import com.yalantis.beamazingtoday.interfaces.BatModel;

/**
 * Created by hao on 2016-11-26.
 */

public class Goal implements BatModel {

    private String name;
    private boolean isChecked;

    public Goal(String name) {

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String getText() {
        return getName();
    }

}
