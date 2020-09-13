package com.yang.controller;

import com.yang.enums.VideoStatusEnum;
import com.yang.pojo.Bgm;
import com.yang.service.VideoService;
import com.yang.utils.IMoocJSONResult;
import com.yang.utils.PagedResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author yg
 * @date 2020/8/17 14:34
 */
@Controller
@RequestMapping("video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping("/showAddBgm")
    public String login() {
        return "video/addBgm";
    }

    @PostMapping("/addBgm")
    @ResponseBody
    public IMoocJSONResult addBgm(Bgm bgm) {
        videoService.addBgm(bgm);
        return IMoocJSONResult.ok();
    }

    @GetMapping("/showBgmList")
    public String showBgmList() {
        return "video/bgmList";
    }

    @PostMapping("/queryBgmList")
    @ResponseBody
    public PagedResult queryBgmList(Integer page) {
        return videoService.queryBgmList(page, 10);
    }

    @PostMapping("/delBgm")
    @ResponseBody
    public IMoocJSONResult deleteBgm(String bgmId) {
        videoService.deleteBgm(bgmId);
        return IMoocJSONResult.ok();
    }

    @PostMapping("/bgmUpload")
    @ResponseBody
    public IMoocJSONResult bgmUpload(@RequestParam("file") MultipartFile[] files) throws IOException {
        // 文件保存的命名空间
        String filePath = "D:\\workspace\\workspace_03\\video_dev\\bgm";
        // 保存到数据库中的相对路径
        String uploadPathDB = File.separator + "bgm";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            if (files != null && files.length > 0) {

                String fileName = files[0].getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)) {
                    // 文件上传的最终保存路径
                    String finalPath = filePath + uploadPathDB + File.separator + fileName;
                    // 设置数据库保存的路径
                    uploadPathDB += (File.separator + fileName);

                    File outFile = new File(finalPath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        // 创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = files[0].getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }

            } else {
                return IMoocJSONResult.errorMsg("上传出错...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return IMoocJSONResult.errorMsg("上传出错...");
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        return IMoocJSONResult.ok(uploadPathDB);
    }

    @GetMapping("/showReportList")
    public String showReportList() {
        return "video/reportList";
    }

    @PostMapping("/reportList")
    @ResponseBody
    public PagedResult reportList(Integer page) {

        PagedResult result = videoService.queryReportList(page, 10);
        return result;
    }

    @PostMapping("/forbidVideo")
    @ResponseBody
    public IMoocJSONResult forbidVideo(String videoId) {

        videoService.updateVideoStatus(videoId, VideoStatusEnum.FORBID.value);
        return IMoocJSONResult.ok();
    }
}
