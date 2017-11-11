package com.khgame.picturepuzzle.operation;

import android.graphics.Color;

import com.khgame.picturepuzzle.db.model.SerialPo;
import com.khgame.picturepuzzle.db.operation.QueryAllSerialsOperation;
import com.khgame.picturepuzzle.model.Serial;
import com.khgame.picturepuzzle.service.model.SerialDto;
import com.khgame.picturepuzzle.service.operation.GetAllSerialsFromServiceOperation;

import java.util.ArrayList;
import java.util.List;

/**
 * 加载所有的Serial
 * 包括已经下载的 - 数据库
 *     网络的
 */

public class LoadSerilsOperation extends Operation<List<Serial>, Void> {

    private List<SerialPo> listPos = new ArrayList<>();
    private List<SerialDto> listDtos = new ArrayList<>();

    @Override
    protected void doWork() {

        new QueryAllSerialsOperation().callback(new Callback<List<SerialPo>, Void>() {
            @Override
            public void onSuccess(List<SerialPo> serialPos) {
                LoadSerilsOperation.this.listPos = serialPos;
                postSuccess(merge());
            }
        }).enqueue();

        new GetAllSerialsFromServiceOperation().callback(new Callback<List<SerialDto>, Void>() {
            @Override
            public void onSuccess(List<SerialDto> serialDtos) {
                LoadSerilsOperation.this.listDtos = serialDtos;
                postSuccess(merge());
            }
        }).enqueue();
    }

    private List<Serial> merge() {
        List<Serial> list = new ArrayList<>();
        for (SerialPo serialPo: listPos) {
            Serial serial = new Serial();
            serial.uuid = serialPo.uuid;
            serial.name = serialPo.name;
            serial.gameLevel = serialPo.gameLevel;
            serial.primaryColor = serialPo.primaryColor;
            serial.secondaryColor = serialPo.secondaryColor;
            serial.installState = Serial.State.INSTALLED;
            list.add(serial);
        }

        for (SerialDto serialDto: listDtos) {
            if (have(list, serialDto)) {
                continue;
            }
            Serial serial = new Serial();
            serial.uuid = serialDto.uuid;
            serial.name = serialDto.name;
            serial.primaryColor = Color.parseColor(serialDto.primaryColor);
            serial.secondaryColor = Color.parseColor(serialDto.secondaryColor);
            serial.installState = Serial.State.UNINSTALL;
            list.add(serial);
        }

        return list;
    }

    private boolean have(List<Serial> list, SerialDto serialDto) {
        for (Serial serial: list) {
            if (serial.uuid.equals(serialDto.uuid)) {
                return true;
            }
        }
        return false;
    }

}
