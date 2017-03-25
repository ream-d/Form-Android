package com.android.form;

import java.util.Date;

import ream.form.annoation.FormView;


/**
 * 2017/1/12 0012.
 */
public class ValueBean {

    @FormView
    private String text;
    @FormView
    private String text1;
    @FormView
    private String text2;

    private String text3;

    private String text4;

    private String text5;

    private String text6;

    private String text7;

    private String text8;

    private String text9;

    private Date text10;

    private String text11;

    public String toString() {
        return text + "\n" +
                text1 + "\n" +
                text2 + "\n" +
                text3 + "\n" +
                text4 + "\n" +
                text5 + "\n" +
                text6 + "\n" +
                text7 + "\n" +
                text8 + "\n" +
                text9 + "\n" +
                String.valueOf(text10) + "\n" +
                text11;
    }
}
