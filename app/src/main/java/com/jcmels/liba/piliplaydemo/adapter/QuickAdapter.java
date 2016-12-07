package com.jcmels.liba.piliplaydemo.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jcmels.liba.piliplaydemo.R;
import com.jcmels.liba.piliplaydemo.bean.LiveStreamItem;

import java.util.List;

/**
 * Created by LIBA on 11/18/2016.
 */

public class QuickAdapter extends BaseQuickAdapter<LiveStreamItem, BaseViewHolder> {
    public QuickAdapter(List<LiveStreamItem> list) {
        super(R.layout.item_cardview,list);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, LiveStreamItem item) {
        SimpleDraweeView snapimg = viewHolder.getView(R.id.cv_img);
        SimpleDraweeView headimg = viewHolder.getView(R.id.cv_headimg);
        if (item.getPiliroomname()!=null) viewHolder.setText(R.id.cv_nameTv,item.getPiliroomname() );
        if(item.getPilier()!=null) viewHolder.setText(R.id.cv_username,item.getPilier());
        if(item.getPiliheaderimgurl()!=null) headimg.setImageURI(item.getPiliheaderimgurl());
            snapimg.setImageURI(item.getSnapurl());

    }
}