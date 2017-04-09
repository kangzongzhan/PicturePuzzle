package com.khgame.sdk.picturepuzzle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.khgame.sdk.picturepuzzle.common.Constant;
import com.khgame.sdk.picturepuzzle.core.DisorderUtil;
import com.khgame.sdk.picturepuzzle.core.Point;
import com.khgame.sdk.picturepuzzle.service.model.SerialDto;
import com.khgame.sdk.picturepuzzle.service.model.SerialPictureDto;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by zkang on 2017/4/7.
 */

public class MakeSerialTest {

    @Test
    public void makeSerial() {
        String serialDir = "C:\\Users\\zkang\\Desktop\\dadwheregoing1";
        String urlBase = "http://kzz.oss-cn-shenzhen.aliyuncs.com/picturepuzzle/pictures/";
        List<SerialPictureDto> serialDtos = new ArrayList<>();
        File dir = new File(serialDir);
        File[] serialPictureFiles = dir.listFiles();

        List<Point> easyBase = DisorderUtil.randomStep(Constant.EASY_TEMPLATE_DEFAULT, 1000);
        List<Point> mediumBase = DisorderUtil.randomStep(Constant.MEDIUM_TEMPLATE_DEFAULT, 1000);
        List<Point> hardBase = DisorderUtil.randomStep(Constant.HARD_TEMPLATE_DEFAULT, 1000);

        for (File serialPictureFile:serialPictureFiles) {
            String fileName = serialPictureFile.getName();
            String[] namePieces = fileName.split("_");
            if (namePieces.length != 3) {
                continue;
            }

            easyBase = DisorderUtil.randomStep(easyBase, 1000);
            mediumBase = DisorderUtil.randomStep(mediumBase, 1000);
            hardBase = DisorderUtil.randomStep(hardBase, 1000);

            SerialPictureDto dto = new SerialPictureDto();
            dto.easyData = DisorderUtil.encode(easyBase);
            dto.mediumData = DisorderUtil.encode(mediumBase);
            dto.hardData = DisorderUtil.encode(hardBase);
            dto.name = namePieces[1];
            dto.uuid = UUID.randomUUID().toString();
            dto.url = urlBase + dto.uuid;
            serialPictureFile.renameTo(new File(serialDir, dto.uuid));
            serialDtos.add(dto);
        }
        Gson gson = new GsonBuilder().create();
        String result = gson.toJson(serialDtos);
        System.out.println(result);
    }


    @Test
    public void generateUuid() {
        System.out.println(UUID.randomUUID());
    }



}
