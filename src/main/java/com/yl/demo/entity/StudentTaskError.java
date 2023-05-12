package com.yl.demo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentTaskError {

    private boolean error;

    public void setIsError() {
        this.error = true;
    }

    public boolean getIsError() {
        return error;
    }

}
