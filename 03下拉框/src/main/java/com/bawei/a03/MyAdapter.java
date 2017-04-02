package com.bawei.a03;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 作    者：云凯文
 * 时    间：2017/2/27
 * 描    述：
 * 修改时间：
 */

public class MyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> msgs;

    public MyAdapter(Context context, ArrayList<String> msgs) {
        this.context = context;
        this.msgs = msgs;
    }

    @Override
    public int getCount() {
        return msgs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_list, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_msg = (TextView) convertView.findViewById(R.id.tv_msg);
            viewHolder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        //根据位置得到数据
        final String msg = msgs.get(position);
        viewHolder.tv_msg.setText(msg);
        //设置删除的监听事件
        viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.从集合删除
                msgs.remove(msg);
                //2.刷新适配器 --刷新UI
                notifyDataSetChanged();


            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_msg;
        ImageView iv_delete;

    }
}
