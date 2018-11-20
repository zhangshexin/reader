package com.redread.libary.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.redread.R;
import com.redread.base.BaseRecycelAdapter;
import com.redread.base.BaseViewHolder;
import com.redread.databinding.LayoutCellBookBinding;
import com.redread.databinding.LayoutLoadmoreBinding;
import com.redread.databinding.LayoutModelListBinding;
import com.redread.libary.Activity_modeDetaillList;
import com.redread.net.Api;
import com.redread.net.netbean.NetBeanBook;
import com.redread.utils.GlideUtils;

import java.util.List;

/**
 * Created by zhangshexin on 2018/9/21.
 * 模块列表
 */

public class Adapter_modelList extends BaseRecycelAdapter<BaseViewHolder> {
    private List<NetBeanBook> books;
    private Context mContext;

    public Adapter_modelList(Context context, List<NetBeanBook> books) {
        super(context);
        this.mContext = context;
        this.books = books;
    }

    private boolean addedFooterView=false;
    public boolean isAdded(){
        return addedFooterView;
    }
    @Override
    public int getItemCount() {
        return addedFooterView?books.size()+1:books.size();
    }

    public void setAddedFooterView(boolean added){
        addedFooterView=added;
        notifyDataSetChanged();
    }
    private int footer=1;
    @Override
    public int getItemViewType(int position) {
        if(addedFooterView&&position==getItemCount()-1){
            return footer;
        }
        return super.getItemViewType(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==footer){
            LayoutLoadmoreBinding loadmoreBinding=DataBindingUtil.inflate(inflater,R.layout.layout_loadmore,parent,false);
            return new BaseViewHolder(loadmoreBinding);
        }else {
            LayoutCellBookBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_cell_book, parent, false);
            return new BaseViewHolder(binding);
        }

    }


    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if(getItemViewType(position)==footer){
            LayoutLoadmoreBinding loadmoreBinding = (LayoutLoadmoreBinding) holder.getBinding();
            loadmoreBinding.loadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setClickable(false);
                    loadmoreBinding.loadMorePB.setVisibility(View.VISIBLE);
                    //加载更多
                    ((Activity_modeDetaillList)mContext).loadData();
                }
            });
            loadmoreBinding.loadMorePB.setVisibility(View.GONE);
        }else{
            LayoutCellBookBinding binding = (LayoutCellBookBinding) holder.getBinding();
            NetBeanBook book = books.get(position);
            binding.bookAuthor.setText(book.getAuthor());
            binding.bookDes.setText(book.getIntroduction());
            binding.bookName.setText(book.getName());
            GlideUtils.LoadImage(mContext, Api.downUrl + book.getCoverPath(), binding.bookCover);
        }
    }
}
