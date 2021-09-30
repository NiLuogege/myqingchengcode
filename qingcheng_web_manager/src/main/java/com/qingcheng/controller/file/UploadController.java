package com.qingcheng.controller.file;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private OSSClient ossClient;

    @PostMapping("/native")
    public String nativeUpload(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("native......");
        //找到真正要存储的位置
        String path = request.getSession().getServletContext().getRealPath("img");
        String filePath = path + File.separator + file.getOriginalFilename();
        File desFile = new File(filePath);
        if (!desFile.getParentFile().exists()) {
            desFile.mkdirs();
        }
        file.transferTo(desFile);
        return "http://localhost:8989/img/" + file.getOriginalFilename();
    }

    @PostMapping("/oss")
    public String ossUpload(@RequestParam("file") MultipartFile file,String floder) throws IOException {
        String bucketName="qingchengdianshang";
        String fileName = floder+File.separator+ UUID.randomUUID()+file.getOriginalFilename();
        ossClient.putObject(bucketName,fileName,file.getInputStream());
        return "https://"+bucketName+".oss-cn-beijing.aliyuncs.com/"+fileName;
    }

}
