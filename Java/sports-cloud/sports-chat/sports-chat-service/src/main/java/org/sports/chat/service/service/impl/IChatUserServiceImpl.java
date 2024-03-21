package org.sports.chat.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sports.chat.service.entity.ChatUserDO;
import org.sports.chat.service.mapper.ChatUserMapper;
import org.sports.chat.service.service.IChatUserService;
import org.springframework.stereotype.Service;


@Service
public class IChatUserServiceImpl extends ServiceImpl<ChatUserMapper, ChatUserDO> implements IChatUserService {

}
