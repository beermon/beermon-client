package com.shopify.android.beermon.async;

/**
 * Created with IntelliJ IDEA.
 * User: csaunders
 * Date: 2012-10-18
 * Time: 4:56 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GetCallback<T> {
    public void execute(T result);
}
