package com.xdja.dsc.advice;

import com.xdja.dsc.enums.ExceptionEnum;
import com.xdja.dsc.exception.ServiceException;
import com.xdja.dsc.vo.ExceptionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

/**
 * 全部异常处理
 *
 * @author Created by niugang on 2018/12/26/12:19
 */
@ControllerAdvice
@Slf4j
public class BasicExceptionHandler {

    /**
     * 具体业务层异常
     *
     * @param e 业务异常
     * @return ResponseEntity<ExceptionResult>
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ExceptionResult> handleException(ServiceException e) {

        /**
         * 响应的状态码，为枚举中定义的状态码
         */
        return ResponseEntity.status(e.getExceptionEnum().value())
                .body(new ExceptionResult(e.getExceptionEnum()));
    }


    /**
     * 业务处理未知异常
     *
     * @param e 异常
     * @return ResponseEntity<ExceptionResult>
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionResultResponseEntity(Exception e) {
        //所有参数异常
        //在对象上绑定校验如(UserDTO)
        if (e instanceof BindException || e instanceof MethodArgumentNotValidException || e instanceof IllegalArgumentException) {
            log.error("参数校验失败:{}", e);
            return ResponseEntity.status(ExceptionEnum.PARAMS_VALIDATE_FAIL.value())
                    .body(new ExceptionResult(ExceptionEnum.PARAMS_VALIDATE_FAIL));
        }
        //方法上参数校验失败
        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException ex = (ConstraintViolationException) e;
            Map<String, Object> res = new HashMap<>(16);
            res.put("status", HttpStatus.BAD_REQUEST.value());
            res.put("message", ex.getMessage());
            res.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(res);

        }

        log.error("服务器内部异常:{}", e);
        /*
         * 响应的状态码，为枚举中定义的状态码
         */
        return ResponseEntity.status(ExceptionEnum.BUSINESS_DEAL_FAIL.value())
                .body(new ExceptionResult(ExceptionEnum.BUSINESS_DEAL_FAIL));
    }
}
