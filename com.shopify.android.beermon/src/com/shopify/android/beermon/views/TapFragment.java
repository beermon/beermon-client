package com.shopify.android.beermon.views;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.shopify.android.beermon.R;

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

}
