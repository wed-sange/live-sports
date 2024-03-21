package org.sports.chat.service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageUserDto {

    private String avatar;

    private String nick;

    private Integer level;
}
