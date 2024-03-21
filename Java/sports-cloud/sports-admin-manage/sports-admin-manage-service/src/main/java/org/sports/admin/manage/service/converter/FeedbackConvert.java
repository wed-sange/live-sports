package org.sports.admin.manage.service.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.sports.admin.manage.dao.entity.FeedbackDO;
import org.sports.admin.manage.dao.req.FeedbackIgnoreRequest;
import org.sports.admin.manage.dao.req.FeedbackReplyRequest;
import org.sports.admin.manage.dao.req.FeedbackSubmitRequest;
import org.sports.admin.manage.service.vo.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Mapper
public interface FeedbackConvert {
    FeedbackConvert INSTANCE = Mappers.getMapper(FeedbackConvert.class);

    @Mapping(target = "feedbackImage", expression = "java(stringToList(bean.getFeedbackImage()))")
    FeedbackDetailByAdminVO convertToDetailByAdminVO(FeedbackDO bean);

    @Mapping(target = "feedbackImage", expression = "java(stringToList(bean.getFeedbackImage()))")
    FeedbackDetailByUserVO convertToDetailByUserVO(FeedbackDO bean);

    default List<String> stringToList(String source) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(source.split(","));
    }

    default String listToString(List<String> source) {
        if (source == null || source.isEmpty()) {
            return "";
        }
        return String.join(",", source);
    }

    @Mapping(target = "feedbackImage", expression = "java(listToString(bean.getFeedbackImage()))")
    FeedbackDO convertToDo(FeedbackSubmitRequest bean);

    FeedbackDO convertToDo(FeedbackIgnoreRequest bean);

    FeedbackDO convertToDo(FeedbackReplyRequest bean);

    @Mapping(target = "feedbackImage", expression = "java(stringToList(bean.getFeedbackImage()))")
    FeedbackListByAdminVO convertToListByAdminVO(FeedbackDO bean);

    @Mapping(target = "feedbackImage", expression = "java(stringToList(bean.getFeedbackImage()))")
    FeedbackListByUserVO convertToListByUserVO(FeedbackDO bean);
}
