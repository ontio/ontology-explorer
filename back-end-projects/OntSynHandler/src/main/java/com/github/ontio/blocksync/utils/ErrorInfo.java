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



package com.github.ontio.blocksync.utils;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/2/27
 */
public enum ErrorInfo {

    /**
     * success
     */
    SUCCESS(0L, "SUCCESS"),

    /**
     * param error
     */
    PARAM_ERROR(61001L, "FAIL, param error."),

    /**
     * already exist
     */
    ALREADY_EXIST(61002L, "FAIL, already exist."),

    /**
     * not found in db
     */
    NOT_FOUND(61003L, "FAIL, not found."),

    /**
     * not exist
     */
    NOT_EXIST(61004L, "FAIL, not exist."),

    /**
     * no permission
     */
    NO_PERMISSION(61005L, "FAIL, no permission"),

    /**
     * not register
     */
    NOT_REGISTRY(61006L, "FAIL, not registry."),

    /**
     * expires
     */
    EXPIRES(61007L, "FAIL, expires."),

    /**
     * revoked
     */
    REVOKED(61008L,"FAIL, revoked."),

    /**
     * serialized error
     */
    SERIALIZE_ERROR(61009L,"FAIL, serialized error."),




    /**
     * verify failed
     */
    VERIFY_FAILED(62001L, "FAIL, verify fail."),

    /**
     * error occur whern create
     */
    CREATE_FAIL(62002L, "FAIL, create fail."),

    /**
     * error occur whern communicate
     */
    COMM_FAIL(62003L, "FAIL, communication fail."),

    /**
     * error occur whern operate file
     */
    FILE_ERROR(62004L, "FAIL, file operate fail."),

    /**
     * error occur when operate db
     */
    DB_ERROR(62005L, "FAIL, db operate fail."),




    /**
     * inner error
     */
    INNER_ERROR(63001L, "FAIL, inner error."),

    /**
     * exception
     */
    EXCEPTION(63002L, "FAIL, exception.");

    private long errorCode;
    private String errorDesc;

    ErrorInfo(long errorCode, String errorDesc) {
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
    }

    public long code() {
        return errorCode;
    }

    public String desc() {
        return errorDesc;
    }


}
