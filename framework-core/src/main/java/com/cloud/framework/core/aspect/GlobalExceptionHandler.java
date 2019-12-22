package com.cloud.framework.core.aspect;


import com.cloud.common.entity.ResponseResult;
import com.cloud.common.enums.StatusEnum;
import com.cloud.common.exception.MyException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author zhangyl
 * @Date 2019/12/22
 */
@RestControllerAdvice()
public class GlobalExceptionHandler {
    public static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public ResponseResult exception(Exception e) {
        logger.error("服务异常:", e);
        return ResponseResult.fail();
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseResult nullPointerException(Exception e) {
        logger.error("空指针异常:", e);
        return ResponseResult.fail().message("NullPointerException");
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseResult httpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.error("空指针异常:", e);
        return ResponseResult.fail().message("请求参数格式异常");
    }

    @ExceptionHandler(value = MyException.class)
    public ResponseResult MyException(MyException e) {
        ResponseResult result = new ResponseResult();
        result.setCode(StringUtils.isEmpty(e.getCode()) ? StatusEnum.FAILED.getCode() : e.getCode());
        result.setMessage(StringUtils.isEmpty(e.getMessage()) ? StatusEnum.FAILED.getMessage() : e.getMessage());
        return result;
    }
}
