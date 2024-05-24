package com.santeut.auth.util;

import org.springframework.stereotype.Component;

@Component
public class LevelUtil {

    public String getTierName(int point){

        String tierName = "언덕";
        if (point >= 301 && point <= 500) tierName = "등산";
        else if (point >= 501 && point <= 1000 ) tierName = "설악산";
        else if (point >= 1001 && point <= 1500) tierName = "지리산";
        else if (point >= 1501) tierName = "한라산";

        return tierName;
    }
}
