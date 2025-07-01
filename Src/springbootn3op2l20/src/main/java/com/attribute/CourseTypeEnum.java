package com.attribute;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

public enum CourseTypeEnum implements IEnum {
    Math("1", "考研数学"),
    ComputerScience("2", "408"),
    Politics("3", "政治"),
    English("4", "英语");

    private final String code;

    @JsonValue
    private final String description;

    CourseTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String getValue() {

        return this.code;
    }

    public static CourseTypeEnum fromCode(String code) {
        for (CourseTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
