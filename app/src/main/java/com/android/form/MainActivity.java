package com.android.form;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.util.form.FormUtil;
import com.util.form.annotation.FormField;
import com.util.form.annotation.FormMap;
import com.util.form.annotation.FormMethod;

import java.util.Date;
import java.util.Map;

import ream.form.CacheForm;
import ream.form.annoation.Form;
import ream.form.annoation.FormView;
import ream.form.binder.IReamForm;


@FormMap(name = "text", bean = ValueBean.class)
@Form(name = "c")
public class MainActivity extends AppCompatActivity {

    @com.util.form.annotation.FormView
    @FormView
    TextView text;
    @com.util.form.annotation.FormView
    private TextView text1;
    @com.util.form.annotation.FormView
    private TextView text2;
    @com.util.form.annotation.FormView
    private TextView text3;
    @com.util.form.annotation.FormView
    private TextView text4;
    @com.util.form.annotation.FormView
    private TextView text5;
    @com.util.form.annotation.FormView
    private TextView text6;
    @com.util.form.annotation.FormView
    private TextView text7;
    @com.util.form.annotation.FormView
    private TextView text8;

    private TextView textView9;

    private TextView text10;

    @FormView
    TextView text11;

    @FormField
    private String text9 = "text9";

    @FormMethod(beanFiled = "text10")
    public Date getText10() {
        return new Date();
    }

    @FormField(beanFiled = "text11")
    private String value = "text11";

    private Binder binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        FormUtil.bind(this);
        binder = new Binder();

    }


    private void initView() {
        text = (TextView) findViewById(R.id.text);
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        text4 = (TextView) findViewById(R.id.text4);
        text5 = (TextView) findViewById(R.id.text5);
        text6 = (TextView) findViewById(R.id.text6);
        text7 = (TextView) findViewById(R.id.text7);
        text8 = (TextView) findViewById(R.id.text8);
        textView9 = (TextView) findViewById(R.id.text9);
        text10 = (TextView) findViewById(R.id.text10);
        text11 = (TextView) findViewById(R.id.text11);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof TextView) {
                    Object valueBean = FormUtil.formBean(MainActivity.this);
                    if (valueBean != null)
                        ((TextView) v).setText(String.valueOf(valueBean));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        binder.bind(this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        FormUtil.unbind(this);
        super.onDestroy();
    }

    static class Binder implements IReamForm<MainActivity> {

        public final static String formName = MainActivity.class.getName();

        private TextView text;
        private TextView text1;
        private TextView text2;


        @Override
        public void bind(MainActivity target) {
            text = target.text;
            text1 = target.text1;
            text2 = target.text2;
        }

        public void resetForm() {
            Map<String, CharSequence> value = CacheForm.getCache("mapName");
            text.setText(value.get("text"));
            text1.setText(value.get("text1"));
            text2.setText(value.get("text2"));
        }

        public void storeForm() {
            Map<String, CharSequence> value = CacheForm.getCache("mapName");
            value.put("text", text.getText());
            value.put("text1", text1.getText());
            value.put("text2", text2.getText());
        }

        @Override
        public String formName() {
            return null;
        }


    }
}
