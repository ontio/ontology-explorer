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

import com.github.ontio.paramBean.Result;

import java.math.BigInteger;

/**
 * @author zhouq
 * @date 2018/2/27
 */
public class Helper {

    private static final String SEPARATOR = "\\|\\|";

    private static final BigInteger TWO_64 = BigInteger.ONE.shiftLeft(64);


    /**
     *
     * @param action
     * @param error
     * @param desc
     * @param version
     * @param rs
     * @return
     */
    public static Result result(String action, long error, String desc, String version, Object rs) {
        Result rr = new Result();
        rr.Error = error;
        rr.Action = action;
        rr.Desc = desc;
        rr.Version = version;
        rr.Result = rs;
        return rr;
    }

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
     * merge byte[] head and byte[] tail ->byte[head+tail] rs
     *
     * @param head
     * @param tail
     * @return byte[]
     */
    public static byte[] byteMerrage(byte[] head, byte[] tail) {
        byte[] temp = new byte[head.length + tail.length];
        System.arraycopy(head, 0, temp, 0, head.length);
        System.arraycopy(tail, 0, temp, head.length, tail.length);
        return temp;
    }


    /**
     * format ontId operation description
     *
     * @param inputStr
     * @return
     */
    public static String templateOntIdOperation(String inputStr) {

        StringBuffer descriptionSb = new StringBuffer();

        String[] desArray = inputStr.split(SEPARATOR);
        String action = desArray[0];
        if (OntIdEventDesType.REGISTERONTID.value().equals(action)) {
            descriptionSb.append("register OntId");
        } else if (OntIdEventDesType.PUBLICKEYOPE.value().equals(action)) {
            descriptionSb.append(desArray[1]);
            descriptionSb.append(" publicKey:");
            descriptionSb.append(desArray[3]);
        } else if (OntIdEventDesType.ATTRIBUTEOPE.value().equals(action)) {
            descriptionSb.append(desArray[1]);
            descriptionSb.append(" attribute:");
            String attrName = desArray[3];
            if(attrName.startsWith(ConstantParam.CLAIM)) {
                descriptionSb.append("claim");
            }else {
                descriptionSb.append(desArray[3]);
            }
        }

        return descriptionSb.toString();
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
