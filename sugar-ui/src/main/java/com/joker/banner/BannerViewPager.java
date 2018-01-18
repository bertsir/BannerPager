package com.joker.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.joker.banner.adapter.BannerViewPagerAdapter;
import com.joker.banner.listener.OnItemCLickListener;

/**
 * CommonViewPager
 *
 * @author joker
 * @date 2018/1/10.
 */

public class BannerViewPager extends ViewPager {

    private OnItemCLickListener mOnItemCLickListener;
    private float oldX = 0, sens = 0;

    public BannerViewPager(Context context) {
        super(context);
        init(context);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        sens = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mOnItemCLickListener != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    oldX = ev.getX();
                    break;
                case MotionEvent.ACTION_UP:
                    final float newX = ev.getX();
                    if (Math.abs(oldX - newX) < sens && checkRange2Click(ev) == 0) {
                        performClick();
                    }
                    oldX = 0;
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 通过判断设置预显示宽 确定点击
     *
     * @param ev MotionEvent
     * @return -1 左边 0 中间 1 右边
     */
    private int checkRange2Click(MotionEvent ev) {
        final ViewGroup.MarginLayoutParams mp = (MarginLayoutParams) getLayoutParams();
        final int max = Math.max(mp.leftMargin, mp.rightMargin);
        if (ev.getX() < max) {
            //点击左边预显示部分
            setCurrentItem(getCurrentItem() - 1, false);
            return -1;
        } else if (ev.getX() > getResources().getDisplayMetrics().widthPixels - max) {
            //点击右边预显示部分
            setCurrentItem(getCurrentItem() + 1, false);
            return 1;
        } else {
            //点击当前显示
            final PagerAdapter adapter = getAdapter();
            if (adapter instanceof BannerViewPagerAdapter) {
                final int position = ((BannerViewPagerAdapter) adapter).toRealPosition(super.getCurrentItem());
                mOnItemCLickListener.onItemClick(position);
            }
            return 0;
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void setOnItemCLickListener(OnItemCLickListener listener) {
        mOnItemCLickListener = listener;
    }
}