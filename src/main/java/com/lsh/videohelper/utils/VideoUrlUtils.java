package com.lsh.videohelper.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.lsh.videohelper.entity.dto.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:
 * 用于接收分享、PC连接。并且最终的到视频地址
 *
 * @author LvShuaihang
 * @date 2024-06-10 13:32
 * @Requirements:
 */
public class VideoUrlUtils {
    public static final Pattern BILIBILI_DATA_PATTERN = Pattern.compile("(?<=<script>window.__playinfo__=).*?(?=</script)");
    //用到http的
    //http的连接池复用

    /**
     * 通过用户传入的地址，拿到真实的视频播放地址
     *
     * @param url
     * @return
     */
    public String getVideoPlayUrl(String url) {
        //判断哪个平台

        //分享链接还是PC访问链接

        //得到真正的视频播放链接

        return "";
    }

    /**
     * 1.给你提供B站的视频+音频URL：还提供一个，他用python获取他语言写好的格式化工具
     * 2.
     *
     * @return
     */
    public static String getBilibiliVideoDownloadUrl(String bilibiliUrl) {
        String html = HttpUtil.get(bilibiliUrl, Charset.forName("UTF-8"));

        Matcher matcher = BILIBILI_DATA_PATTERN.matcher(html);
        if (matcher.find()) {
            String group = matcher.group();
            System.out.println(group);
            BilibiliVideoDataDto bilibiliVideoDataDto = JSONObject.parseObject(group, BilibiliVideoDataDto.class);
            Data data = bilibiliVideoDataDto.getData();
            Dash dash = data.getDash();
            List<Video> videos = dash.getVideo();
            List<Audio> audios = dash.getAudio();
            String videoUrl = videos.get(0).getBaseUrl();
            String audioUrl = audios.get(0).getBaseUrl();
            File videoFile = null;
            File audioFile = null;

            if (StrUtil.isAllNotEmpty(videoUrl, audioUrl)) {
                HttpResponse responseVideo = HttpRequest.get(videoUrl)
                        .header("Referer", bilibiliUrl)
                        .header("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36")
                        .execute();
                InputStream inputStreamVideo = responseVideo.bodyStream();

                HttpResponse responseAudio = HttpRequest.get(audioUrl)
                        .header("Referer", bilibiliUrl)
                        .header("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36")
                        .execute();
                InputStream inputStreamAudio = responseAudio.bodyStream();

                videoFile = FileUtil.writeFromStream(inputStreamVideo, new File("/Users/lvshuaihang/IdeaProjects/VideoHelper/download/video.m4s"));
//                audioFile = HttpUtil.downloadFileFromUrl(audioUrl, new File("/Users/lvshuaihang/IdeaProjects/VideoHelper/download/audio.m4s"));
                audioFile = FileUtil.writeFromStream(inputStreamAudio, new File("/Users/lvshuaihang/IdeaProjects/VideoHelper/download/audio.m4s"));

                try {
                    ProcessBuilder pb = new ProcessBuilder("/opt/homebrew/bin/ffmpeg",
                            "-i", "/Users/lvshuaihang/IdeaProjects/VideoHelper/download/video.m4s",
                            "-i", "/Users/lvshuaihang/IdeaProjects/VideoHelper/download/audio.m4s",
                            "-codec", "copy", "/Users/lvshuaihang/IdeaProjects/VideoHelper/download/video.mp4");
                    Process p = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    FileUtil.del(videoFile);
                    FileUtil.del(audioFile);
                }
            }
        } else {
            System.err.println("未匹配到视频信息，退出程序！");
        }
        return "";
    }

    public static void main(String[] args) {
        getBilibiliVideoDownloadUrl("https://www.bilibili.com/video/BV1Qt421K79o/?vd_source=fc7901758a99adf5bfedfb5369a9d406");

    }
}
