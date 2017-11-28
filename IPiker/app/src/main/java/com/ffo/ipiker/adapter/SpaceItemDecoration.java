package com.ffo.ipiker.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Author: huchunhua
 * Time: 2017/8/29 20:33
 * Package: com.ffo.ipiker.adapter
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 一句话描述
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    int mSpace;


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        outRect.left = mSpace;
//        outRect.right = mSpace;
//        outRect.bottom = mSpace;
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.top = mSpace;
        }

    }

    public SpaceItemDecoration(int space) {
        this.mSpace = space;
    }
}
