package org.sports.app.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.sports.admin.manage.dao.req.LiveSearchRequest;
import org.sports.admin.manage.service.service.ILiveService;
import org.sports.admin.manage.service.service.ILiveUserService;
import org.sports.admin.manage.service.service.IUserLiveHistoryService;
import org.sports.admin.manage.service.vo.HomeLivingInfoVo;
import org.sports.admin.manage.service.vo.HomeLivingVo;
import org.sports.admin.manage.service.vo.LiveUserVO;
import org.sports.app.service.service.HomeService;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {
    private final ILiveService iLiveService;
    private final IUserLiveHistoryService userLiveHistoryService;
    private final ILiveUserService liveUserService;

    @Override
    public PageResponse<HomeLivingVo> living(LiveSearchRequest request) {
        if(Strings.isNotBlank(request.getName())){
            List<LiveUserVO> liveUserListByNickName = liveUserService.getLiveUserListByNickName(request.getName());
            if(!CollectionUtils.isEmpty(liveUserListByNickName)){
                request.setAnchorIds(liveUserListByNickName.stream().map(LiveUserVO::getId).collect(Collectors.toList()));
            }
        }
        PageResponse<HomeLivingVo> pageResponse =  iLiveService.living(request);
        if(Objects.nonNull(pageResponse)|| !CollectionUtils.isEmpty(pageResponse.getRecords())) {
            List<Long> anchorIds =  pageResponse.getRecords().stream().map(HomeLivingVo::getUserId).distinct().collect(Collectors.toList());
            List<LiveUserVO> liveUserList = liveUserService.getLiveUserList(anchorIds);
            Map<Long, LiveUserVO> liveUserMap = liveUserList.stream().collect(Collectors.toMap(LiveUserVO::getId, Function.identity()));
            pageResponse.getRecords().forEach(item->{
                LiveUserVO liveUserVO = liveUserMap.get(item.getUserId());
                if(Objects.nonNull(liveUserVO)){
                    item.setNickName(liveUserVO.getNickName());
                    item.setUserLogo(liveUserVO.getHead());
                }
            });

        }
        return pageResponse;
    }

    @Override
    public HomeLivingInfoVo livingInfo(Long id) {
        return iLiveService.livingInfo(id);
    }
}
