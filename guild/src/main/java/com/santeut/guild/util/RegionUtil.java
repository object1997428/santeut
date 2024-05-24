package com.santeut.guild.util;

import com.santeut.guild.common.exception.DataNotFoundException;
import com.santeut.guild.common.response.ResponseCode;
import org.springframework.stereotype.Component;

@Component
public class RegionUtil {

    public String getRegionName(int regionId){

        if (regionId == 1) return "서울";
        else if(regionId == 2) return "부산";
        else if(regionId == 3) return "대구";
        else if(regionId == 4) return "인천";
        else if(regionId == 5) return "광주";
        else if(regionId == 6) return "대전";
        else if(regionId == 7) return "울산";
        else if(regionId == 8) return "세종";
        else if(regionId == 9) return "경기";
        else if(regionId == 10) return "충북";
        else if(regionId == 11) return "충남";
        else if(regionId == 12) return "전북";
        else if(regionId == 13) return "전남";
        else if(regionId == 14) return "경북";
        else if(regionId == 15) return "경남";
        else if(regionId == 16) return "제주";
        else if(regionId == 17) return "강원";
        else {
            throw new DataNotFoundException(ResponseCode.WRONG_REGION_ID);
        }
    }
}
