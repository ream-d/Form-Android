package com.util.form;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * 2017/1/12 0012.
 */
class ViewBean {


    private String beanFiled;
    private Method method;
    private WeakReference<Object> view;


    public ViewBean(String beanFiled, Method method, Object view) {

        this.beanFiled = beanFiled;
        this.method = method;
        this.view = new WeakReference<>(view);
    }

    public Method getMethod() {
        return method;
    }

    public Object getView() {
        return view.get();
    }


    public String getBeanFiled() {
        return beanFiled;
    }
}
