package com.lsh.videohelper.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPath;
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
    public static final Pattern DOUYIN_DATA_PATTERN = Pattern.compile("(?<=script id=\"RENDER_DATA\" type=\"application/json\">).*?(?=</script>)");

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

    public static String downloadDouyinVideo(String douyinUrl) {
        //pc端的抖音视频地址
        //手机端分享抖音视频，正则获取短链url
        HttpResponse response = HttpRequest.get(douyinUrl)
                .header("Referer", douyinUrl)
                .header("cookie", "ttwid=1%7CzZ9DJWUiAOLsMF0CSYkc2VDzFhFWkA9zSZkPrWNs37o%7C1719066750%7C6079c43583db421690bef5ed106d061419744ed5b8db5d087751d9d52c8fc5c4; UIFID_TEMP=1994d5ea02185c59e0ce2f61826e33d5e3041724e0e346378ad909ae2a68ae89b8527bd2f6f51df03e60e995ec72c1931a1761cbaa362e0f753f8c25c259afa0cc4c5eb3357395269e474517366cc6ec; s_v_web_id=verify_lxq7yifu_1REEiBji_Mwpl_48Ww_94P8_XgBM6z8FhQEs; douyin.com; device_web_cpu_core=8; device_web_memory_size=8; dy_swidth=3008; dy_sheight=1269; strategyABtestKey=%221719066751.468%22; csrf_session_id=37afe453900e1f71cbf01919afd7a736; FORCE_LOGIN=%7B%22videoConsumedRemainSeconds%22%3A180%7D; fpk1=U2FsdGVkX19udDL7ZjNz8+pPVkKuZe3GtVb5hn9myw6ony30rFaSOTwvPXxSI0RdxgualLPE9mPbr5SuDoWZCQ==; fpk2=9af1fd1192d005fa6fee32e72c2ccfb4; passport_csrf_token=4f31e9b8287341950d044894462c4680; passport_csrf_token_default=4f31e9b8287341950d044894462c4680; bd_ticket_guard_client_web_domain=2; volume_info=%7B%22isUserMute%22%3Afalse%2C%22isMute%22%3Atrue%2C%22volume%22%3A0.5%7D; d_ticket=2ee402f5814f268d0e0772d5a84a56a53f248; passport_assist_user=CjzDxw80IAuCs_7MZAwrr5ZcUeUWVEnHOJQeYI3NUm0caI7gK85g2_2xolxlmn6P1RQ_mELTfaEU-MoQWGoaSgo8AkaOvWrQ_LpOPmp_93RSfLaKCMePszU5-ahVjJKKpMFhk_ZZyj1ANrecGD8WlkTKWRgqPAplhGzzsm2JEMna1A0Yia_WVCABIgEDVb394Q%3D%3D; n_mh=TrZLiF3uVccgI-M_sp_j_rNKsXfmRl5LTT6IlaUXHIw; sso_uid_tt=8f7de66a0dc54057a1d89aa60cc7317f; sso_uid_tt_ss=8f7de66a0dc54057a1d89aa60cc7317f; toutiao_sso_user=6165839978edac82aa0f047066a0c2f8; toutiao_sso_user_ss=6165839978edac82aa0f047066a0c2f8; sid_ucp_sso_v1=1.0.0-KDgzMjM0NGVjZTU4MGM5NDgwZDBmYzJmNGMzYWM2NWU1YzBkYjFjNzMKHwj779Sq3QIQi9LbswYY7zEgDDD_7cTUBTgGQPQHSAYaAmhsIiA2MTY1ODM5OTc4ZWRhYzgyYWEwZjA0NzA2NmEwYzJmOA; ssid_ucp_sso_v1=1.0.0-KDgzMjM0NGVjZTU4MGM5NDgwZDBmYzJmNGMzYWM2NWU1YzBkYjFjNzMKHwj779Sq3QIQi9LbswYY7zEgDDD_7cTUBTgGQPQHSAYaAmhsIiA2MTY1ODM5OTc4ZWRhYzgyYWEwZjA0NzA2NmEwYzJmOA; passport_auth_status=2482b1dd6c2ea962425f3f0b14ba8c49%2C; passport_auth_status_ss=2482b1dd6c2ea962425f3f0b14ba8c49%2C; uid_tt=bec252984f36b65b6d7d0cdbe7852d87; uid_tt_ss=bec252984f36b65b6d7d0cdbe7852d87; sid_tt=eb3e0d1fcdc837b462e6da27c186d819; sessionid=eb3e0d1fcdc837b462e6da27c186d819; sessionid_ss=eb3e0d1fcdc837b462e6da27c186d819; __ac_nonce=06676e90d004d1644847f; UIFID=1994d5ea02185c59e0ce2f61826e33d5e3041724e0e346378ad909ae2a68ae8954b98bc64dd8f815eb5575e19a72c61a3a22c906ad83b17a31dda0b90fb91613b327c3c8adf780f95c29bc633443b5000e1cd560753cf1e55b88b9653fc051625bbde6136eff599eece600ff765335b51d3688cba10f09d60532022cd2794bee57a427d0f6aa70a280172dd79565beb269c3fa24c26ba6889183b9a5fa37e726; _bd_ticket_crypt_doamin=2; _bd_ticket_crypt_cookie=ff9843ee470dda5d8f1853859a59cfbc; __security_server_data_status=1; sid_guard=eb3e0d1fcdc837b462e6da27c186d819%7C1719068956%7C5183986%7CWed%2C+21-Aug-2024+15%3A09%3A02+GMT; sid_ucp_v1=1.0.0-KGI4MDI1OGYzNDMyYzdiMDUzMTA2YjY1ZGIxN2QyYzhjZWM3NGNjMTAKGQj779Sq3QIQnNLbswYY7zEgDDgGQPQHSAQaAmxmIiBlYjNlMGQxZmNkYzgzN2I0NjJlNmRhMjdjMTg2ZDgxOQ; ssid_ucp_v1=1.0.0-KGI4MDI1OGYzNDMyYzdiMDUzMTA2YjY1ZGIxN2QyYzhjZWM3NGNjMTAKGQj779Sq3QIQnNLbswYY7zEgDDgGQPQHSAQaAmxmIiBlYjNlMGQxZmNkYzgzN2I0NjJlNmRhMjdjMTg2ZDgxOQ; store-region=cn-tj; store-region-src=uid; download_guide=%222%2F20240622%2F0%22; pwa2=%220%7C0%7C1%7C0%22; stream_recommend_feed_params=%22%7B%5C%22cookie_enabled%5C%22%3Atrue%2C%5C%22screen_width%5C%22%3A3008%2C%5C%22screen_height%5C%22%3A1269%2C%5C%22browser_online%5C%22%3Atrue%2C%5C%22cpu_core_num%5C%22%3A8%2C%5C%22device_memory%5C%22%3A8%2C%5C%22downlink%5C%22%3A10%2C%5C%22effective_type%5C%22%3A%5C%224g%5C%22%2C%5C%22round_trip_time%5C%22%3A0%7D%22; FOLLOW_LIVE_POINT_INFO=%22MS4wLjABAAAAq5DBQKHEP_HXq5LgwHhGbi-deJ2egdcDzjQeoBtieGc%2F1719072000000%2F0%2F0%2F1719069915307%22; FOLLOW_NUMBER_YELLOW_POINT_INFO=%22MS4wLjABAAAAq5DBQKHEP_HXq5LgwHhGbi-deJ2egdcDzjQeoBtieGc%2F1719072000000%2F0%2F1719069315307%2F0%22; xg_device_score=7.5090245116023535; IsDouyinActive=true; stream_player_status_params=%22%7B%5C%22is_auto_play%5C%22%3A0%2C%5C%22is_full_screen%5C%22%3A0%2C%5C%22is_full_webscreen%5C%22%3A0%2C%5C%22is_mute%5C%22%3A1%2C%5C%22is_speed%5C%22%3A1%2C%5C%22is_visible%5C%22%3A1%7D%22; home_can_add_dy_2_desktop=%221%22; bd_ticket_guard_client_data=eyJiZC10aWNrZXQtZ3VhcmQtdmVyc2lvbiI6MiwiYmQtdGlja2V0LWd1YXJkLWl0ZXJhdGlvbi12ZXJzaW9uIjoxLCJiZC10aWNrZXQtZ3VhcmQtcmVlLXB1YmxpYy1rZXkiOiJCUHNGVWlhclZsMG5NcU9ESDNyQ0VSbUJwNjk1RTk4MHY2OWhnUXF4ajVoS2Y2K1hXamIxTm5GMWVLVHYxeDZWcWUvTXFHKzVtYm9kY3RXdGJUekpZWUk9IiwiYmQtdGlja2V0LWd1YXJkLXdlYi12ZXJzaW9uIjoxfQ%3D%3D; passport_fe_beating_status=true; msToken=FmhUppp0_Y2LCXlgZTCk-VPuPPh3KoX5zatIafjDMoRKzsLVyPWG_dU8nYdFA8s325Z0024yAmMUkxNrR-j2Y-gMrYHqRfN2fWBINCtNSXGZyI7w6g==; odin_tt=958e1fc38b3ed36c09de65558366b9fab6e3c4283680eefc13a4822b6526288e01115703846812ca671e297ceee28b77; publish_badge_show_info=%220%2C0%2C0%2C1719069331900%22")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36")
                .execute();
        String body= response.body();
        Matcher matcher = DOUYIN_DATA_PATTERN.matcher(body);
        if (matcher.find()) {
            String playAddrContent = matcher.group(0);
            String playAddrContentJson = URLUtil.decode(playAddrContent, Charset.forName("UTF-8"));
            Object src = JSONPath.eval(playAddrContentJson, "$.app.videoDetail.video.playAddr[0].src");
            String videoSrc = "https:" + src.toString();
            File file = HttpUtil.downloadFileFromUrl(videoSrc, new File("/Users/lvshuaihang/IdeaProjects/VideoHelper/download/douyin/douyin.mp4"));

        }
        return "";
    }

    public static void main(String[] args) {
//        getBilibiliVideoDownloadUrl("https://www.bilibili.com/video/BV1Qt421K79o/?vd_source=fc7901758a99adf5bfedfb5369a9d406");
        String s = downloadDouyinVideo("https://www.douyin.com/user/MS4wLjABAAAAeSCEHV_mG7DZqXDewf5gEFHKjSlklKIIsW261HdgFFY?modal_id=7193651991275474237");
    }
}
