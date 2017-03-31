package com.khgame.sdk.picturepuzzle.operation;

/**
 * Created by Kisha Deng on 2/27/2017.
 * This operation do nothing and will also success
 * Can be used to combine multi operation in its onSuccess callback
 */

public class EmptyOperation extends Operation<Void, Void> {
    @Override
    protected void doWork() {
        postSuccess(null);
    }
}
