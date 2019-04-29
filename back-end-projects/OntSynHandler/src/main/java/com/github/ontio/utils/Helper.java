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


package com.github.ontio.utils;

import com.alibaba.fastjson.JSONObject;

import java.math.BigInteger;

/**
 * @author zhouq
 * @date 2018/2/27
 */
public class Helper {

    private static final String SEPARATOR = "\\|\\|";

    private static final BigInteger TWO_64 = BigInteger.ONE.shiftLeft(64);

    /**
     * check param whether is null or ''
     *
     * @param params
     * @return boolean
     */
    public static Boolean isEmptyOrNull(Object... params) {
        if (params != null) {
            for (Object val : params) {
                if ("".equals(val) || val == null) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * judge whether the string is in json format.
     *
     * @param str
     * @return
     */
    public static Boolean isJSONStr(String str) {
        try{
            JSONObject obj = JSONObject.parseObject(str);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public static String currentMethod() {
        return new Exception("").getStackTrace()[1].getMethodName();
    }


    public static String asUnsignedDecimalString(long l) {
        BigInteger b = BigInteger.valueOf(l);
        if (b.signum() < 0) {
            b = b.add(TWO_64);
        }
        return b.toString();
    }


}
