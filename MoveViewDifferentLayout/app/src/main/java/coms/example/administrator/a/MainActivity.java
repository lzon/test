package coms.example.administrator.a;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {

    private int rW = 102;
    private int rH = 102;

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);
    }

    private List<Button> listFa;
    RelativeLayout relativeLayoutMain;
    //------------------1
    private Button buttonChangGuiLB;
    private Button buttonChangGuiLT;
    private Button buttonChangGuiLS;
    private Button buttonChangGuiRB;
    private Button buttonChangGuiRT;
    private Button buttonChangGuiRS;
    //------------------2
    private Button buttonChangGuiZUO;
    private Button buttonChangGuiYOU;
    private Button buttonChangGuiSHANG;
    private Button buttonChangGuiXIA;
    private Button buttonChangGuiBK;
    //------------------3
    private Button buttonChangGuiM1;
    private Button buttonChangGuiM2;
    private Button buttonChangGuiM3;
    private Button buttonChangGuiM4;
    //------------------4
    private Button buttonChangGuiA;
    private Button buttonChangGuiB;
    private Button buttonChangGuiX;
    private Button buttonChangGuiY;
    private Button buttonChangGuiL;
    private Button buttonChangGuiR;

    /*zuhe*/
    //------------------1
    private Button buttonZuHeLB_A;
    private Button buttonZuHeLB_B;
    private Button buttonZuHeLT_A;
    private Button buttonZuHeLT_B;
    private Button buttonZuHeRB_A;
    private Button buttonZuHeRB_B;
    //------------------2
    private Button buttonZuHeLB_X;
    private Button buttonZuHeLB_Y;
    private Button buttonZuHeLT_X;
    private Button buttonZuHeLT_Y;
    private Button buttonZuHeRB_X;
    private Button buttonZuHeRB_Y;
    //------------------3
    private Button buttonZuHeM1_A;
    private Button buttonZuHeM1_B;
    private Button buttonZuHeM2_A;
    private Button buttonZuHeM2_B;
    private Button buttonZuHeRT_A;
    private Button buttonZuHeRT_B;
    //------------------4
    private Button buttonZuHeM1_X;
    private Button buttonZuHeM1_Y;
    private Button buttonZuHeM2_X;
    private Button buttonZuHeM2_Y;
    private Button buttonZuHeRT_X;
    private Button buttonZuHeRT_Y;
    //------------------5
    private Button buttonZuHeM3_A;
    private Button buttonZuHeM3_B;
    private Button buttonZuHeM4_A;
    private Button buttonZuHeM4_B;
    //------------------6
    private Button buttonZuHeM3_X;
    private Button buttonZuHeM3_Y;
    private Button buttonZuHeM4_X;
    private Button buttonZuHeM4_Y;
    //layout
    private ScrollView scrollViewChangGui;
    private ScrollView scrollViewZuhe;
    private boolean isUp = true;
    private FrameLayout floatButton;
    private boolean scrollEnable;
    private OrientationEventListener mOrientationListener;
    private float downX;
    private float downY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrollViewChangGui = findViewById(R.id.scroll_changguicontent);
        scrollViewZuhe = findViewById(R.id.scroll_zuhecontent);
        init();
        getPpi();
        //
        scrollViewChangGui.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        System.out.println("事件：scrollViewChangGui MotionEvent。ACTION_DOWN");
                        downX = event.getX();
                        downY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float x = event.getRawX();
                        float y = event.getRawY();

                        final float xDistance = event.getX() - downX;
                        final float yDistance = event.getY() - downY;
                        if (isUp) {
                            if (xDistance != 0 && yDistance != 0) {
                                if (listFa.size() > 0) {
                                    int l = (int) (listFa.get(listFa.size() - 1).getLeft() + xDistance);
                                    int r = (int) (listFa.get(listFa.size() - 1).getRight() + xDistance);
                                    int t = (int) (listFa.get(listFa.size() - 1).getTop() + yDistance);
                                    int b = (int) (listFa.get(listFa.size() - 1).getBottom() + yDistance);
                                } else {
                                    break;
                                }
                                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(rW, rH);
                                layoutParams.leftMargin = (int) (x - 50 / 2 - 30);
                                layoutParams.topMargin = (int) (y - 50 * 2 - 10);
                                listFa.get(listFa.size() - 1).setLayoutParams(layoutParams);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        isUp = false;
                        scrollEnable = false;
                        break;
                }
                return scrollEnable;
            }
        });

        scrollViewZuhe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float x = event.getRawX();
                        float y = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        isUp = false;
                        scrollEnable = false;
                        break;
                }
                return scrollEnable;
            }
        });


    }


    private void getPpi() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;  // 屏幕宽度（像素）
        int height = metric.heightPixels;  // 屏幕高度（像素）
        int midu = metric.densityDpi;
        float bizhi = metric.density;
        Log.e("like", "输出：" + "height = " + height + "width=" + width + "dpi=" + midu + "bizhi=" + bizhi);
    }

    private Button floatButtona;

    private FrameLayout initFloatButton(Drawable resource, View v) {
        floatButtona = new Button(MainActivity.this);
        floatButtona.setBackground(v.getBackground());
        floatButtona.setTag(v.getTag());
        RelativeLayout.LayoutParams relativeLayoutnew = new RelativeLayout.LayoutParams(rW, rH);
        int[] location = new int[2];
        v.getLocationInWindow(location);
        if (floatButtona.getParent() == null) { //102
            relativeLayoutnew.leftMargin = location[0];
            relativeLayoutnew.topMargin = location[1] - 101 / 2 - 22;
//            floatButtona.setLayoutParams(relativeLayoutnew);
            relativeLayoutMain.addView(floatButtona, relativeLayoutnew);
            listFa.add(floatButtona);
            v.setVisibility(View.INVISIBLE);
        }
        floatButtona.setOnTouchListener(this);
        return floatButton;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float xDistance = event.getX() - downX;
                final float yDistance = event.getY() - downY;
                if (xDistance != 0 && yDistance != 0) {
                    int l = (int) (v.getLeft() + xDistance);
                    int r = (int) (v.getRight() + xDistance);
                    int t = (int) (v.getTop() + yDistance);
                    int b = (int) (v.getBottom() + yDistance);
                    //v.layout(l, t, r, b);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(102, 102);
                    layoutParams.leftMargin = l;
                    layoutParams.topMargin = t;
                    v.setLayoutParams(layoutParams);
                    break;
                }
            case MotionEvent.ACTION_UP:
                Toast.makeText(MainActivity.this, "x值:" + event.getX() + "  y值:" + event.getY(), Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.button_changgu_lb:
                v.setTag("KEY_LB");
                addFloatLayout(v, R.drawable.lb);
                break;
            case R.id.button_changgu_lt:
                v.setTag("KEY_LT");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_changgu_ls:
                v.setTag("KEY_LS");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_changgu_rb:
                v.setTag("KEY_RB");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_changgu_rt:
                v.setTag("KEY_RT");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_changgu_rs:
                v.setTag("KEY_RS");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_changgui_xiangzuo:
                v.setTag("KEY_LEFT");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_changgui_xiangyou:
                v.setTag("KEY_RIGHT");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_changgui_xiangshang:
                v.setTag("KEY_TOP");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_changgui_xiangxia:
                v.setTag("KEY_DOWN");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_changgui_back:
                v.setTag("KEY_BACK");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_changgui_m1:
                v.setTag("KEY_M1");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_changgui_m2:
                v.setTag("KEY_M2");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_changgui_m3:
                v.setTag("KEY_M3");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_changgui_m4:
                v.setTag("KEY_M4");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_changgui_a:
                v.setTag("KEY_A");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_changgui_b:
                v.setTag("KEY_B");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_changgui_x:
                v.setTag("KEY_X");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_changgui_y:
                v.setTag("KEY_Y");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_changgui_l:
                v.setTag("KEY_L");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_changgui_r:
                v.setTag("KEY_R");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            //zuhe
            case R.id.button_zuhe_lb_a:
                v.setTag("KEY_LB_A");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_lb_b:
                v.setTag("KEY_LB_B");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_lt_a:
                v.setTag("KEY_LT_A");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_lt_b:
                v.setTag("KEY_LT_B");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_rb_a:
                v.setTag("KEY_RB_A");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_rb_b:
                v.setTag("KEY_RB_B");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_lb_x:
                v.setTag("KEY_LB_X");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_lb_y:
                v.setTag("KEY_LB_Y");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_lt_x:
                v.setTag("KEY_LT_X");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_lt_y:
                v.setTag("KEY_LT_Y");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_rb_x:
                v.setTag("KEY_RB_X");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_rb_y:
                v.setTag("KEY_RB_Y");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            //..3
            case R.id.button_zuhe_m1_a:
                v.setTag("KEY_M1_A");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_m1_b:
                v.setTag("KEY_M1_B");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_m2_a:
                v.setTag("KEY_M2_A");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_m2_b:
                v.setTag("KEY_M2_B");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_rt_a:
                v.setTag("KEY_RT_A");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_rt_b:
                v.setTag("KEY_RT_B");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            //..4
            case R.id.button_zuhe_m1_x:
                v.setTag("KEY_M1_X");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_m1_y:
                v.setTag("KEY_M1_Y");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_m2_x:
                v.setTag("KEY_M2_X");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_m2_y:
                v.setTag("KEY_M2_Y");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_rt_x:
                v.setTag("KEY_RT_X");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_rt_y:
                v.setTag("KEY_RT_Y");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            //..5
            case R.id.button_zuhe_m3_a:
                v.setTag("KEY_M3_A");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_m3_b:
                v.setTag("KEY_M3_B");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_m4_a:
                v.setTag("KEY_M4_A");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_m4_b:
                v.setTag("KEY_M4_B");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            //..6
            case R.id.button_zuhe_m3_x:
                v.setTag("KEY_M3_X");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_m3_y:
                v.setTag("KEY_M3_Y");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_m4_x:
                v.setTag("KEY_M4_X");
                addFloatLayout(v, R.drawable.xf_lt);
                break;
            case R.id.button_zuhe_m4_y:
                v.setTag("KEY_M4_Y");
                addFloatLayout(v, R.drawable.xf_lt);
                break;

        }

        return true;
    }

    private void addFloatLayout(View v, int resouce) {
        scrollEnable = true;
        isUp = true;
//      scrollViewChangGui.scrollTo(0, 0);
        initFloatButton(v.getBackground(), v);
    }

    private Button button_changgui;
    private Button button_zuhe;
    private ScrollView scroll_changguicontent;
    private ScrollView scroll_zuhecontent;

    @SuppressLint("WrongConstant")
    private void init() {
        button_changgui = findViewById(R.id.changgui);
        button_zuhe = findViewById(R.id.zuhe);
        scroll_changguicontent = findViewById(R.id.scroll_changguicontent);
        scroll_zuhecontent = findViewById(R.id.scroll_zuhecontent);

        relativeLayoutMain = findViewById(R.id.main);
        listFa = new ArrayList<Button>();
        buttonChangGuiLB = findViewById(R.id.button_changgu_lb);
        buttonChangGuiLB.setOnClickListener(this);
        buttonChangGuiLB.setOnLongClickListener(this);
        buttonChangGuiLT = findViewById(R.id.button_changgu_lt);
        buttonChangGuiLT.setOnClickListener(this);
        buttonChangGuiLT.setOnLongClickListener(this);
        buttonChangGuiLS = findViewById(R.id.button_changgu_ls);
        buttonChangGuiLS.setOnClickListener(this);
        buttonChangGuiLS.setOnLongClickListener(this);
        buttonChangGuiRB = findViewById(R.id.button_changgu_rb);
        buttonChangGuiRB.setOnClickListener(this);
        buttonChangGuiRB.setOnLongClickListener(this);
        buttonChangGuiRT = findViewById(R.id.button_changgu_rt);
        buttonChangGuiRT.setOnClickListener(this);
        buttonChangGuiRT.setOnLongClickListener(this);
        buttonChangGuiRS = findViewById(R.id.button_changgu_rs);
        buttonChangGuiRS.setOnClickListener(this);
        buttonChangGuiRS.setOnLongClickListener(this);
        //------------------2
        buttonChangGuiZUO = findViewById(R.id.button_changgui_xiangzuo);
        buttonChangGuiZUO.setOnClickListener(this);
        buttonChangGuiZUO.setOnLongClickListener(this);
        buttonChangGuiYOU = findViewById(R.id.button_changgui_xiangyou);
        buttonChangGuiYOU.setOnClickListener(this);
        buttonChangGuiYOU.setOnLongClickListener(this);
        buttonChangGuiSHANG = findViewById(R.id.button_changgui_xiangshang);
        buttonChangGuiSHANG.setOnClickListener(this);
        buttonChangGuiSHANG.setOnLongClickListener(this);
        buttonChangGuiXIA = findViewById(R.id.button_changgui_xiangxia);
        buttonChangGuiXIA.setOnClickListener(this);
        buttonChangGuiXIA.setOnLongClickListener(this);
        buttonChangGuiBK = findViewById(R.id.button_changgui_back);
        buttonChangGuiBK.setOnClickListener(this);
        buttonChangGuiBK.setOnLongClickListener(this);
        //------------------3
        buttonChangGuiM1 = findViewById(R.id.button_changgui_m1);
        buttonChangGuiM1.setOnClickListener(this);
        buttonChangGuiM1.setOnLongClickListener(this);
        buttonChangGuiM2 = findViewById(R.id.button_changgui_m2);
        buttonChangGuiM2.setOnClickListener(this);
        buttonChangGuiM2.setOnLongClickListener(this);
        buttonChangGuiM3 = findViewById(R.id.button_changgui_m3);
        buttonChangGuiM3.setOnClickListener(this);
        buttonChangGuiM3.setOnLongClickListener(this);
        buttonChangGuiM4 = findViewById(R.id.button_changgui_m4);
        buttonChangGuiM4.setOnClickListener(this);
        buttonChangGuiM4.setOnLongClickListener(this);
        //------------------4
        buttonChangGuiA = findViewById(R.id.button_changgui_a);
        buttonChangGuiA.setOnClickListener(this);
        buttonChangGuiA.setOnLongClickListener(this);
        buttonChangGuiB = findViewById(R.id.button_changgui_b);
        buttonChangGuiB.setOnClickListener(this);
        buttonChangGuiB.setOnLongClickListener(this);
        buttonChangGuiX = findViewById(R.id.button_changgui_x);
        buttonChangGuiX.setOnClickListener(this);
        buttonChangGuiX.setOnLongClickListener(this);
        buttonChangGuiY = findViewById(R.id.button_changgui_y);
        buttonChangGuiY.setOnClickListener(this);
        buttonChangGuiY.setOnLongClickListener(this);
        buttonChangGuiL = findViewById(R.id.button_changgui_l);
        buttonChangGuiL.setOnClickListener(this);
        buttonChangGuiL.setOnLongClickListener(this);
        buttonChangGuiR = findViewById(R.id.button_changgui_r);
        buttonChangGuiR.setOnClickListener(this);
        buttonChangGuiR.setOnLongClickListener(this);

        /*zuhe*/
        //------------------1
        buttonZuHeLB_A = findViewById(R.id.button_zuhe_lb_a);
        buttonZuHeLB_A.setOnClickListener(this);
        buttonZuHeLB_A.setOnLongClickListener(this);
        buttonZuHeLB_B = findViewById(R.id.button_zuhe_lb_b);
        buttonZuHeLB_B.setOnClickListener(this);
        buttonZuHeLB_B.setOnLongClickListener(this);
        buttonZuHeLT_A = findViewById(R.id.button_zuhe_lt_a);
        buttonZuHeLT_A.setOnClickListener(this);
        buttonZuHeLT_A.setOnLongClickListener(this);
        buttonZuHeLT_B = findViewById(R.id.button_zuhe_rb_b);
        buttonZuHeLT_B.setOnClickListener(this);
        buttonZuHeLT_B.setOnLongClickListener(this);
        buttonZuHeRB_A = findViewById(R.id.button_zuhe_rb_a);
        buttonZuHeRB_A.setOnClickListener(this);
        buttonZuHeRB_A.setOnLongClickListener(this);
        buttonZuHeRB_B = findViewById(R.id.button_changgu_rb);
        buttonZuHeRB_B.setOnClickListener(this);
        buttonZuHeRB_B.setOnLongClickListener(this);
        //------------------2
        buttonZuHeLB_X = findViewById(R.id.button_zuhe_lb_x);
        buttonZuHeLB_X.setOnClickListener(this);
        buttonZuHeLB_X.setOnLongClickListener(this);
        buttonZuHeLB_Y = findViewById(R.id.button_zuhe_lb_y);
        buttonZuHeLB_Y.setOnClickListener(this);
        buttonZuHeLB_Y.setOnLongClickListener(this);
        buttonZuHeLT_X = findViewById(R.id.button_zuhe_lt_x);
        buttonZuHeLT_X.setOnClickListener(this);
        buttonZuHeLT_X.setOnLongClickListener(this);
        buttonZuHeLT_Y = findViewById(R.id.button_zuhe_lt_y);
        buttonZuHeLT_Y.setOnClickListener(this);
        buttonZuHeLT_Y.setOnLongClickListener(this);
        buttonZuHeRB_X = findViewById(R.id.button_zuhe_rb_x);
        buttonZuHeRB_X.setOnClickListener(this);
        buttonZuHeRB_X.setOnLongClickListener(this);
        buttonZuHeRB_Y = findViewById(R.id.button_zuhe_rb_y);
        buttonZuHeRB_Y.setOnClickListener(this);
        buttonZuHeRB_Y.setOnLongClickListener(this);
        //------------------3
        buttonZuHeM1_A = findViewById(R.id.button_zuhe_m1_a);
        buttonZuHeM1_A.setOnClickListener(this);
        buttonZuHeM1_A.setOnLongClickListener(this);
        buttonZuHeM1_B = findViewById(R.id.button_zuhe_m1_b);
        buttonZuHeM1_B.setOnClickListener(this);
        buttonZuHeM1_B.setOnLongClickListener(this);
        buttonZuHeM2_A = findViewById(R.id.button_zuhe_m2_a);
        buttonZuHeM2_A.setOnClickListener(this);
        buttonZuHeM2_A.setOnLongClickListener(this);
        buttonZuHeM2_B = findViewById(R.id.button_zuhe_m2_b);
        buttonZuHeM2_B.setOnClickListener(this);
        buttonZuHeM2_B.setOnLongClickListener(this);
        buttonZuHeRT_A = findViewById(R.id.button_zuhe_rt_a);
        buttonZuHeRT_A.setOnClickListener(this);
        buttonZuHeRT_A.setOnLongClickListener(this);
        buttonZuHeRT_B = findViewById(R.id.button_zuhe_rt_b);
        buttonZuHeRT_B.setOnClickListener(this);
        buttonZuHeRT_B.setOnLongClickListener(this);
        //------------------4
        buttonZuHeM1_X = findViewById(R.id.button_zuhe_m1_x);
        buttonZuHeM1_X.setOnClickListener(this);
        buttonZuHeM1_X.setOnLongClickListener(this);
        buttonZuHeM1_Y = findViewById(R.id.button_zuhe_m1_y);
        buttonZuHeM1_Y.setOnClickListener(this);
        buttonZuHeM1_Y.setOnLongClickListener(this);
        buttonZuHeM2_X = findViewById(R.id.button_zuhe_m2_x);
        buttonZuHeM2_X.setOnClickListener(this);
        buttonZuHeM2_X.setOnLongClickListener(this);
        buttonZuHeM2_Y = findViewById(R.id.button_zuhe_m2_y);
        buttonZuHeM2_Y.setOnClickListener(this);
        buttonZuHeM2_Y.setOnLongClickListener(this);
        buttonZuHeRT_X = findViewById(R.id.button_zuhe_rt_x);
        buttonZuHeRT_X.setOnClickListener(this);
        buttonZuHeRT_X.setOnLongClickListener(this);
        buttonZuHeRT_Y = findViewById(R.id.button_zuhe_rt_y);
        buttonZuHeRT_Y.setOnClickListener(this);
        buttonZuHeRT_Y.setOnLongClickListener(this);
        //------------------5
        buttonZuHeM3_A = findViewById(R.id.button_zuhe_m3_a);
        buttonZuHeM3_A.setOnClickListener(this);
        buttonZuHeM3_A.setOnLongClickListener(this);
        buttonZuHeM3_B = findViewById(R.id.button_zuhe_m3_b);
        buttonZuHeM3_B.setOnClickListener(this);
        buttonZuHeM3_B.setOnLongClickListener(this);
        buttonZuHeM4_A = findViewById(R.id.button_zuhe_m4_a);
        buttonZuHeM3_A.setOnClickListener(this);
        buttonZuHeM3_A.setOnLongClickListener(this);
        buttonZuHeM4_B = findViewById(R.id.button_zuhe_m4_b);
        buttonZuHeM3_B.setOnClickListener(this);
        buttonZuHeM3_B.setOnLongClickListener(this);
        //------------------6
        buttonZuHeM3_X = findViewById(R.id.button_zuhe_m3_x);
        buttonZuHeM3_X.setOnClickListener(this);
        buttonZuHeM3_X.setOnLongClickListener(this);
        buttonZuHeM3_Y = findViewById(R.id.button_zuhe_m3_y);
        buttonZuHeM3_Y.setOnClickListener(this);
        buttonZuHeM3_Y.setOnLongClickListener(this);
        buttonZuHeM4_X = findViewById(R.id.button_zuhe_m4_x);
        buttonZuHeM4_X.setOnClickListener(this);
        buttonZuHeM3_Y.setOnLongClickListener(this);
        buttonZuHeM4_Y = findViewById(R.id.button_zuhe_m4_y);
        buttonZuHeM4_Y.setOnClickListener(this);
        buttonZuHeM4_Y.setOnLongClickListener(this);
//        view = LayoutInflater.from(this).inflate(R.layout.activity_float, null);
//        Button button = view.findViewById(R.id.f_button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "ttttttttt", Toast.LENGTH_LONG).show();
//            }
//        });
    }


    public void onClickChangGui(View view) {
        if (scroll_changguicontent.getVisibility() == View.INVISIBLE) {
            scroll_zuhecontent.setVisibility(View.INVISIBLE);
        }
    }

    public void onClickZuHe(View view) {
        if (scroll_zuhecontent.getVisibility() == View.INVISIBLE) {
            scroll_changguicontent.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setTurnScreenOn(boolean turnScreenOn) {
        super.setTurnScreenOn(turnScreenOn);
    }

    public void onClickClean(View view) {
        for (int i = 0; i < buttons.size(); i++) {
            Button frameLayout = buttons.get(i);
            if (relativeLayoutMain != null && frameLayout.getParent() != null) {
                relativeLayoutMain.removeView(frameLayout);
            }
        }
        for (int x = 0; x < listFa.size(); x++) {
            Button frameLayout = listFa.get(x);
            if (relativeLayoutMain != null && frameLayout.getParent() != null) {
                relativeLayoutMain.removeView(frameLayout);
            }
        }
        listFa.clear();
        buttonChangGuiLB.setVisibility(View.VISIBLE);
        buttonChangGuiLT.setVisibility(View.VISIBLE);
        buttonChangGuiLS.setVisibility(View.VISIBLE);
        buttonChangGuiRT.setVisibility(View.VISIBLE);
        buttonChangGuiRB.setVisibility(View.VISIBLE);
        buttonChangGuiRS.setVisibility(View.VISIBLE);

        buttonChangGuiZUO.setVisibility(View.VISIBLE);
        buttonChangGuiYOU.setVisibility(View.VISIBLE);
        buttonChangGuiSHANG.setVisibility(View.VISIBLE);
        buttonChangGuiXIA.setVisibility(View.VISIBLE);
        buttonChangGuiBK.setVisibility(View.VISIBLE);

        buttonChangGuiM1.setVisibility(View.VISIBLE);
        buttonChangGuiM2.setVisibility(View.VISIBLE);
        buttonChangGuiM3.setVisibility(View.VISIBLE);
        buttonChangGuiM4.setVisibility(View.VISIBLE);

        buttonChangGuiA.setVisibility(View.VISIBLE);
        buttonChangGuiB.setVisibility(View.VISIBLE);
        buttonChangGuiX.setVisibility(View.VISIBLE);
        buttonChangGuiY.setVisibility(View.VISIBLE);
        buttonChangGuiL.setVisibility(View.VISIBLE);
        buttonChangGuiR.setVisibility(View.VISIBLE);
        buttonZuHeLB_A.setVisibility(View.VISIBLE);
        buttonZuHeLB_B.setVisibility(View.VISIBLE);
        buttonZuHeLT_A.setVisibility(View.VISIBLE);
        buttonZuHeLT_B.setVisibility(View.VISIBLE);
        buttonZuHeRB_A.setVisibility(View.VISIBLE);
        buttonZuHeRB_B.setVisibility(View.VISIBLE);
        //....2
        buttonZuHeLB_X.setVisibility(View.VISIBLE);
        buttonZuHeLB_Y.setVisibility(View.VISIBLE);
        buttonZuHeLT_X.setVisibility(View.VISIBLE);
        buttonZuHeLT_Y.setVisibility(View.VISIBLE);
        buttonZuHeRB_X.setVisibility(View.VISIBLE);
        buttonZuHeRB_Y.setVisibility(View.VISIBLE);
        //....3
        buttonZuHeM1_A.setVisibility(View.VISIBLE);
        buttonZuHeM1_B.setVisibility(View.VISIBLE);
        buttonZuHeM2_A.setVisibility(View.VISIBLE);
        buttonZuHeM2_B.setVisibility(View.VISIBLE);
        buttonZuHeRT_A.setVisibility(View.VISIBLE);
        buttonZuHeRT_B.setVisibility(View.VISIBLE);
        //...4
        buttonZuHeM1_X.setVisibility(View.VISIBLE);
        buttonZuHeM1_Y.setVisibility(View.VISIBLE);
        buttonZuHeM2_X.setVisibility(View.VISIBLE);
        buttonZuHeM2_Y.setVisibility(View.VISIBLE);
        buttonZuHeRT_X.setVisibility(View.VISIBLE);
        buttonZuHeRT_Y.setVisibility(View.VISIBLE);

        //...5
        buttonZuHeM3_A.setVisibility(View.VISIBLE);
        buttonZuHeM3_B.setVisibility(View.VISIBLE);
        buttonZuHeM4_A.setVisibility(View.VISIBLE);
        buttonZuHeM4_B.setVisibility(View.VISIBLE);
        //...6
        buttonZuHeM3_X.setVisibility(View.VISIBLE);
        buttonZuHeM3_Y.setVisibility(View.VISIBLE);
        buttonZuHeM4_X.setVisibility(View.VISIBLE);
        buttonZuHeM4_Y.setVisibility(View.VISIBLE);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 判断当前界面是否是桌面
     */
    private boolean isHome() {
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return getHomes().contains(rti.get(0).topActivity.getPackageName());
    }

    /**
     * 获得属于桌面的应用的应用包名称
     *
     * @return 返回包含所有包名的字符串列表
     */
    private List<String> getHomes() {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }


    public void onClickGuanBi(View view) {
        view.setBackgroundResource(R.drawable.img_button_blue);
        findViewById(R.id.queding).setBackgroundResource(R.drawable.img_button_grade);
    }


    public void startConnect(View view) {
    }


    List<Button> buttons = new ArrayList<>();

    public void jianweixianshi(View view) {
    }

    public void startScan(View view) {
    }

    public void startRead(View view) {
    }
}

