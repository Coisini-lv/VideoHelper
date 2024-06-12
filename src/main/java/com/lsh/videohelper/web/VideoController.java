package com.lsh.videohelper.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @author LvShuaihang
 * @date 2024-06-10 13:23
 * @Requirements:
 */
@RestController
@RequestMapping("video")
public class VideoController {

    @PostMapping("/download/douyin")
    public String downloadDouyin(@RequestParam("url") String url) {

        return "";
    }

    @PostMapping("/download/bilibili")
    public String downloadBilibili() {

        return "";
    }

    @PostMapping("/download/redBook")
    public String downloadRedBoook() {

        return "";
    }
}
