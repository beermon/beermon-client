package com.shopify.android.beermon.views;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.*;
import com.shopify.android.beermon.R;
import com.shopify.android.beermon.models.Beer;
import com.shopify.android.beermon.models.Keg;
import com.shopify.android.beermon.models.Tap;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: carson
 * Date: 2012-10-18
 * Time: 11:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class TapFragment extends Fragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //TODO: get state from bundle


    }

    public View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
        RelativeLayout rl = (RelativeLayout)inflater.inflate(R.layout.tap_status, container, false);

        final ScrollView beerFill = (ScrollView)rl.findViewById(R.id.beer_color_scroll);
        beerFill.setOnTouchListener( new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return rl;
    }

    public void update(Tap tap) {
        View outOfOrderTag = getView().findViewById(R.id.out_of_order_tag);
        if (tap.keg == null) {
            outOfOrderTag.setVisibility(View.VISIBLE);

            View beerTag = (View)getView().findViewById(R.id.working_tap_status);
            beerTag.setVisibility(View.INVISIBLE);
            return;
        }

        outOfOrderTag.setVisibility(View.INVISIBLE);

        TextView textView = (TextView)getView().findViewById(R.id.name);
        textView.setText(tap.keg.beer.name);

        textView = (TextView)getView().findViewById(R.id.brewery);
        textView.setText(tap.keg.beer.brewery);

        textView = (TextView)getView().findViewById(R.id.ibu);
        textView.setText("IBUS: " + (int)tap.keg.beer.ibus);

        textView = (TextView)getView().findViewById(R.id.srm);
        textView.setText("SRM: " + (int)tap.keg.beer.srm);

        textView = (TextView)getView().findViewById(R.id.abv);
        textView.setText("ABV: " + tap.keg.beer.abv + "%");

        textView = (TextView)getView().findViewById(R.id.date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd yyyy");
        textView.setText(simpleDateFormat.format(tap.keg.createdAt).toUpperCase());

        setRating(tap.keg.beer.rating);

        setBarcode(tap);

        //set color
        String[] srmColors = getResources().getStringArray(R.array.srm_colors);
        View beerColor = getView().findViewById(R.id.beer_color);
        int srmRounded = (int)Math.max(Math.min(tap.keg.beer.srm, 30.0), 1.0);
        beerColor.setBackgroundColor(Color.parseColor(srmColors[(int) tap.keg.beer.srm]));

        View beerTag = (View)getView().findViewById(R.id.working_tap_status);
        if (beerTag.getVisibility() != View.VISIBLE) {
            //fade it in
            beerTag.setAlpha(0.0f);
            beerTag.setVisibility(View.VISIBLE);
            beerTag.animate().alpha(1.0f);
        }

        Log.v("Beermon", "Update keg " + tap.keg.beer.name + " volume: " + tap.keg.remaining + " / " + tap.keg.capacity + "L");

        adjustGlassView(tap.keg);
    }

    private void setBarcode(Tap tap) {
        String hashText = tap.keg.beer.name + " " + tap.keg.beer.brewery + tap.keg.createdAt.toString();
        int hashCode = hashText.hashCode();

        double coin = hashCode % 3;//Math.random() * 3.0; //three sided coin ಠ_ಠ
        int resource;
        if (coin <= 1.0) {
            resource = R.drawable.barcode_1;
        } else if (coin <= 2.0) {
            resource = R.drawable.barcode_2;
        } else {
            resource = R.drawable.barcode_3;
        }

        ImageView barcode = (ImageView)getView().findViewById(R.id.barcode);
        barcode.setImageDrawable(getResources().getDrawable(resource));
    }

    private void setRating(float rating) {
        //round it so we can have 5/5 beers
        rating = Math.round(rating);
        View[] views = {
                getView().findViewById(R.id.star1),
                getView().findViewById(R.id.star2),
                getView().findViewById(R.id.star3),
                getView().findViewById(R.id.star4),
                getView().findViewById(R.id.star5)};

        for (int i=0; i<views.length; ++i) {
            views[i].setVisibility((i+1 <= rating) ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private void adjustGlassView(Keg keg) {
        ScrollView beerFill = (ScrollView)getView().findViewById(R.id.beer_color_scroll);
        int totalHeight = beerFill.getChildAt(0).getHeight();

        int offset = (int)((float)totalHeight * keg.remaining / keg.capacity) - beerFill.getHeight();

        Log.v("Beermon", "offset = " + offset + " totalHeight: " + totalHeight + " beer:" + keg.remaining + "/" + keg.capacity);

        beerFill.scrollTo(0, 0);
        beerFill.smoothScrollTo(0, offset);
    }

}
