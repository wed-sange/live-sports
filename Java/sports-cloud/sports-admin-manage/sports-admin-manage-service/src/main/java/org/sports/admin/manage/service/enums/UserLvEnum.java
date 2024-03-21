package org.sports.admin.manage.service.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 用户等级成长值
 */
@Getter
public enum UserLvEnum {
    ONE(1, 0, "黄铜"),
    TWO(2, 300, "白银"),
    HTREE(3, 1000, "黄金"),
    FOUR(4, 3000, "铂金"),
    FIVE(5, 10000, "钻石"),
    SIX(6, 50000, "星耀");

    private Integer lv;
    private Integer score;
    private String name;

    UserLvEnum(Integer lv, Integer score, String name){
        this.lv = lv;
        this.score = score;
        this.name = name;
    }

    /**
     * 根据当前成长值 返回对应等级
     * @param score
     * @return
     */
    public static UserLvEnum getLvEnumByScore(Integer score){
        if(score < UserLvEnum.TWO.getScore()){
            return UserLvEnum.ONE;
        }else if(score < UserLvEnum.HTREE.getScore()){
            return UserLvEnum.TWO;
        }else if(score < UserLvEnum.FOUR.getScore()){
            return UserLvEnum.HTREE;
        }else if(score < UserLvEnum.FIVE.getScore()){
            return UserLvEnum.FOUR;
        }else if(score < UserLvEnum.SIX.getScore()){
            return UserLvEnum.FIVE;
        }else{
            return UserLvEnum.SIX;
        }
    }

    /**
     * 根据当前等级获取下一等级成长值
     * @param lv
     * @return
     */
    public static Integer getNextLvEnumScore(Integer lv){
        if(Objects.equals(lv, UserLvEnum.ONE.getLv())){
            return UserLvEnum.TWO.getScore();
        }else if(Objects.equals(lv, UserLvEnum.TWO.getLv())){
            return UserLvEnum.HTREE.getScore();
        }else if(Objects.equals(lv, UserLvEnum.HTREE.getLv())){
            return UserLvEnum.FOUR.getScore();
        }else if(Objects.equals(lv, UserLvEnum.FOUR.getLv())){
            return UserLvEnum.FIVE.getScore();
        }else if(Objects.equals(lv, UserLvEnum.FIVE.getLv())){
            return UserLvEnum.SIX.getScore();
        }
        return UserLvEnum.SIX.getScore();
    }
}
