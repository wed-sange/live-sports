package com.edi.sdk.basketball;


import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.edi.commons.model.vo.PageVo;
import com.edi.sdk.basketball.domain.*;
import com.edi.sdk.basketball.request.*;
import com.edi.sdk.core.EdiClient;
import com.edi.sdk.core.EdiResponse;

/**
 2023年2月9日 下午1:16:19
 */
public class BasketballClient extends EdiClient {

    public BasketballClient() {
        super();
    }

    public BasketballClient(String user, String secret) {
        super(user, secret);
    }

    public BasketballClient(String user, String secret, String baseUrl) {
        super(user, secret, baseUrl);
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
     * 获取变动比赛列表<br/>
     * <p>
     * 用于获取比赛变动后的数据（比赛时间、状态等），可根据时间戳增量获取新增或变动的数据<br/>
     * 限制：前30天比赛开始<br/>
     * 1、首次全量更新，根据参数id获取全量数据<br/>
     * 2、后续增量更新，根据参数time增量获取变动数据（建议请求频次：1min/次）<br/>
     *
     * @param request
     * @return
     */
    public EdiResponse<List<Match>> recentMatchList(RecentMatchListRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<Match>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取赛程赛果列表-日期查询<br/>
     * 该接口返回所请求日期的全量赛程赛果数据（请求限制：前后30天）<br/>
     * 注：实时数据的获取通过实时统计数据接口<br/>
     * 当天赛程 建议请求频次：10min/次（全量更新）<br/>
     * 未来赛程 建议请求频次：30min/次（全量更新）<br/>
     *
     * @param request
     * @return
     */
    public EdiResponse<Schedule> scheduleDiary(MatchScheduleDiaryRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<Schedule>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取赛程赛果列表-维度查询 <br/>
     * 该接口返回维度查询对应的全量赛程赛果数据（赛事-最新赛季赛程、体彩-最近72h内彩种赛程） <br/>
     * 请求次数：120次/min <br/>
     *
     * @param request
     * @return
     */
    public EdiResponse<List<Match>> scheduleParam(MatchScheduleParamRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<Match>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取实时统计数据 <br/>
     * 返回最近120min内的比赛技术统计、阵容及文字直播数据（全量更新） <br/>
     * ps：非120min内的比赛数据有更新，也会同步返回 <br/>
     * 建议请求频次：2s/次 <br/>
     *
     * @param request
     * @return
     */
    public EdiResponse<List<MatchLive>> matchLive(MatchLiveRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<MatchLive>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取比赛趋势详情<br/>
     * 返回比赛主客队趋势详情数据<br/>
     * 主队为正、客队为负，按比赛分钟数变化<br/>
     * 请求限制：前30天比赛<br/>
     * 请求次数：120次/min<br/>
     *
     * @param request
     * @return
     */
    public EdiResponse<TrendDetail> matchTrendDetail(MatchTrendDetailRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<TrendDetail>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取比赛投篮点数据<br/>
     * 返回比赛主客队球员投篮点数据（有阵容数据的比赛，根据“实时统计数据接口”中是否有“阵容players”字段来判断是否调用该接口）<br/>
     * 请求次数：120次/min<br/>
     * 坐标说明：<br/>
     * 坐标原点：左上；即：x轴方向向右，y轴方向向下；<br/>
     * 请求限制：前30天比赛<br/>
     *
     * @param request
     * @return
     */
    public EdiResponse<List<List<Object>>> matchShootPoint(MatchShootPointRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<List<Object>>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取版权比赛直播地址<br/>
     * 返回未开赛的版权比赛（24h内）版权方的直播地址<br/>
     * 建议请求频次：10min/次<br/>
     *
     * @param request
     * @return
     */
    public EdiResponse<List<UrlsFree>> matchStreamUrlsFree(MatchStreamUrlsFreeRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<UrlsFree>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取版权比赛集锦录像地址<br/>
     * 返回版权比赛版权方的集锦/录像地址，‘版权比赛直播地址’api中的比赛可能会有集锦/录像（开赛后获取）<br/>
     * 请求次数：120次/min<br/>
     *
     * @param request
     * @return
     */
    public EdiResponse<List<VideoCollection>> matchVideoCollection(MatchStreamVideoCollectionRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<VideoCollection>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    /**
     * 获取删除数据<br/>
     * 返回最近24小时内删除的数据id（比赛、球队、球员、赛事、赛季、阶段），需定时同步<br/>
     * <p>
     * 建议请求频次：1~5min/次<br/>
     *
     * @param request
     * @return
     */
    public EdiResponse<List<DeletedData>> basketballDeleted(DeletedDataListRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<List<DeletedData>>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    // =========================== 资料库数据包 开始 =============================

    /**
     * 获取比赛列表
     * 返回全量比赛数据，可根据时间戳增量获取新增或变动的数据
     * <p>
     * 1、首次全量更新，根据参数id获取全量数据
     * 2、后续增量更新，根据参数time增量获取变动数据（建议请求频次：1min/次）
     *
     * @param request
     * @return
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
     *
     * @param request
     * @return
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
     *
     * @param request
     * @return
     */
    public EdiResponse<SeasonTableDetail> seasonTableDetail(SeasonTableDetailRequest request) {
        String execute = execute(request);
        return JSON.parseObject(execute, new TypeReference<EdiResponse<SeasonTableDetail>>() {
        });
    }

    /**
     * 获取赛季统计详情
     * 返回查询赛季的积分榜数据详情
     * 请求次数：120次/min
     * <p>
     * 说明：
     * 由于球员存在转会情况，球员数据中会存在一个球员有多条数据，所在球队不同的情况，分别表示该球员在不同球队的数据，可合并处理
     *
     * @param request
     * @return
     */
    public EdiResponse<SeasonStatsDetail> seasonStatsDetail(SeasonStatsDetailRequest request) {
        String execute = execute(request);
        return JSON.parseObject(execute, new TypeReference<EdiResponse<SeasonStatsDetail>>() {
        });
    }


    /**
     * 获取打包数据地址
     * 返回全量历史比赛的打包数据地址（比赛详情、指数）
     *
     * @param request
     * @return
     */
    public EdiResponse<Archive> archive(ArchiveRequest request) {
        String execute = execute(request);
        Type type = new TypeReference<EdiResponse<Archive>>() {
        }.getType();
        return JSON.parseObject(execute, type);
    }

    // =========================== 资料库数据包 结束 =============================


    // =========================== 高阶数据包 开始 =============================

    /**
     * 获取情报列表
     * 返回全量比赛情报数据，可根据时间戳增量获取新增或变动的数据
     * <p>
     * 1、首次全量更新，根据参数id获取全量数据
     * 2、后续增量更新，根据参数time增量获取变动数据（建议请求频次：1min/次）
     *
     * @param request
     * @return
     */
    public EdiResponse<List<Intelligence>> intelligence(IntelligenceListRequest request) {
        String execute = execute(request);
        return JSON.parseObject(execute, new TypeReference<EdiResponse<List<Intelligence>>>() {
        });
    }

    /**
     * 获取球队阵容列表
     * 返回全量球队球员阵容数据，可根据时间戳增量获取新增或变动的数据
     * <p>
     * 1、首次全量更新，根据参数id获取全量数据
     * 2、后续增量更新，根据参数time增量获取变动数据（建议请求频次：1min/次）
     *
     * @param request
     * @return
     */
    public EdiResponse<List<TeamSquad>> teamSquad(TeamSquadListRequest request) {
        String execute = execute(request);
        return JSON.parseObject(execute, new TypeReference<EdiResponse<List<TeamSquad>>>() {
        });
    }


    /**
     * 获取球队伤停列表
     * 返回全量球队的球员伤停数据，可根据时间戳增量获取新增或变动的数据
     * <p>
     * 1、首次全量更新，根据参数id获取全量数据
     * 2、后续增量更新，根据参数time增量获取变动数据（建议请求频次：1min/次）
     *
     * @param request
     * @return
     */
    public EdiResponse<List<TeamInjury>> teamInjury(TeamInjuryListRequest request) {
        String execute = execute(request);
        return JSON.parseObject(execute, new TypeReference<EdiResponse<List<TeamInjury>>>() {
        });
    }

    /**
     * 获取球队荣誉列表
     * 返回全量球队荣誉数据（冠军），可根据时间戳增量获取新增或变动的数据
     * <p>
     * 1、首次全量更新，根据参数id获取全量数据
     * 2、后续增量更新，根据参数time增量获取变动数据（建议请求频次：1min/次）
     *
     * @param request
     * @return
     */
    public EdiResponse<List<TeamHonor>> teamHonor(TeamHonorListRequest request) {
        String execute = execute(request);
        return JSON.parseObject(execute, new TypeReference<EdiResponse<List<TeamHonor>>>() {
        });
    }

    /**
     * 获取球员荣誉列表
     * 返回全量球员荣誉数据，可根据时间戳增量获取新增或变动的数据
     * <p>
     * 1、首次全量更新，根据参数id获取全量数据
     * 2、后续增量更新，根据参数time增量获取变动数据（建议请求频次：1min/次）
     *
     * @param request
     * @return
     */
    public EdiResponse<List<PlayerHonor>> playerHonor(PlayerHonorListRequest request) {
        String execute = execute(request);
        return JSON.parseObject(execute, new TypeReference<EdiResponse<List<PlayerHonor>>>() {
        });
    }

    /**
     * 获取球员职业生涯列表
     * 返回全量球员职业生涯数据，可根据时间戳增量获取新增或变动的数据
     * <p>
     * 1、首次全量更新，根据参数id获取全量数据
     * 2、后续增量更新，根据参数time增量获取变动数据（建议请求频次：1min/次）
     *
     * @param request
     * @return
     */
    public EdiResponse<List<PlayerCareer>> playerCareer(PlayerCareerListRequest request) {
        String execute = execute(request);
        return JSON.parseObject(execute, new TypeReference<EdiResponse<List<PlayerCareer>>>() {
        });
    }


    /**
     * 获取国家队FIBA男子排名
     * 返回国家队FIBA男子排名数据，可根据pub_time更新时间查询历史排名数据
     * <p>
     * 数据很少变动，根据‘数据更新’接口变动更新，或建议请求频次：1天/次
     *
     * @param request
     * @return
     */
    public EdiResponse<RankingFibaMen> rankingFibaMen(RankingFibaMenRequest request) {
        String execute = execute(request);
        return JSON.parseObject(execute, new TypeReference<EdiResponse<RankingFibaMen>>() {
        });
    }

    /**
     * 获取国家队FIBA女子排名
     * 返回国家队FIBA女子排名数据，可根据pub_time更新时间查询历史排名数据
     * <p>
     * 数据很少变动，根据‘数据更新’接口变动更新，或建议请求频次：1天/次
     *
     * @param request
     * @return
     */
    public EdiResponse<RankingFibaWomen> rankingFibaWomen(RankingFibaWomenRequest request) {
        String execute = execute(request);
        return JSON.parseObject(execute, new TypeReference<EdiResponse<RankingFibaWomen>>() {
        });
    }


    // =========================== 高阶数据包 结束 =============================


    // =========================== 统计数据包 开始 =============================

    /**
     * 获取比赛分析数据
     * 返回比赛分析统计数据（比赛信息、历史交锋/近期战绩、未来赛程）
     * <p>
     * 该接口用于请求未开赛的比赛的历史对阵等数据，多为历史数据，变化不频繁
     * 请求限制：前30天比赛
     * <p>
     * 请求次数：60次/min
     */
    public EdiResponse<MatchAnalysis> matchAnalysis(MatchAnalysisRequest request) {
        String execute = execute(request);
        return JSON.parseObject(execute, new TypeReference<EdiResponse<MatchAnalysis>>() {
        });
    }

    /**
     * 获取历史比赛统计数据
     * 返回已完结历史比赛（限制：30天内）的统计数据（技术统计、阵容、文字直播）
     * 请求次数：120次/min
     * <p>
     * 说明：
     * 实时比赛统计数据都在实时统计数据接口获取，实时统计数据接口返回的是4h内的全量比赛的实时统计数据，需本地记录
     * 若比赛统计数据获取有缺失或未获取到，再通过历史比赛统计数据接口进行查缺补漏
     */
    public EdiResponse<MatchLiveHistory> matchLiveHistory(MatchLiveHistoryRequest request) {
        String execute = execute(request);
        return JSON.parseObject(execute, new TypeReference<EdiResponse<MatchLiveHistory>>() {
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
        String execute = execute(request);
        return JSON.parseObject(execute, new TypeReference<EdiResponse<List<Compensation>>>() {
        });
    }

    /**
     * 获取赛事统计详情
     * 返回查询赛事最新赛季的球队球员统计详情数据
     * 请求次数：120次/min
     * <p>
     * 说明：
     * 由于球员存在转会情况，球员数据中会存在一个球员有多条数据，所在球队不同的情况，分别表示该球员在不同球队的数据，可合并处理
     */
    public EdiResponse<CompetitionStatsDetail> competitionStatsDetail(CompetitionStatsDetailRequest request) {
        String execute = execute(request);
        return JSON.parseObject(execute, new TypeReference<EdiResponse<CompetitionStatsDetail>>() {
        });
    }

    /**
     * 获取赛事积分榜数据
     * 返回查询赛事最新赛季的积分榜数据详情
     * 请求次数：120次/min
     */
    public EdiResponse<CompetitionTableDetail> competitionTableDetail(CompetitionTableDetailRequest request) {
        String execute = execute(request);
        return JSON.parseObject(execute, new TypeReference<EdiResponse<CompetitionTableDetail>>() {
        });
    }

    /**
     * 获取淘汰阶段对阵图数据
     * 该接口返回同一赛季的赛事淘汰赛阶段对阵数据（如：nba/cba季后赛）
     * 请求次数：120次/min
     */
    public EdiResponse<SeasonBracket> seasonBracket(SeasonBracketRequest request) {
        String execute = execute(request);
        return JSON.parseObject(execute, new TypeReference<EdiResponse<SeasonBracket>>() {
        });
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
}
