package org.sports.app.service.service;

import org.sports.admin.manage.dao.req.LiveSearchRequest;
import org.sports.admin.manage.service.vo.HomeLivingInfoVo;
import org.sports.admin.manage.service.vo.HomeLivingVo;
import org.sports.springboot.starter.convention.page.PageResponse;

public interface HomeService {
    PageResponse<HomeLivingVo> living(LiveSearchRequest request);

    HomeLivingInfoVo livingInfo(Long id);
}
