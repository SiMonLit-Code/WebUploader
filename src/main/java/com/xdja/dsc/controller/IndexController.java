package com.xdja.dsc.controller;

import com.xdja.dsc.enums.ExceptionEnum;
import com.xdja.dsc.exception.ServiceException;
import com.xdja.dsc.utils.AesSecret;
import com.xdja.dsc.vo.Person;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.sun.deploy.cache.Cache.exists;


/**
 * @author Created by niugang on 2019/1/24/11:42
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
@Controller
@Validated
@Slf4j
public class IndexController {


    private static String FILENAME = "";

    private static String FILEPATH = "";


    @Value("${xdja.upload.file.path}")
    private String decryptFilePath;

    @Value("${xdja.upload.file.path.temp}")
    private String decryptFilePathTemp;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/webuploader")
    public String webuploader() {
        return "webupload";
    }

    @GetMapping("downloaderror")
    public String downloaderror() {
        return "error";
    }

    /**
     * 分片上传
     *
     * @return ResponseEntity<Void>
     */
    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<Void> decrypt(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file, Integer chunks, Integer chunk, String name, String guid) throws IOException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            if (file == null) {
                throw new ServiceException(ExceptionEnum.PARAMS_VALIDATE_FAIL);
            }
            System.out.println("guid:" + guid);
            if (chunks == null && chunk == null) {
                chunk = 0;
            }

            File dirs=new File(decryptFilePath);
            if (!dirs.exists()){
                dirs.mkdirs();
            }
            //创建碎片文件
            File outFile = new File(decryptFilePathTemp+File.separator+guid, chunk + ".part");
            if ("".equals(FILENAME)) {
                FILENAME = name;
            }
            InputStream inputStream = file.getInputStream();
            FileUtils.copyInputStreamToFile(inputStream, outFile);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 合并所有分片
     *
     * @throws Exception Exception
     */
    @GetMapping("/merge")
    @ResponseBody
    public void byteMergeAll(String guid) throws Exception {
        System.out.println("merge:"+guid);
        File dirs = new File(decryptFilePathTemp);
        if (!dirs.exists()){
            dirs.mkdirs();
        }
        File file = new File(decryptFilePathTemp+File.separator+guid);
        if (file.isDirectory()) {
            File[] files = file.listFiles();//获取所有碎片
            if (files != null && files.length > 0) {
                File partFile = new File(decryptFilePath + File.separator + FILENAME);
                for (int i = 0; i < files.length; i++) {
                    File s = new File(decryptFilePathTemp+File.separator+guid, i + ".part");
                    FileOutputStream destTempfos = new FileOutputStream(partFile, true);
                    FileUtils.copyFile(s,destTempfos );
                    destTempfos.close();
                }
                FileUtils.deleteDirectory(file);
                FILENAME = "";
            }

        }



    }

    /**
     * 非分片上传
     *
     * @param request request
     * @param file    file
     * @return ResponseEntity<Void>
     * @throws IOException IOException
     */
    @PostMapping("/oldupload")
    @ResponseBody
    public ResponseEntity<Void> decrypt(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter yyyymMddHHmmSS = DateTimeFormatter.ofPattern("YYYYMMddHHmmSS");
        String format = now.format(yyyymMddHHmmSS);
        String path = decryptFilePath + File.separator + format;
        File fileDirty = new File(path);
        if (!fileDirty.exists()) {
            fileDirty.mkdirs();
        }
        FILEPATH=path + File.separator + file.getOriginalFilename();
        File outFile = new File(FILEPATH);
        request.setAttribute("filePath",FILEPATH);
        FileUtils.copyInputStreamToFile(file.getInputStream(), outFile);
        return ResponseEntity.ok().build();
    }



}
