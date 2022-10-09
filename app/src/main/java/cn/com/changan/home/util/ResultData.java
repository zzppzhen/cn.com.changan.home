package cn.com.changan.home.util;

import java.io.Serializable;

public class ResultData implements Serializable {
    private String Reason;
    private String Result;
    private int ErrorCode;

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public int getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(int errorCode) {
        ErrorCode = errorCode;
    }
}
