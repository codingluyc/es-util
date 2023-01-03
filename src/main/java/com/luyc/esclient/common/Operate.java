package com.luyc.esclient.common;

import java.util.List;

/**
 * @author luyc
 * @since 2023/1/3 15:21
 */
public class Operate {
    public boolean success;
    private List<OperateResult> list;

    public Operate(boolean success, List<OperateResult> list) {
        this.success = success;
        this.list = list;
    }

    public static Operate success(){
        return new Operate(true,null);
    }

    public static Operate failed(List<OperateResult> list){
        return new Operate(false,list);
    }

    public boolean isSuccess() {
        return success;
    }

    public List<OperateResult> getList() {
        return list;
    }
}
