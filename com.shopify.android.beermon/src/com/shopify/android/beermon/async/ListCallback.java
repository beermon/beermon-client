package com.shopify.android.beermon.async;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: csaunders
 * Date: 2012-10-18
 * Time: 4:47 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ListCallback<T> {
    public void execute(List<T> list);
}
