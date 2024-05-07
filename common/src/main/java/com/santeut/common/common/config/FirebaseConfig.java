package com.santeut.common.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.santeut.common.common.exception.FirebaseSettingFailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

   @Autowired
   private ResourceLoader resourceLoader;

   @Value("${FIREBASE_CONFIG_PATH}")
   private String firebaseConfigPath;

   @Bean
   FirebaseMessaging firebaseMessaging() {

       try {
           FirebaseApp firebaseApp = null;

           InputStream serviceAccount = new FileInputStream(firebaseConfigPath);

           FirebaseOptions options = new FirebaseOptions.Builder()
                   .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                   .setDatabaseUrl("firebase-adminsdk-j1t3w@santeut-ec5e6.iam.gserviceaccount.com")
                   .build();

           firebaseApp = FirebaseApp.initializeApp(options);
           return FirebaseMessaging.getInstance(firebaseApp);
       } catch(IOException e) {
           e.printStackTrace();
           throw new FirebaseSettingFailException("Firebase 설정이 잘못되었습니다.");
       }

   }

}
