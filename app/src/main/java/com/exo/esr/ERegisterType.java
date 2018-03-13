package com.exo.esr;

/**
 * Created by atanasko on 3/4/18.
 */

public enum ERegisterType {

    REGISTER_IN("01"),
    BREAK("02"),
    REGISTER_OUT("03");

    private String checkState;

    ERegisterType(String checkState) {
        this.checkState = checkState;
    }

    public String registerType() {
        return checkState;
    }

}
