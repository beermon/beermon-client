package com.shopify.android.beermon.views;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shopify.android.beermon.R;
import com.shopify.android.beermon.models.Beer;
import com.shopify.android.beermon.models.Tap;

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
        LinearLayout ll = (LinearLayout)inflater.inflate(R.layout.tap_status, container, false);

        return ll;
    }

    public void update(Tap tap) {
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

        View beerTag = (View)getView().findViewById(R.id.beer_tag);
        if (beerTag.getVisibility() != View.VISIBLE) {
            //fade it in
            beerTag.setAlpha(0.0f);
            beerTag.setVisibility(View.VISIBLE);
            beerTag.animate().alpha(1.0f);
        }
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

}
