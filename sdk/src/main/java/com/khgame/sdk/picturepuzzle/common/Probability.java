package com.khgame.sdk.picturepuzzle.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zkang on 2017/3/31.
 */

public class Probability {

    private List<Bean> beans = new ArrayList<>();
    public synchronized Probability with(int probability, Runnable r) {
        Bean bean = new Bean(probability, r);
        beans.add(bean);
        return this;
    }

    public synchronized void go() {
        check100();
        int v = (int) (Math.random() * 100 + 1);
        int sum = 0;
        for (Bean b: beans) {
            sum += b.probability;
            if (v <= sum) {
                b.r.run();
                break;
            }
        }
    }

    private void check100() {
        int sum = 0;
        for (Bean b: beans) {
            sum += b.probability;
        }
        if (sum != 100) {
            throw new RuntimeException("Probability sum must be 100, now is:" + sum);
        }
    }

    class Bean {
        int probability;
        Runnable r;
        public Bean(int probability, Runnable r) {
            this.probability = probability;
            this.r = r;
            validBean();
        }
        private void validBean() {
            if (probability < 0 || probability > 100) {
                throw new IllegalArgumentException("probability is invalid:" + probability);
            }
            if (r == null) {
                throw new IllegalArgumentException("Runnable cannot be null");
            }
        }
    }
}
