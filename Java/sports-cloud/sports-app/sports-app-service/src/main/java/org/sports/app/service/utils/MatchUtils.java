package org.sports.app.service.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.util.Strings;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.service.enums.BasketballStatusType;
import org.sports.admin.manage.service.enums.FootballStatusType;
import org.sports.admin.manage.service.vo.SdkMatchVo;
import org.sports.app.service.vo.MatchVO;
import org.sports.app.service.vo.live.BasketballPlayerDataStatsVO;
import org.sports.app.service.vo.live.BasketballPlayerStatsVO;
import org.sports.app.service.vo.live.IncidentsVO;
import org.sports.app.service.vo.live.MatchLineupDetailVO;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @描述: MatchSortUtil  比赛排序工具类
 * @版权: Copyright (c) 2023
 * @公司:

 * @版本: 1.0
 * @创建日期: 2023/9/21
 * @创建时间: 15:54
 */
public class MatchUtils {
    public static List<BasketballPlayerStatsVO> coverPlayStats(List<Object> list) {
        return list.stream().map(o -> {
                    List<Object> playInfo = (List<Object>) o;
                    BasketballPlayerStatsVO playerStatsVO = new BasketballPlayerStatsVO();
                    playerStatsVO.setId((Integer) playInfo.get(0));
                    String chineseName = playInfo.get(1).toString();
                    if (StrUtil.isNotBlank(chineseName)) {
                        playerStatsVO.setName(chineseName);
                    } else {
                        playerStatsVO.setName(playInfo.get(3).toString());
                    }
                    //名称缩写
                    playerStatsVO.setName(handlePlayerName(playerStatsVO.getName()));
                    playerStatsVO.setLogo(playInfo.get(4).toString());
                    playerStatsVO.setNumber(playInfo.get(5).toString());
                    ArrayList<String> dataList = CollUtil.toList(playInfo.get(6).toString().split("\\^"));
                    playerStatsVO.setData(new BasketballPlayerDataStatsVO(dataList));
                    return playerStatsVO;
                })
                .collect(Collectors.toList());
    }

    /**
     * 名称缩写处理
     *
     * @param name
     * @return
     */
    private static String handlePlayerName(String name) {
        if (Strings.isBlank(name)) {
            return null;
        }
        if (name.contains("·")) {
            name = name.substring(name.lastIndexOf("·") + 1);
        }
        if (name.contains("-")) {
            name = name.substring(name.lastIndexOf("-") + 1);
        }
        return name;
    }

    public static void main(String[] args) {
        String name = "凯尔.喀什-嘻嘻";
        System.out.println("args = " + handlePlayerName(name));
    }

    public static List<SdkMatchVo> sortMatch(List<SdkMatchVo> filterList) {
        List<SdkMatchVo> resultList = Lists.newArrayList(); //返回结果集
        List<SdkMatchVo> upComingtList = Lists.newArrayList();//未开始
        List<SdkMatchVo> ongoingList = Lists.newArrayList();//除了未开始和已完成的都排前面
        List<SdkMatchVo> finishedList = Lists.newArrayList();//已完成
        List<SdkMatchVo> errorList = Lists.newArrayList();//异常
        filterList.forEach(matchInfo -> {
            if (Objects.nonNull(matchInfo)) {
                if (MatchType.BASKETBALL.equals(matchInfo.getMatchType())) {
                    if (BasketballStatusType.NOT_START.getCode().equals(matchInfo.getStatusId())) {
                        upComingtList.add(matchInfo);
                    } else if (BasketballStatusType.FINISHED.getCode().equals(matchInfo.getStatusId())) {
                        finishedList.add(matchInfo);
                    } else if (BasketballStatusType.getRunningStatus().contains(matchInfo.getStatusId())) {
                        ongoingList.add(matchInfo);
                    } else {
                        errorList.add(matchInfo);
                    }
                } else if (MatchType.FOOTBALL.equals(matchInfo.getMatchType())) {
                    if (FootballStatusType.NOT_START.getCode().equals(matchInfo.getStatusId())) {
                        upComingtList.add(matchInfo);
                    } else if (FootballStatusType.FINISHED.getCode().equals(matchInfo.getStatusId())) {
                        finishedList.add(matchInfo);
                    } else if (FootballStatusType.getRunningStatus().contains(matchInfo.getStatusId())) {
                        ongoingList.add(matchInfo);
                    } else {
                        errorList.add(matchInfo);
                    }
                }
            }
        });

        //对结果进行排序
        upComingtList.sort(Comparator.comparing(SdkMatchVo::getMatchTime).thenComparing(SdkMatchVo::getId));//正序
        ongoingList.sort(Comparator.comparing(SdkMatchVo::getMatchTime).thenComparing(SdkMatchVo::getId));//正序
        finishedList.sort(Comparator.comparing(SdkMatchVo::getMatchTime).reversed().thenComparing(SdkMatchVo::getId));//倒序
        errorList.sort(Comparator.comparing(SdkMatchVo::getMatchTime).reversed().thenComparing(SdkMatchVo::getId));//倒序
        resultList.addAll(ongoingList);
        resultList.addAll(upComingtList);
        resultList.addAll(finishedList);
        resultList.addAll(errorList);
        return resultList;
    }


    /**
     * `
     * 比分字段说明
     * example：[1, 0, 0, 0, -1, 0, 0]
     * 0:"比分(常规时间) - int"
     * 1:"半场比分 - int"
     * 2:"红牌 - int"
     * 3:"黄牌 - int"
     * 4:"角球，-1表示没有角球数据 - int"
     * 5:"加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int"
     * 6:"点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"
     *
     * @param vo         比赛对象
     * @param homeScores 主队得分
     * @param awayScores 客队得分
     */
    public static void setFootballScore(MatchVO vo, List<Object> homeScores, List<Object> awayScores) {
        if (homeScores.size() != 7 || awayScores.size() != 7) {
            return;
        }
        int homeScore = (int) homeScores.get(0);
        int homeExtraScore = (int) homeScores.get(5);
        int homePKScore = (int) homeScores.get(6);
        int awayScore = (int) awayScores.get(0);
        int awayExtraScore = (int) awayScores.get(5);
        int awayPkScore = (int) awayScores.get(6);
        if (homeExtraScore > 0) {
            vo.setHomeScore(homeExtraScore + homePKScore);
        } else {
            vo.setHomeScore(homeScore + homePKScore);
        }
        if (awayExtraScore > 0) {
            vo.setAwayScore(awayExtraScore + awayPkScore);
        } else {
            vo.setAwayScore(awayScore + awayPkScore);
        }
        vo.setHomeHalfScore((Integer) homeScores.get(1));
        vo.setAwayHalfScore((Integer) awayScores.get(1));

    }

    /**
     * 设置比赛半场比分，当前比分，当前进行阶段
     * 比分字段说明
     * example：[0, 0, 0, 0, 0]
     * Enum: Array[5]
     * 0:"第1节分数 - int"
     * 1:"第2节分数 - int"
     * 2:"第3节分数 - int"
     * 3:"第4节分数 - int"
     * 4:"加时分数 - int"
     *
     * @param vo         比赛对象
     * @param homeScores 主队得分
     * @param awayScores 客队得分
     */
    public static void setBasketballScore(MatchVO vo, List<Object> homeScores, List<Object> awayScores) {
        int totalScore = 0;
        int halfScore = 0;
        for (int i = 0; i < homeScores.size(); i++) {
            int score = (int) homeScores.get(i);
            if (i < 2) {
                halfScore += score;
            }
            totalScore += score;
        }
        vo.setHomeScore(totalScore);
        vo.setHomeHalfScore(halfScore);
        totalScore = 0;
        halfScore = 0;
        for (int i = 0; i < awayScores.size(); i++) {
            int score = (int) awayScores.get(i);
            if (i < 2) {
                halfScore += score;
            }
            totalScore += score;
        }
        vo.setAwayScore(totalScore);
        vo.setAwayHalfScore(halfScore);
    }

    public static void setPlayerShortName(MatchLineupDetailVO matchLineupDetailVO) {
        if (Objects.isNull(matchLineupDetailVO)) {
            return;
        }
        matchLineupDetailVO.getHome().forEach(item -> item.setName(handlePlayerName(item.getName())));
        matchLineupDetailVO.getAway().forEach(item -> item.setName(handlePlayerName(item.getName())));
    }

    public static void setPlayerShortName(List<IncidentsVO> incidentsVOS) {
        if (CollectionUtil.isEmpty(incidentsVOS)) {
            return;
        }
        incidentsVOS.forEach(item -> {
            item.setPlayerName(handlePlayerName(item.getPlayerName()));
            item.setAssist1Name(handlePlayerName(item.getAssist1Name()));
            item.setAssist2Name(handlePlayerName(item.getAssist2Name()));
            item.setInPlayerName(handlePlayerName(item.getInPlayerName()));
            item.setOutPlayerName(handlePlayerName(item.getOutPlayerName()));
        });
    }
}
