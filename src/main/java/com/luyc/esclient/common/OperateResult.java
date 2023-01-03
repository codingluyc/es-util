package com.luyc.esclient.common;

/**
 * es数据库操作结果
 * @author luyc
 * @since 2023/1/3 14:47
 */
public class OperateResult {
    private String id;
    private Boolean result;

    private Integer index;
    private String error;

    public OperateResult(Boolean result, Integer index, String error) {
        this.result = result;
        this.index = index;
        this.error = error;
    }

    public OperateResult(String id, Boolean result) {
        this.id = id;
        this.result = result;
    }

    public String getId() {
        return id;
    }
    public boolean isSuccess(){
        return this.result.booleanValue();
    }
}
