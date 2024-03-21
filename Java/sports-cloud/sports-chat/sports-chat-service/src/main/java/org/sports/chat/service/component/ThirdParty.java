package org.sports.chat.service.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @描述: JPushBody
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/12/7
 * @创建时间: 15:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThirdParty implements Serializable{
    private  String distribution_new = "mtpush_pns";



}
