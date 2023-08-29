package com.github.ontio.model.common;

/**
 * @author zhouq
 * @version 1.0
 * @date 2020/2/26
 */
public enum StakeStatusEnum {

    PENDING(1),//待生效
    IN_STAKE(2),//质押中
    WITHDRAWABLE(3),//可提取
    CANCELLING(4); //取消中

    private int state;

    StakeStatusEnum(int state) {
        this.state = state;
    }

    public int state() {
        return state;
    }

}
