package com.luyc.esclient.common;

import java.util.List;

/**
 * @author luyc
 * @since 2023/1/3 15:21
 */
public class Operate {

    /**
     * 操作是否成功
     */
    public boolean success;

    /**
     * 失败列表
     */
    private List<OperateResult> list;

    /**
     * 成功数量
     */
    private Integer  successCount;

    /**
     * 失败数量
     */
    private Integer failCount;


    public Operate(boolean success, Integer successCount,List<OperateResult> list) {
        this.success = success;
        this.list = list;
        this.successCount = successCount;
        if(!success){
            this.failCount = list.size();
        }
    }

    public static Operate success(){
        return new Operate(true,0,null);
    }

    public static Operate success(Integer successCount){
        return new Operate(true,successCount,null);
    }

    public static Operate failed(List<OperateResult> list){
        return new Operate(false,0,list);
    }

    public static Operate failed(Integer successCount,List<OperateResult> list){
        return new Operate(false,successCount,list);
    }

    public boolean isSuccess() {
        return success;
    }

    public List<OperateResult> getList() {
        return list;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setList(List<OperateResult> list) {
        this.list = list;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }
}