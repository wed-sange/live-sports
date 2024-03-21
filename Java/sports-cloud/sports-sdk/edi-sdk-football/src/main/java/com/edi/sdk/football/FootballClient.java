package com.edi.sdk.football;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.edi.commons.model.vo.PageVo;
import com.edi.sdk.core.EdiClient;
import com.edi.sdk.core.EdiResponse;
import com.edi.sdk.football.domain.*;
import com.edi.sdk.football.request.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 2023年6月26日 下午5:01:20
 */
public class FootballClient extends EdiClient {
    public FootballClient() {
        super();
    }

    public FootballClient(String user, String secret) {
        super(user, secret);
    }

    public FootballClient(String user, String secret, String baseUrl) {
        super(user, secret, baseUrl);
    }

    // =========================== 资料库数据包 开始 =============================

    /**
     * 获取比赛列表
     * 返回全量比赛数据，可根据时间戳增量获取新增或变动的数据
     * <p>
     * 1、首次全量更新，根据参数id获取全量数据
     * 2、后续增量更新，根据参数time增量获取变动数据（建议请求频次：1min/次）
     */
    public EdiResponse<List<Match>> matchList(MatchListRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<Match>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取赛程赛果列表-赛季查询
     * 该接口返回查询赛季的全量赛程赛果数据
     * 请求次数：120次/min
     */
    public EdiResponse<List<Match>> matchScheduleSeaSon(MatchScheduleSeaSonRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<Match>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取赛季积分榜数据
     * 返回查询赛季的积分榜数据详情
     * 请求次数：120次/min
     */
    public EdiResponse<SeasonTableDetail> seasonTableDetail(SeasonTableDetailRequest request) {
        String execute = execute(request);
        return JSON.parseObject(execute, new TypeReference<EdiResponse<SeasonTableDetail>>() {
        });
    }

    /**
     * 获取赛季统计详情
     * 返回查询赛季的球队球员统计详情数据（球员统计、射手榜、球队统计）
     * 请求次数：120次/min
     * <p>
     * 说明：
     * 由于球员存在赛事内转会情况，球员统计中会存在一个球员有多条数据，所在球队不同的情况，表示该球员在不同球队的数据，可合并处理
     */
    public EdiResponse<SeasonStatsDetail> seasonStatsDetail(SeasonStatsDetailRequest request) {
        String execute = execute(request);
        return JSON.parseObject(execute, new TypeReference<EdiResponse<SeasonStatsDetail>>() {
        });
    }


    /**
     * 获取打包数据地址
     * 返回全量历史比赛的打包数据地址（比赛详情、阵容、指数、比赛球员统计）
     */
    public EdiResponse<Archive> archive(ArchiveRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<Archive>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    // =========================== 资料库数据包 结束 =============================

    // =========================== 实时数据包 开始 =============================

    /**
     * 获取变动比赛列表
     * 用于获取比赛变动后的数据（比赛时间、状态等），可根据时间戳增量获取新增或变动的数据
     * 限制：前30天比赛开始
     * <p>
     * 1、首次全量更新，根据参数id获取全量数据
     * 2、后续增量更新，根据参数time增量获取变动数据（建议请求频次：1min/次）
     */
    public EdiResponse<List<RecentMatch>> recentMatchList(RecentMatchListRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<RecentMatch>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取赛程赛果列表-日期查询
     * 该接口返回所请求日期的全量赛程赛果数据（请求限制：前后30天）
     * 注：实时数据的获取通过实时统计数据接口
     * <p>
     * 当天赛程 建议请求频次：10min/次（全量更新）
     * 未来赛程 建议请求频次：30min/次（全量更新）
     */
    public EdiResponse<MatchScheduleDiary> matchScheduleDiary(MatchScheduleDiaryRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<MatchScheduleDiary>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取赛程赛果列表-维度查询
     * 该接口返回维度查询对应的全量赛程赛果数据（赛事-最新赛季赛程、体彩-最近72h内彩种赛程）
     * 请求次数：120次/min
     */
    public EdiResponse<List<MatchScheduleParam>> scheduleParam(MatchScheduleParamRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<MatchScheduleParam>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取实时统计数据
     * 返回最近120min内的比赛事件、技术统计、文字直播数据（全量更新）
     * ps：非120min内的比赛数据有更新，也会同步返回
     * 建议请求频次：2s/次
     * <p>
     * 比赛进行分钟数获取公式
     * 上半场：比赛进行分钟数=(当前时间戳-上半场开球时间戳) / 60 + 1
     * 下半场：比赛进行分钟数=(当前时间戳-下半场开球时间戳) / 60 + 45 + 1
     * <p>
     * stats 比赛统计字段说明
     * 包含：角球、黄牌、红牌、点球、射正、射偏、进攻、危险进攻、控球率
     * <p>
     * incidents 比赛事件字段说明
     * 包含：黄牌、两黄变红、红牌、进球(助攻)、换人、点球、点球未进、乌龙球、VAR、中场、伤停补时、结束、加时结束、点球大战结束
     * <p>
     * tlive 文字直播字段说明
     * 包含：黄牌、红牌、进球、换人、角球、越位、助攻、比赛开始、中场、结束等
     */
    public EdiResponse<List<MatchLive>> matchLive(MatchLiveRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<MatchLive>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取比赛趋势详情
     * 返回比赛主客队趋势详情数据
     * <p>
     * 主队为正、客队为负，按分钟数变化（有加时赛，加时分钟数变化加在下半场中）
     * 请求限制：前30天比赛
     * <p>
     * 请求次数：120次/min
     */
    public EdiResponse<MatchTrendDetail> matchTrendDetail(MatchTrendDetailRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<MatchTrendDetail>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取比赛阵容详情
     * 返回单场比赛阵容数据（根据“赛程赛果接口”中“是否有阵容”字段来判断是否调用该接口）
     * 请求次数：120次/min
     * <p>
     * 坐标说明：
     * 主队坐标原点：左上；即：x轴方向向右，y轴方向向下；
     * 客队坐标原点：右下；即：x轴方向向左，y轴方向向上。
     * <p>
     * 含球员事件，请求限制：前30天比赛
     */
    public EdiResponse<MatchLineupDetail> matchLineupDetail(MatchLineupDetailRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<MatchLineupDetail>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取比赛球队统计列表
     * 返回最近10min内球队统计数据变动比赛的球队统计数据（全量更新）
     * <p>
     * 建议请求频次：1~5min/次
     */
    public EdiResponse<List<MatchTeamStats>> matchTeamStatsList(MatchTeamStatsListRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<MatchTeamStats>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取比赛球员统计列表
     * 返回最近10min内球员统计数据变动的比赛的球员统计数据（全量更新）
     * <p>
     * 建议请求频次：1~5min/次
     */
    public EdiResponse<List<MatchPlayerStats>> matchPlayerStatsList(MatchPlayerStatsListRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<MatchPlayerStats>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取版权比赛直播地址
     * 返回未开赛的版权比赛（24h内）版权方的直播地址
     * <p>
     * 建议请求频次：10min/次
     */
    public EdiResponse<List<MatchStreamUrlsFree>> matchStreamUrlsFree(MatchStreamUrlsFreeRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<MatchStreamUrlsFree>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取版权比赛集锦录像地址
     * 返回版权比赛版权方的集锦/录像地址，‘版权比赛直播地址’api中的比赛可能会有集锦/录像（开赛后获取）
     * 请求次数：120次/min
     */
    public EdiResponse<List<MatchStreamVideoCollection>> matchStreamVideoCollection(MatchStreamVideoCollectionRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<MatchStreamVideoCollection>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取实时积分榜数据
     * 返回最近10min内实时变动的赛季积分榜数据（全量更新）
     * <p>
     * 建议请求频次：1~5min/次
     */
    public EdiResponse<List<TableLive>> tableLive(TableLiveRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<TableLive>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取删除数据
     * 返回最近24小时内删除的数据id（比赛、球队、球员、赛事、赛季、阶段），需定时同步
     * <p>
     * 建议请求频次：1~5min/次
     */
    public EdiResponse<Deleted> deleted(DeletedRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<Deleted>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    // =========================== 实时数据包 结束 =============================

    // =========================== 体彩数据包 结束 =============================

    /**
     * 获取体彩比赛关联列表
     * 该接口实现：“竞彩、北单、传统足彩 指数接口”中比赛与“赛程赛果接口”中比赛的关联
     * 建议请求频次：10~30min/次
     * <p>
     * 彩种说明
     * 竞彩：
     * 101-竞彩足球
     * 201-竞彩篮球
     * 北单：
     * 301-北单胜负过关
     * 404-北单让球胜平负
     * 传统足彩：
     * 401-14场胜负/任选9场
     * 402-半全场
     * 403-进球彩
     * <p>
     * 使用说明
     * 1.根据match_id（纳米比赛id）与“赛程赛果接口”中的比赛相关联
     * 2.判断lottery_type，然后根据id（每种彩种下唯一，用于关联）和“竞彩、北单、传统足彩 指数接口”中比赛相关联
     * 从而实现比赛数据和彩票数据的关联
     */
    public EdiResponse<List<LotteryMatch>> lotteryMatchList(LotteryMatchListRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<List<LotteryMatch>>>() {
        });
    }

    /**
     * 获取竞彩比赛指数
     * 获取竞彩足球和竞彩篮球比赛的指数
     * 建议请求频次：3~5min/次
     */
    public EdiResponse<JcOdds> jcOdds(JcOddsRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<JcOdds>>() {
        });
    }

    /**
     * 获取竞彩比赛开奖结果
     * 获取近两天（含当日）的竞彩足球和竞彩篮球比赛的开奖结果
     * 建议请求频次：5~10min/次
     */
    public EdiResponse<JcResult> jcResult(JcResultRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<JcResult>>() {
        });
    }

    /**
     * 获取北单指数
     * 获取北京单场比赛的指数(不含胜负过关)
     * 建议请求频次：3~5min/次
     */
    public EdiResponse<List<BdOdds>> bdOdds(BdOddsRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<List<BdOdds>>>() {
        });
    }

    /**
     * 获取北单开奖结果
     * 获取当前一期北京单场开奖结果(不含胜负过关)
     * 建议请求频次：5~10min/次
     */
    public EdiResponse<List<BdResult>> bdResult(BdResultRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<List<BdResult>>>() {
        });
    }

    /**
     * 获取北单胜负过关指数
     * 获取北京单场比赛胜负过关的指数（提供足篮球比赛）
     * 建议请求频次：3~5min/次
     */
    public EdiResponse<List<BdSfOdds>> bdSfOdds(BdSfOddsRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<List<BdSfOdds>>>() {
        });
    }

    /**
     * 获取北单胜负过关开奖结果
     * 获取获取近俩期的北京单场胜负过关开奖结果（提供足篮球比赛）
     * 建议请求频次：5~10min/次
     */
    public EdiResponse<List<BdSfResult>> bdSfResult(BdSfResultRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<List<BdSfResult>>>() {
        });
    }

    /**
     * 获取传统足彩赛程
     * 建议请求频次：10~30min/次
     */
    public EdiResponse<List<ZcMatches>> zcMatches(ZcMatchesRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<List<ZcMatches>>>() {
        });
    }

    /**
     * 获取传统足彩开奖结果
     * 该接口用于获取传统足彩近20期的开奖结果
     * 建议请求频次：10~30min/次
     */
    public EdiResponse<List<ZcResult>> zcResult(ZcResultRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<List<ZcResult>>>() {
        });
    }

    // =========================== 体彩数据包 结束 =============================


    // =========================== 高阶数据包 开始 =============================

    /**
     * 获取情报列表
     * 返回全量比赛情报数据，可根据时间戳增量获取新增或变动的数据
     * <p>
     * 1、首次全量更新，根据参数id获取全量数据
     * 2、后续增量更新，根据参数time增量获取变动数据（建议请求频次：1min/次
     */
    public EdiResponse<List<Intelligence>> intelligence(IntelligenceListRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<List<Intelligence>>>() {
        });
    }

    /**
     * 获取赛季轮次最佳阵容列表
     * 返回全量赛事赛季轮次最佳阵容数据，可根据时间戳增量获取新增或变动的数据
     * <p>
     * 1、首次全量更新，根据参数id获取全量数据
     * 2、后续增量更新，根据参数time增量获取变动数据（建议请求频次：1min/次）
     */
    public EdiResponse<List<SeasonBestLineup>> seasonBestLineup(SeasonBestLineupListRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<List<SeasonBestLineup>>>() {
        });
    }

    /**
     * 获取球队阵容列表
     * 返回全量球队球员阵容数据，可根据时间戳增量获取新增或变动的数据
     * <p>
     * 1、首次全量更新，根据参数id获取全量数据
     * 2、后续增量更新，根据参数time增量获取变动数据（建议请求频次：1min/次）
     */
    public EdiResponse<List<TeamSquad>> teamSquad(TeamSquadListRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<List<TeamSquad>>>() {
        });
    }

    /**
     * 获取球队伤停列表
     * 返回全量球队的球员伤停数据，可根据时间戳增量获取新增或变动的数据
     * <p>
     * 1、首次全量更新，根据参数id获取全量数据
     * 2、后续增量更新，根据参数time增量获取变动数据（建议请求频次：1min/次）
     */
    public EdiResponse<List<TeamInjury>> teamInjury(TeamInjuryListRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<List<TeamInjury>>>() {
        });
    }

    /**
     * 获取球队荣誉列表
     * 返回全量球队荣誉数据，可根据时间戳增量获取新增或变动的数据
     * <p>
     * 1、首次全量更新，根据参数id获取全量数据
     * 2、后续增量更新，根据参数time增量获取变动数据（建议请求频次：1min/次）
     */
    public EdiResponse<List<TeamHonor>> teamHonor(TeamHonorListRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<List<TeamHonor>>>() {
        });
    }

    /**
     * 获取球员转会列表
     * 返回全量球员的转会数据，可根据时间戳增量获取新增或变动的数据
     * <p>
     * 1、首次全量更新，根据参数id获取全量数据
     * 2、后续增量更新，根据参数time增量获取变动数据（建议请求频次：1min/次）
     */
    public EdiResponse<List<PlayerTransfer>> playerTransfer(PlayerTransferListRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<List<PlayerTransfer>>>() {
        });
    }

    /**
     * 获取球员荣誉列表
     * 返回全量球员荣誉数据，可根据时间戳增量获取新增或变动的数据
     * <p>
     * 1、首次全量更新，根据参数id获取全量数据
     * 2、后续增量更新，根据参数time增量获取变动数据（建议请求频次：1min/次）
     */
    public EdiResponse<List<PlayerHonor>> playerHonor(PlayerHonorListRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<List<PlayerHonor>>>() {
        });
    }

    /**
     * 获取教练执教履历列表
     * 返回全量教练的执教履历数据，可根据时间戳增量获取新增或变动的数据
     * <p>
     * 1、首次全量更新，根据参数id获取全量数据
     * 2、后续增量更新，根据参数time增量获取变动数据（建议请求频次：1min/次）
     */
    public EdiResponse<List<CoachHistory>> coachHistory(CoachHistoryListRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<List<CoachHistory>>>() {
        });
    }

    /**
     * 获取教练荣誉列表
     * 返回全量教练的荣誉数据，可根据时间戳增量获取新增或变动的数据
     * <p>
     * 1、首次全量更新，根据参数id获取全量数据
     * 2、后续增量更新，根据参数time增量获取变动数据（建议请求频次：1min/次）
     */
    public EdiResponse<List<CoachHonor>> coachHonor(CoachHonorListRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<List<CoachHonor>>>() {
        });
    }

    /**
     * 获取国家队FIFA男子排名
     * 返回国家队FIFA男子排名数据，可根据pub_time更新时间查询历史排名数据
     * <p>
     * 数据很少变动，根据‘数据更新’接口变动更新，或建议请求频次：1天/次
     */
    public EdiResponse<RankingFifaMen> rankingFifaMen(RankingFifaMenRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<RankingFifaMen>>() {
        });
    }

    /**
     * 获取国家队FIFA女子排名
     * 返回国家队FIFA女子排名数据，可根据pub_time更新时间查询历史排名数据
     * <p>
     * 数据很少变动，根据‘数据更新’接口变动更新，或建议请求频次：1天/次
     */
    public EdiResponse<RankingFifaWomen> rankingFifaWomen(RankingFifaWomenRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<RankingFifaWomen>>() {
        });
    }

    /**
     * 获取俱乐部排名
     * 返回俱乐部排名数据
     * <p>
     * 数据很少变动，根据‘数据更新’接口变动更新，或建议请求频次：1天/次
     */
    public EdiResponse<List<RankingClub>> rankingClub(RankingClubRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<List<RankingClub>>>() {
        });
    }

    // =========================== 高阶数据包 结束 =============================


    // =========================== 指数数据包 开始 =============================

    /**
     * 获取实时指数数据
     * 返回实时变动的赔率数据，无赔率变动的比赛不返回
     * 只返回最近60秒变化的赔率数据，需要客户自身把变动数据记录下来
     * <p>
     * 范围：初盘、即时盘、滚球盘
     * 每家公司每种指数的第一个盘口，即为初盘；初盘到开球为即时盘；开球后为滚球盘
     * <p>
     * 建议请求频次：3秒/次
     */
    public EdiResponse<HashMap<String, List<List<Object>>>> oddsLive(OddsLiveRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<HashMap<String, List<List<Object>>>>>() {
        });
    }

    /**
     * 获取单场比赛指数数据
     * 返回单场比赛所有指数公司的指数变化历史，从初盘到请求接口的时刻（根据‘指数更新数据’接口变动更新，作为补充使用）
     * <p>
     * 范围：初盘、即时盘、滚球盘
     * 每家公司每种指数的第一个盘口，即为初盘；初盘到开球为即时盘；开球后为滚球盘
     * <p>
     * 请求次数：120次/min
     * <p>
     * 说明：
     * 指数变化数据都在实时指数接口获取，实时指数接口返回的是最近60s内的全量比赛的变动指数数据，需本地记录
     * 若指数数据获取有缺失或未获取到，再通过单场比赛指数接口进行查缺补漏
     */
    public EdiResponse<OddsHistory> oddsHistory(OddsHistoryRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<OddsHistory>>() {
        });
    }

    /**
     * 获取指数更新数据
     * 返回最近60秒内指数有更新修复的全量比赛以及关联的指数公司与更新时间（倒序）
     * 建议请求频次：3秒/次
     * <p>
     * 说明：
     * 根据该接口获取到的比赛id与指数公司，通过‘单场比赛指数数据’接口获取并修复对应的公司指数数据
     */
    public EdiResponse<List<OddsUpdate>> oddsUpdate(OddsUpdateRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<List<OddsUpdate>>>() {
        });
    }

    /**
     * 获取百欧公司列表
     * 返回百欧公司列表数据
     * <p>
     * 数据很少变动，建议请求频次：1天/次
     */
    public EdiResponse<List<OddsEuropeCompany>> oddsEuropeCompany(OddsEuropeCompanyListRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<List<OddsEuropeCompany>>>() {
        });
    }

    /**
     * 获取实时百欧指数数据
     * 返回实时变动的欧赔数据，无赔率变动的比赛不返回
     * 返回最近60秒内变化的欧赔数据，需要客户自身把变动数据记录下来
     * <p>
     * 范围：初盘、即时盘
     * 每家公司每种指数的第一个盘口，即为初盘；初盘到开球为即时盘
     * <p>
     * 建议请求频次：5秒/次
     */
    public EdiResponse<OddsEuropeLive> oddsEuropeLive(OddsEuropeLiveRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<OddsEuropeLive>>() {
        });
    }

    /**
     * 获取单场比赛百欧指数数据
     * 返回单场比赛所有指数公司的欧赔变化历史，从初盘到请求接口的时刻（作为补充使用）
     * <p>
     * 范围：初盘、即时盘
     * 每家公司每种指数的第一个盘口，即为初盘；初盘到开球为即时盘
     * <p>
     * 请求次数：60次/min
     * <p>
     * 说明：
     * 百欧指数变化数据都在实时百欧指数接口获取，实时百欧指数接口返回的是最近60s内的全量比赛的变动指数数据，需本地记录
     * 若百欧指数数据获取有缺失或未获取到，再通过单场比赛百欧指数接口进行查缺补漏
     */
    public EdiResponse<OddsEuropeHistory> oddsEuropeHistory(OddsEuropeHistoryRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<OddsEuropeHistory>>() {
        });
    }

    // =========================== 指数数据包 结束 =============================

    //////统计数据包////////

    /**
     * 获取比赛分析数据
     * 返回比赛分析统计数据（历史交锋/近期战绩、未来赛程、进球分布）
     * <p>
     * 该接口用于请求未开赛的比赛的历史对阵等数据，多为历史数据，变化不频繁
     * 请求限制：前30天比赛
     * <p>
     * 请求次数：60次/min
     */

    public EdiResponse<MatchAnalysis> matchAnalysis(MatchAnalysisRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<MatchAnalysis>>() {
        });
    }

    /**
     * 获取历史比赛球队统计数据
     * 返回已完结历史比赛（限制：30天内）的球队统计数据
     * 请求次数：120次/min
     * <p>
     * 说明：
     * 实时比赛球队统计数据都在比赛球队统计接口获取，比赛球队统计接口返回的是10min内球队统计数据变动的比赛的球队统计数据，需本地记录
     * 若比赛球队统计数据获取有缺失或未获取到，再通过该接口进行查缺补漏
     */
    public EdiResponse<List<MatchTeamStatsDetail>> matchTeamStatsDetail(MatchTeamStatsDetailRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<List<MatchTeamStatsDetail>>>() {
        });
    }

    /**
     * 获取历史比赛球员统计数据
     * 返回已完结历史比赛（限制：30天内）的球员统计数据
     * 请求次数：120次/min
     * <p>
     * 说明：
     * 实时比赛球员统计数据都在比赛球员统计接口获取，比赛球员统计接口返回的是10min内球员统计数据变动的比赛的球员统计数据，需本地记录
     * 若比赛球员统计数据获取有缺失或未获取到，再通过该接口进行查缺补漏
     */
    public EdiResponse<List<MatchPlayerStatsDetail>> matchPlayerStatsDetail(MatchPlayerStatsDetailRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<List<MatchPlayerStatsDetail>>>() {
        });
    }

    /**
     * 获取历史比赛统计数据
     * 返回已完结历史比赛（限制：30天内）的统计数据（比赛事件、技术统计、文字直播）
     * 请求次数：120次/min
     * <p>
     * 说明：
     * 实时比赛统计数据都在实时统计数据接口获取，实时统计数据接口返回的是2h内的全量比赛的实时统计数据，需本地记录
     * 若比赛统计数据获取有缺失或未获取到，再通过该接口进行查缺补漏
     */
    public EdiResponse<MatchLiveHistory> matchLiveHistory(MatchLiveHistoryRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<MatchLiveHistory>>() {
        });
    }

    /**
     * 获取比赛历史同赔统计列表
     * 该接口返回30天内未开始比赛的统计数据（历史交锋、近期战绩、历史同赔），可根据时间戳增量获取新增或变动的数据
     * <p>
     * 1、首次全量更新，根据参数id获取全量数据
     * 2、后续增量更新，根据参数time增量获取变动数据（建议请求频次：1min/次）
     */
    public EdiResponse<List<Compensation>> compensationList(CompensationListRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<List<Compensation>>>() {
        });
    }

    /**
     * 获取淘汰阶段对阵图数据
     * 该接口返回同一赛季的赛事淘汰赛阶段对阵数据
     * 请求次数：120次/min
     */
    public EdiResponse<SeasonBracket> seasonBracket(SeasonBracketRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<SeasonBracket>>() {
        });
    }

    /**
     * 获取赛事积分榜数据
     * 返回查询赛事最新赛季的积分榜数据详情
     * 请求次数：120次/min
     */
    public EdiResponse<CompetitionTableDetail> competitionTableDetail(CompetitionTableDetailRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<CompetitionTableDetail>>() {
        });
    }

    /**
     * 获取赛事统计详情
     * 返回查询赛事最新赛季的球队球员统计详情数据（球员统计、射手榜、球队统计）
     * 请求次数：120次/min
     * <p>
     * 说明：
     * 由于球员存在赛事内转会情况，球员统计中会存在一个球员有多条数据，所在球队不同的情况，表示该球员在不同球队的数据，可合并处理
     */
    public EdiResponse<CompetitionStatsDetail> competitionStatsDetail(CompetitionStatsDetailRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<CompetitionStatsDetail>>() {
        });
    }

    //////必发数据包////////

    /**
     * 获取实时必发数据
     * 返回实时变动的必发数据，无变动的比赛不返回
     * 返回最近60秒变化的必发数据，需要客户自身把变动数据记录下来
     * <p>
     * 建议请求频次：3秒/次
     */
    public EdiResponse<OddsBfLive> oddsBfLive(OddsBfLiveRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<OddsBfLive>>() {
        });
    }

    /**
     * 获取单场比赛必发数据
     * 返回单场比赛必发数据的变化历史，从初始到请求接口的时刻
     * 请求次数：120次/min
     * <p>
     * 说明：
     * 必发变化数据在实时必发接口获取，实时必发接口返回的是最近60s内的全量比赛的变动必发数据，需本地记录
     * 若必发数据获取有缺失或未获取到，再通过单场比赛必发接口进行查缺补漏
     */
    public EdiResponse<OddsBfHistory> oddsBfHistory(OddsBfHistoryRequest request) {
        return JSON.parseObject(execute(request), new TypeReference<EdiResponse<OddsBfHistory>>() {
        });
    }

    /**
     * 获取分类列表
     *
     * @param request
     * @return
     */
    public EdiResponse<List<Category>> categoryList(CategoryListRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<Category>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取国家列表
     *
     * @param request
     * @return
     */
    public EdiResponse<List<Country>> countryList(CountryListRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<Country>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取赛事列表
     *
     * @param request
     * @return
     */
    public EdiResponse<List<Competition>> competitionList(CompetitionListRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<Competition>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取赛事赛制列表
     *
     * @param request
     * @return
     */
    public EdiResponse<List<CompetitionRule>> competitionRuleList(CompetitionRuleListRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<CompetitionRule>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取球队列表
     *
     * @param request
     * @return
     */
    public EdiResponse<List<Team>> teamList(TeamListRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<Team>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取球员列表
     *
     * @param request
     * @return
     */
    public EdiResponse<List<Player>> playerList(PlayerListRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<Player>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取教练列表
     *
     * @param request
     * @return
     */
    public EdiResponse<List<Coach>> coachList(CoachListRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<Coach>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }


    /**
     * 获取裁判列表
     *
     * @param request
     * @return
     */
    public EdiResponse<List<Referee>> refereeList(RefereeListRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<Referee>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取场馆列表
     *
     * @param request
     * @return
     */
    public EdiResponse<List<Venue>> venueList(VenueListRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<Venue>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取荣誉列表
     *
     * @param request
     * @return
     */
    public EdiResponse<List<Honor>> honorList(HonorListRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<Honor>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取赛季列表
     *
     * @param request
     * @return
     */
    public EdiResponse<List<Season>> seasonList(SeasonListRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<Season>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取阶段列表
     *
     * @param request
     * @return
     */
    public EdiResponse<List<Stage>> stageList(StageListRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<Stage>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取数据更新
     *
     * @param request
     * @return
     */
    public EdiResponse<Map<String, List<DataUpdate>>> dataMoreUpdate(DataMoreUpdateRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<Map<String, List<DataUpdate>>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }


    /**
     * 按比赛时间、赛事类型、状态 获取比赛列表
     */
    public EdiResponse<PageVo<Match>> matchScheduleSearchRequest(MatchScheduleSearchRequest request) {
        String execute = execute(request);
        return JSON.parseObject(execute, new TypeReference<EdiResponse<PageVo<Match>>>() {
        });
    }

    /**
     * 模糊搜索赛事列表
     */
    public EdiResponse<List<Competition>> competitionSearchRequest(CompetitionSearchRequest request) {
        String execute = execute(request);
        return JSON.parseObject(execute, new TypeReference<EdiResponse<List<Competition>>>() {
        });
    }


    /**
     * 按比赛时间获取天比赛数
     */
    public EdiResponse<List<MatchTimeCountList>> matchTimeCountListRequest(MatchTimeCountListRequest request) {
        String execute = execute(request);
        return JSON.parseObject(execute, new TypeReference<EdiResponse<List<MatchTimeCountList>>>() {
        });
    }

    /**
     * 根据日期查询有比赛的赛事列表
     */
    public EdiResponse<List<Competition>> searchByTimeCompetitionRequest(SearchByTimeCompetitionRequest request) {
        String execute = execute(request);
        return JSON.parseObject(execute, new TypeReference<EdiResponse<List<Competition>>>() {
        });
    }

    /**
     * 获取比赛对应的线路信息
     * 返回全量数据
     */
    public EdiResponse<List<MatchVideo>> searchVideoList(MatchLiveVideoRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<MatchVideo>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }
}
