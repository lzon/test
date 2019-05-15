package coms.example.administrator.ble;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by lzj
 * on 2019/5/15
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    Set set;

    public MyAdapter(Context context, Set set) {
        this.context = context;
        this.set = set;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        TextView textView = new TextView(context);
        return new MyViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        Object[] objects = set.toArray();
        myViewHolder.textView.setText(objects[i].toString());
    }

    @Override
    public int getItemCount() {
        return set.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }
}
