package com.ly.vrps.common.util;

import net.sf.json.JSONObject;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FastDFSUtil {

   /* public static void main(String[] args) {
        upload();
    }*/

    public static JSONObject upload(MultipartFile file) throws IOException {
        JSONObject object = new JSONObject();
        StorageClient sc = null;
        try {
            File toFile = MultipartFileToFile(file);
            String local_fileName = toFile.getAbsolutePath().replace(toFile.separator,"/");
            String file_ext_nmme = local_fileName.substring(local_fileName.lastIndexOf(".") + 1);

            /*读取配置文件*/
            ClientGlobal.init("fastdfs.conf");
            TrackerClient tc = new TrackerClient();
            TrackerServer trackerServer = tc.getTrackerServer();
            StorageServer storeStorage = tc.getStoreStorage(trackerServer);
            /*定义storage的客户端对象，需要使用这个对象完成文件的上传和下载*/
            sc = new StorageClient(trackerServer, storeStorage);
            String[] result = sc.upload_file(local_fileName, file_ext_nmme, null);
            object.put("falg",true);
            String path = "";
            for (int i = 0;i < result.length;i++){

                if(i == result.length-1){
                    path += result[i];
                }else{
                    path += result[i]+"/" ;
                }
            }
            object.put("path",path);
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
            object.put("falg",false);
            sc.close();
        } catch (MyException e) {
            e.printStackTrace();
            object.put("falg",false);
            sc.close();
        }
        return  object;

    }

    public static File MultipartFileToFile(MultipartFile multiFile) {
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若需要防止生成的临时文件重复,可以在文件名后添加随机码

        try {
            File file = File.createTempFile(fileName, prefix);
            multiFile.transferTo(file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
