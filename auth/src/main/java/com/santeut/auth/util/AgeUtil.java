package com.santeut.auth.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class AgeUtil {

    public int calculateAge(String birth){

        LocalDate currentDate = LocalDate.now();

        int currentYear = currentDate.getYear();

        birth = birth.substring(4);
        int userBirth = Integer.parseInt(birth);

        return currentYear-userBirth+1;
    }
}
