package com.jerry.jtakeaway.Notification;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.bean.model.NoticeStyle;

import java.util.ArrayList;
import java.util.List;

public class NoticeStyles {
    public static final List<NoticeStyle> noticeStyles = new ArrayList<NoticeStyle>();

    public static void init(){
        noticeStyles.add(new NoticeStyle("默认", R.drawable.c6662b0de7365559f79d9eb6088d9527));
        noticeStyles.add(new NoticeStyle("清新", R.drawable.c6662b0de7365559f79d9eb6088d9527));
        noticeStyles.add(new NoticeStyle("黑夜", R.drawable.c6662b0de7365559f79d9eb6088d9527));
        noticeStyles.add(new NoticeStyle("白昼", R.drawable.c6662b0de7365559f79d9eb6088d9527));
    }

}
