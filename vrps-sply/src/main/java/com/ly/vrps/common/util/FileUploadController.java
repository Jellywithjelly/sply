package com.ly.vrps.common.util;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/upload/file")
public class FileUploadController {


    @Autowired
    private FileUploadService service;

    @PostMapping("/save")
    @ResponseBody
    public JSONObject save(@RequestParam("file") MultipartFile file) {
        try {
            return  service.uploadImage(file);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

}
