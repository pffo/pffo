package com.ffo.ipiker;

import android.graphics.Point;
import android.support.annotation.IdRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ffo.ipiker.activity.BaseActivity;
import com.ffo.ipiker.fragment.HomePageFragment;
import com.ffo.ipiker.fragment.PersonalPageFragment;
import com.ffo.ipiker.fragment.ReportPageFragment;
import com.ffo.ipiker.util.DisplayScreen;
import com.ffo.ipiker.util.FileUtil;
import com.ffo.ipiker.util.LogUtil;

import java.util.HashMap;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static String TGA = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;//侧滑控件
    private FrameLayout fl_content; // fragment替换区域
    private LinearLayout ll_mainBottomMenu; //界面底部导航栏
    private RadioGroup mRadioGroup;
    private Toolbar mToolbar;//顶部标题栏
    private ActionBar mActionBar;
    private TextView tv_toolbar;//ToolBar居中标题

    private long mExitTime;// 返回键响应事件间隔计算常量。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mExitTime = System.currentTimeMillis();
        initView();

        // 这里一定要在save为null时才加载Fragment，Fragment中onCreateView等生命周里加载根子Fragment同理
        // 因为在页面重启时，Fragment会被保存恢复，而此时再加载Fragment会重复加载，导致重叠
        if (savedInstanceState == null) {
            initFragmentParamers();
            switchFragment(R.string.reportpage);
            showToolBarTitle(getString(R.string.reportpage));
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fragments != null) {
            fragments.clear();
            fragments = null;
        }
        setContentView(R.layout.clear_null);
    }

    /**
     * Fragment切换参数初始化
     */
    @Override
    public void initFragmentParamers() {
        content = R.id.fl_context;
        initFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * initView
     */
    private void initView() {
        findView();
    }

    /**
     * 初始化Fragment实例
     */
    private void initFragment() {
        if (fragments != null) {
            fragments.clear();
        } else {
            fragments = new HashMap<>();
        }

        HomePageFragment homePageFragment = new HomePageFragment();
        fragments.put(getString(R.string.homepage), homePageFragment);

        ReportPageFragment reportPageFragment = new ReportPageFragment();
        fragments.put(getString(R.string.reportpage), reportPageFragment);

        PersonalPageFragment personalPageFragment = new PersonalPageFragment();
        fragments.put(getString(R.string.personalpage), personalPageFragment);

    }

    /**
     * findViewById
     */
    private void findView() {
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_mainDrawerLayout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mDrawerLayout.addDrawerListener(mDrawerListener);

        fl_content = (FrameLayout) findViewById(R.id.fl_context);
        ll_mainBottomMenu = (LinearLayout) findViewById(R.id.ll_main_bottom_menu);

        mRadioGroup = (RadioGroup) findViewById(R.id.rg_radiogroup_main);
        mRadioGroup.setOnCheckedChangeListener(onCheckedChangeListener);

        tv_toolbar = (TextView) findViewById(R.id.tv_toolbar);

//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
//            //实现透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //实现透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }

        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setTitle("");
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeAsUpIndicator(R.drawable.bottom_personalpage2);
        }
        calculation();//根据底部导航栏的高度，计算正文内容显示内容的高度
    }

    /**
     * 计算fl_context的实际高度（根据底部导航栏的高度，计算页面正文内容显示内容的高度）
     */
    private void calculation() {
        final Point screenPoint = DisplayScreen.getDisplayScreen();
        if (ll_mainBottomMenu == null || mToolbar == null) {
            return;
        }
        ViewTreeObserver mViewTreeObserver = ll_mainBottomMenu.getViewTreeObserver();
        mViewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_mainBottomMenu.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                ViewTreeObserver mToolBarTreeObserver = mToolbar.getViewTreeObserver();
                mToolBarTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver
                        .OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        mToolbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        RelativeLayout.LayoutParams fl_contentLayoutParams = (RelativeLayout
                                .LayoutParams) fl_content.getLayoutParams();
                        fl_contentLayoutParams.height = screenPoint.y - ll_mainBottomMenu
                                .getHeight() - mToolbar.getHeight();
                        fl_contentLayoutParams.width = screenPoint.x;
                        fl_content.setLayoutParams(fl_contentLayoutParams); // 设置fl_content的实际宽高
                        LogUtil.i(TGA, "ll_mainBottomMenu height width : " +
                                ll_mainBottomMenu.getHeight
                                        () + "  " + ll_mainBottomMenu.getWidth());
                        LogUtil.i(TGA, "mToolBar height width : " +
                                mToolbar.getHeight
                                        () + "  " + mToolbar.getWidth());

                        LogUtil.i(TGA, "fl_contentLayoutParams height width : " +
                                fl_contentLayoutParams.height
                                + "  " + fl_contentLayoutParams.width);
                    }
                });

//                LogUtil.i(TGA, "ll_mainBottomMenu height width : " + ll_mainBottomMenu.getHeight
//                        () + "  " + ll_mainBottomMenu.getWidth());
                LogUtil.i(TGA, "displayScreen height width : " + screenPoint.y + "  " +
                        screenPoint.x);

            }
        });
    }


    @Override
    public void onClick(View v) {
    }

    /**
     * 更换ToolBar居中标题的显示文字
     *
     * @param title
     */
    private void showToolBarTitle(String title) {
        if (tv_toolbar != null) {
            tv_toolbar.setText(title);
        }
    }

    /**
     * DrawerLayout listener
     */
    DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {
        /**
         * 当抽屉被滑动的时候调用此方法
         * arg1 表示 滑动的幅度（0-1）
         */
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        /**
         * 当一个抽屉被完全打开的时候被调用
         */
        @Override
        public void onDrawerOpened(View drawerView) {
            drawerView.setClickable(true);
            //DrawerLayout界面点击事件穿透问题，即点击Drawerlayout上面的区域，会发现该位置DrawerLayout覆盖掉的区域的控件可以被点击
        }

        /**
         * 当一个抽屉完全关闭的时候调用此方法
         */
        @Override
        public void onDrawerClosed(View drawerView) {

        }

        /**
         * 当抽屉滑动状态改变的时候被调用
         * 状态值是STATE_IDLE（闲置--0）, STATE_DRAGGING（拖拽的--1）, STATE_SETTLING（固定--2）中之一。
         * 抽屉打开的时候，点击抽屉，drawer的状态就会变成STATE_DRAGGING，然后变成STATE_IDLE
         */
        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    //底部导航栏
    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup
            .OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId) {
                case R.id.rb_homepage:
                    //底部导航栏——首页
                    switchFragment(R.string.homepage);
                    showToolBarTitle(getString(R.string.homepage));
                    break;
                case R.id.rb_reportpage:
                    //底部导航栏——举报中心
                    switchFragment(R.string.reportpage);
                    showToolBarTitle(getString(R.string.reportpage));
                    break;
                case R.id.rb_personalpage:
                    //底部导航栏———个人中心
                    switchFragment(R.string.personalpage);
                    showToolBarTitle(getString(R.string.personalpage));
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //返回键事件拦截监听
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            LogUtil.v(TGA, "(System.currentTimeMillis() - mExitTime) " + (System
                    .currentTimeMillis() - mExitTime));
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT)
                        .show();
                mExitTime = System.currentTimeMillis();// 更新时间
            } else {
                this.finish();
                System.exit(0);// 退出程序
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
