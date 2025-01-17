package org.jim.server.processor.group;

import org.jim.core.ImChannelContext;
import org.jim.core.packets.Group;
import org.jim.core.packets.JoinGroupRespBody;
import org.jim.core.packets.OutGroupRespBody;
import org.jim.server.processor.SingleProtocolCmdProcessor;


public interface GroupCmdProcessor extends SingleProtocolCmdProcessor {
    /**
     * 加入群组处理
     * @param joinGroup
     * @param imChannelContext
     * @return
     */
    JoinGroupRespBody join(Group joinGroup, ImChannelContext imChannelContext);

    /**
     * 离开群组处理
     * @param joinGroup
     * @param imChannelContext
     * @return
     */
    OutGroupRespBody out(Group joinGroup, ImChannelContext imChannelContext);
}
