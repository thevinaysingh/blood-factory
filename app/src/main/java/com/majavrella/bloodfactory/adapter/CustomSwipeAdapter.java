package com.majavrella.bloodfactory.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.majavrella.bloodfactory.R;

/**
 * Created by shubham on 23/8/16.
 */
public class CustomSwipeAdapter extends PagerAdapter {
    private  int[] image_resources = {R.drawable.s_0,R.drawable.s_1,R.drawable.s_3};
    private String[] big_text = {"BRILLIANT\nLEARNING","LESSON\nMANAGEMENT","GOAL ORIENTED"};
    private String[] small_text = {"With integrated tools and customized tools plans, we intelligently teach you the quran.",
            "Lesson management to further help students stay on track and complete their goals.",
            "Allowing administrators, teachers, parents to follow along with the process."};
    private Context ctx;
    private LayoutInflater layoutInflator;

    public CustomSwipeAdapter(Context ctx){
        this.ctx =ctx;
    }
    @Override
    public int getCount() {
        return image_resources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return (view==(LinearLayout)object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflator=(LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflator.inflate(R.layout.swipe_layout,container,false);
        ImageView imageView=(ImageView)item_view.findViewById(R.id.imageView);
        TextView textView1 = (TextView)item_view.findViewById(R.id.big_text);
        imageView.setImageResource(image_resources[position]);
        textView1.setText(big_text[position]);
        TextView textView2 = (TextView)item_view.findViewById(R.id.small_text);
        textView2.setText(small_text[position]);
        container.addView(item_view);
        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
