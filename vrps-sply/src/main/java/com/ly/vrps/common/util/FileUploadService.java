package com.ly.vrps.common.util;


import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Service
public class FileUploadService {


    private Logger log= LogManager.getLogger(FileUploadService.class);



    @Value("${ImageUpload.server}")
    private String server;



    public JSONObject uploadImage(MultipartFile file) {

        try {
            return  FastDFSUtil.upload(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;


    }
}
