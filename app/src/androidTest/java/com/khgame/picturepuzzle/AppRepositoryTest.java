package com.khgame.picturepuzzle;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.khgame.picturepuzzle.data.Serial;
import com.khgame.picturepuzzle.data.source.AppRepository;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.functions.Consumer;

import static org.junit.Assert.assertEquals;

/**
 * Created by kishadeng on 2017/12/18.
 */

@RunWith(AndroidJUnit4.class)
public class AppRepositoryTest {
    @Test
    public void useAppContext() throws Exception {

        AppRepository appRepository = AppRepository.getINSTANCE();
        appRepository.getSerials().subscribe(serials -> {
            for (Serial serial: serials) {
                System.out.println(serial.getId());
            }
        });
    }
}
