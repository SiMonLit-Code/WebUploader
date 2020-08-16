package com.xdja.dsc.vo;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Created by niugang on 2019/4/23/10:39
 */
//JsonIgnoreProperties 用在类上
//@JsonIgnore此注解用于属性或者方法上（最好是属性上）
@JsonIgnoreProperties(value = {"id"})
//null值忽略
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    private String id;
    private String name;

    /**
     * 日期格式化
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime brithday;
    private String address;
    /**
     * 定义序列化别名
     */
    @JsonProperty("moblie")
    private String phone;
    private String school;
}
