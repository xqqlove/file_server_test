package com.yiwa.file_server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 文件上传
 */
@RestController
public class FileController {
    private static final Logger log=  LoggerFactory.getLogger(FileController.class);

    /**
     * 单文件上传
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload")
    public String upload(@RequestParam("file")MultipartFile file){
        try {
            if (file.isEmpty()) return "file is empty";
            String fileName=file.getOriginalFilename();
            String suffixName=fileName.substring(fileName.lastIndexOf("."));
            log.info("上传文件名为："+fileName+" 后缀名为"+suffixName);
            //设置文件存储路径，
            String filePath="D:\\fileServer\\";
            String path=filePath+fileName;
            File dest=new File(path);
            if (!dest.getParentFile().exists()){
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
            return "upload success";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "upload failure";
    }

    @PostMapping("/batch")
    public String handleFileUpload(HttpServletRequest request){
        List<MultipartFile> files=((MultipartHttpServletRequest)request).getFiles("file");
        MultipartFile file=null;
        BufferedOutputStream out=null;
        for (int i=0;i<files.size();i++){
            file=files.get(i);
            String filePath="D:\\fileServer\\";
            if (!file.isEmpty()){
                try {
                    byte[] bytes=file.getBytes();
                    out=new BufferedOutputStream(new FileOutputStream(new File(filePath+file.getOriginalFilename())));
                    out.write(bytes);
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "file upload failure";
                }
            }else {
                return "file is empty";
            }
        }
        return "upload Multifile success";
    }

}
