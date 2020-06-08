package com.jerry.jtakeaway.utils;

import okhttp3.Callback;

public interface JCallback extends Callback {
    void response(Object t);
}
