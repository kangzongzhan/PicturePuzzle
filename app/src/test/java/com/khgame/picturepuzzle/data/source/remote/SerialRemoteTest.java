package com.khgame.picturepuzzle.data.source.remote;

import com.khgame.picturepuzzle.data.Serial;
import com.khgame.picturepuzzle.data.source.AppRepository;

import org.junit.Test;

public class SerialRemoteTest {

    @Test
    public void serialTest() {
        AppRepository appRepository = AppRepository.getINSTANCE();
        appRepository.getSerials().subscribe(serials -> {
            for (Serial serial: serials) {
                System.out.println(serial.getId());
            }
        });
    }
}
