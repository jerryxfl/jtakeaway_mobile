package com.jerry.jtakeaway.eventBusEvents;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BadgeEvent {
    private int count;

    public BadgeEvent() {
    }

    public BadgeEvent(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setPagePosition(BadgeEvent badgeEvent) {

    }
}
