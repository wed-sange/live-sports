package org.sports.admin.manage.web.job;

import cn.hutool.core.date.DatePattern;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sports.admin.manage.dao.entity.NewsDO;
import org.sports.admin.manage.dao.repository.INewsDao;
import org.sports.admin.manage.service.service.IPushMessageService;
import org.sports.springboot.starter.cache.DistributedCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.sports.admin.manage.service.constant.CacheConstant.GRAB_NEWS_LOCK_IP;
import static org.sports.admin.manage.service.constant.CacheConstant.GRAB_NEWS_TARGET;

/**
 * @描述: GrabNewsTask 抓取球天下新闻
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/19
 * @创建时间: 16:32
 */
@Component
@Slf4j
public class GrabNewsTask {
    private static final String qtxUrl = "https://www.qtx.com/";
    private static final String leisuUrl = "https://www.leisu.com/news/";
    @Resource
    private INewsDao newsDao;
    @Resource
    private DistributedCache distributedCache;

    @Resource
    private IPushMessageService pushMessageService;

    String[] ua = {"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36 OPR/37.0.2178.32",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586",
            "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
            "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)",
            "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)",
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0)",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 BIDUBrowser/8.3 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36 Core/1.47.277.400 QQBrowser/9.4.7658.400",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 UBrowser/5.6.12150.8 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36 TheWorld 7",
            "Mozilla/5.0 (Windows NT 6.1; W…) Gecko/20100101 Firefox/60.0"};


    /**
     * 每30分钟执行一次
     */
    @Scheduled(initialDelay = 10000, fixedRate = 1000 * 60 * 30)
    private void startTask() {
        try {
            String ip = distributedCache.get(GRAB_NEWS_LOCK_IP);
            String localIp = InetAddress.getLocalHost().getHostAddress();
            if (Objects.isNull(ip) || ip.equals(localIp)) {
                distributedCache.put(GRAB_NEWS_LOCK_IP, localIp, 2, TimeUnit.HOURS);
                String target = distributedCache.get(GRAB_NEWS_TARGET);
                if(Objects.nonNull(target)) {
                    if (qtxUrl.equals(target)) {
                        grabQtxNews(qtxUrl);
                    } else {
                        grabLeisuNews(leisuUrl);
                    }
                }else {
                    try {
                        grabLeisuNews(leisuUrl);
                        distributedCache.put(GRAB_NEWS_TARGET, leisuUrl, 1 ,TimeUnit.DAYS);
                    }catch (Exception e){
                        log.error("抓取雷速新闻失败",e);
                        grabQtxNews(qtxUrl);
                        distributedCache.put(GRAB_NEWS_TARGET, qtxUrl, 1 ,TimeUnit.DAYS);
                    }
                }
                pushMessageService.pushNewsRefresh();
            }
        } catch (IOException e) {
            log.error("本次抓取新闻失败：", e);
        }
    }

    private void grabLeisuNews(String url) throws IOException {
        Document doc = Jsoup.connect(url).timeout(30000)
                .referrer(url)
                .userAgent(getRandomUa())
                .header("Cache-Control", "no-cache")
                .header("Content-Type", "text/html; charset=UTF-8")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("Accept-Encoding", "gzip, deflate, br")
                .maxBodySize(3000000)
                .get();
        Elements baseNewsList = doc.getElementsByClass("news-list");
        baseNewsList.forEach(item -> {
            Elements newsUrls = item.getElementsByTag("a");
            newsUrls.forEach(news -> {
                try {
                    String newsUrl = news.select("a").attr("href");
                    long count = newsDao.getNewsCountByUrl(newsUrl);
                    if (count < 1) {
                        String tournament = null;
                        Elements cname = news.getElementsByClass("cname");
                        if(cname.size()>0){
                            tournament = cname.get(0).text();
                        }
                        LocalDateTime beijingDateTime = LocalDateTime.now(ZoneOffset.UTC);
                        Elements publishTimeElements = news.getElementsByClass("publish-time");
                        if(publishTimeElements.size()>0){
                            String publishTimeStr = publishTimeElements.get(0).text().trim();
                            if(publishTimeStr.contains("分钟")){
                                long minutes = Long.valueOf(publishTimeStr.substring(0,publishTimeStr.indexOf("分钟")));
                                beijingDateTime = LocalDateTime.now(ZoneId.of("Asia/Shanghai")).minusMinutes(minutes);
                            }else if(publishTimeStr.contains("小时")){
                                long hours = Long.valueOf(publishTimeStr.substring(0,publishTimeStr.indexOf("小时")));
                                beijingDateTime = LocalDateTime.now(ZoneId.of("Asia/Shanghai")).minusHours(hours);
                            }else if(publishTimeStr.contains("天")){
                                long days = Long.valueOf(publishTimeStr.substring(0,publishTimeStr.indexOf("天")));
                                beijingDateTime = LocalDateTime.now(ZoneId.of("Asia/Shanghai")).minusDays(days);
                            }
                        }
                        LocalDateTime utcDateTime = beijingDateTime.atZone(ZoneId.of("Asia/Shanghai"))
                                .toOffsetDateTime()
                                .atZoneSameInstant(ZoneOffset.UTC)
                                .toLocalDateTime();
                        storeLeisuNews(newsUrl, tournament, utcDateTime);
                    }
                } catch (IOException e) {
                    log.error("本次抓取新闻失败：", e);
                }
            });
        });

    }
    private String getRandomUa(){
        Random r = new Random();
        return ua[r.nextInt(ua.length)];
    }
    private void storeLeisuNews(String href, String tournament, LocalDateTime utcDateTime) throws IOException {
        Document doc = Jsoup.connect(href).timeout(30000)
                .referrer(href)
                .userAgent(getRandomUa())
                .header("Cache-Control", "no-cache")
                .header("Content-Type", "text/html; charset=UTF-8")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("Accept-Encoding", "gzip, deflate, br")
                .maxBodySize(3000000)
                .get();
        Elements arttitle = doc.getElementsByClass("article-detail");
        if (CollectionUtils.isEmpty(arttitle)) {
            return;
        }
        NewsDO newsDO = new NewsDO();
        arttitle.forEach(item -> {
            newsDO.setTitle(item.getElementsByTag("h1").text());
            newsDO.setPublishTime(utcDateTime);
            Elements elementsContent= item.getElementsByClass("article-content");
            if(elementsContent.size()>0) {
                Element artContent = elementsContent.get(0);
                Elements elementsByTag = artContent.getElementsByTag("img");
                if (elementsByTag.size() > 0) {
                    newsDO.setPic(elementsByTag.get(0).attr("src"));
                }
                String content = elementsContent.html();
                newsDO.setContent(content);
                newsDO.setSourceUrl(href);
                newsDO.setTournament(tournament);
                newsDao.insertNews(newsDO);
            }
        });
    }
    private void grabQtxNews(String url) throws IOException {
        Document doc = Jsoup.connect(url).timeout(30000)
                .referrer(url)
                .userAgent(getRandomUa())
                .header("Cache-Control", "no-cache")
                .header("Content-Type", "text/html; charset=UTF-8")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("Accept-Encoding", "gzip, deflate, br")
                .maxBodySize(3000000)
                .get();
        Elements baseNewsList = doc.getElementsByClass("baseNewsList");
        baseNewsList.forEach(item -> {
            Elements newsUrls = item.getElementsByTag("a");
            newsUrls.forEach(news -> {
                try {
                    String newsUrl = news.select("a").attr("href");
                    long count = newsDao.getNewsCountByUrl(newsUrl);
                    if (count < 1) {
                        storeQtxNews(newsUrl);
                    }
                } catch (IOException e) {
                    log.error("本次抓取新闻失败：", e);
                }
            });
        });

    }
    private void storeQtxNews(String href) throws IOException {
        Document doc = Jsoup.connect(href).timeout(30000)
                 .referrer(href)
                .userAgent(getRandomUa())
                .header("Cache-Control", "no-cache")
                .header("Content-Type", "text/html; charset=UTF-8")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("Accept-Encoding", "gzip, deflate, br")
                .maxBodySize(3000000)
                .get();
        Elements arttitle = doc.getElementsByClass("arttitle");
        if (CollectionUtils.isEmpty(arttitle)) {
            return;
        }
        NewsDO newsDO = new NewsDO();
        arttitle.forEach(item -> {
            newsDO.setTitle(item.getElementsByTag("h1").text());
            String tourInfo = item.getElementsByClass("mes").text();
            tourInfo = tourInfo.replace("发布在", "");
            newsDO.setTournament(tourInfo.substring(0, tourInfo.indexOf(" ")));
            String timeStr = tourInfo.substring(tourInfo.indexOf(" ")).trim();
            if(timeStr.length()<19){
                timeStr =  timeStr +":00";
            }
            LocalDateTime localDateTime = LocalDateTime.parse(timeStr.trim().substring(0, 19).trim(), DatePattern.NORM_DATETIME_FORMATTER);
            LocalDateTime utcDateTime = localDateTime.atZone(ZoneId.of("Asia/Shanghai"))
                    .toOffsetDateTime()
                    .atZoneSameInstant(ZoneOffset.UTC)
                    .toLocalDateTime();
            newsDO.setPublishTime(utcDateTime);
        });
        Element artContent = doc.getElementById("artContent");
        Elements hLabelBox = artContent.getElementsByClass("h_label_box");
        hLabelBox.remove();
        Elements elementsByTag = artContent.getElementsByTag("img");
        if(elementsByTag.size()>0) {
            newsDO.setPic(elementsByTag.get(0).attr("src"));
        }
        String content = artContent.html().replace("球天下","");
        newsDO.setContent(content);
        newsDO.setSourceUrl(href);
        newsDao.insertNews(newsDO);

    }

    public static void main(String[] args) throws IOException {
//        GrabNewsTask grabNewsTask = new GrabNewsTask();
//        grabNewsTask.grabLeisuNews(leisuUrl);

        String tourInfo = "发布在综合  2024-01-22 15:10:26";
        tourInfo = tourInfo.replace("发布在", "");

        String tour = tourInfo.substring(0, tourInfo.indexOf(" "));
        LocalDateTime localDateTime = LocalDateTime.parse(tourInfo.substring(tourInfo.indexOf(" ")).trim().substring(0, 19).trim(), DatePattern.NORM_DATETIME_FORMATTER);
    }
}
