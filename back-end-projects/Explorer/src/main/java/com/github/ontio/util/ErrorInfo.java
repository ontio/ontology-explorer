/*
 * Copyright (C) 2018 The ontology Authors
 * This file is part of The ontology library.
 *
 * The ontology is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ontology is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with The ontology.  If not, see <http://www.gnu.org/licenses/>.
 */



package com.github.ontio.util;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/2/27
 */
public enum ErrorInfo {

    /**
     * success
     */
    SUCCESS(0, "SUCCESS"),

    /**
     * param error
     */
    PARAM_ERROR(61001, "FAIL, param error."),

    /**
     * already exist
     */
    REQ_TIME_EXCEED(61002, "Request frequency exceeded limit."),

    /**
     * not found in db
     */
    NOT_FOUND(61003, "FAIL, not found."),

    /**
     * not exist
     */
    TIME_RANGE_EXCEED(61004, "Time range exceeds limit."),

    /**
     * no permission
     */
    REQ_NUMBER_RANGE_EXCEED(61005, "Request number exceeded limit."),

    /**
     * not register
     */
    NOT_REGISTRY(61006, "FAIL, not registry."),

    /**
     * expires
     */
    EXPIRES(61007, "FAIL, expires."),

    /**
     * revoked
     */
    REVOKED(61008,"FAIL, revoked."),

    /**
     * serialized error
     */
    SERIALIZE_ERROR(61009,"FAIL, serialized error."),

    /**
     * serialized error
     */
    ALREADY_EXSIT(61010,"FAIL, already exsit."),


    /**
     * serialized error
     */
    ALREADY_AUDITPASS(61011,"FAIL, already on shelves."),

    QRCODE_EXPIRED(61012,"Qrcode expired."),

    NO_LOGIN_USERINFO(61013,"No login user info."),

    TOKEN_EXPIRED(61014,"Token expired."),

    TOKEN_UNMATCH(61015,"Token unmatch."),

    TOKEN_EMPTY(61016,"Token empty."),

    IN_BLACKADDRESS(61017,"Address in blacklist."),

    ADDRESS_TOOMANY(61018,"Too many addresses."),

    ADDRESS_ONTID_UNMATCH(61019,"Address unmatch ontid."),

    ADDRESS_FORMAT_INCORRECT(61020,"Address format incorrect."),





    /**
     * verify failed
     */
    VERIFY_FAILED(62001, "FAIL, verify fail."),

    /**
     * error occur whern create
     */
    CREATE_FAIL(62002, "FAIL, create fail."),

    /**
     * error occur whern communicate
     */
    COMM_FAIL(62003, "FAIL, communication fail."),

    /**
     * error occur whern operate file
     */
    FILE_ERROR(62004, "FAIL, file operate fail."),

    /**
     * error occur when operate db
     */
    DB_ERROR(62005, "FAIL, db operate fail."),

    VERIFY_SIGN_FAILED(62006,"Verify signature failed."),

    TX_ERROR(62007,"transaction error."),


    /**
     * inner error
     */
    INNER_ERROR(63001, "FAIL, inner error."),

    /**
     * exception
     */
    EXCEPTION(63002, "FAIL, exception.");

    private Integer code;
    private String msg;

    ErrorInfo(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer code() {
        return code;
    }

    public String desc() {
        return msg;
    }


}
