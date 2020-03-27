package com.github.ontio.txPush.model;

/**
 * @author zhouq
 * @version 1.0
 * @date 2020/3/26
 */
public enum PushStrategyEnum {

    NoPush(0),
    AllPush(1),
    DepositPush(2),
    WithdrawPush(3);

    private int value;

    PushStrategyEnum(int value){
        this.value = value;
    }

    public int value(){
        return this.value;
    }


}
