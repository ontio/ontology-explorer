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
import com.github.ontio.common.Address;
import com.github.ontio.io.BinaryReader;
import com.github.ontio.sdk.exception.SDKException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;

public class Helper {

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

    public static Boolean isNotEmptyOrNull(Object... params) {
        return !isEmptyOrNull(params);
    }

    /**
     * judge whether the string is in json format.
     *
     * @param str
     * @return
     */
    public static Boolean isJSONStr(String str) {
        try {
            JSONObject obj = JSONObject.parseObject(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String currentMethod() {
        return new Exception("").getStackTrace()[1].getMethodName();
    }

    public static String ontAddrToEthAddr(String ontAddr) throws SDKException {
        String payer = "";
        try {
            Address address = Address.decodeBase58(ontAddr);
            String hexAddress = com.github.ontio.common.Helper.toHexString(address.toArray());
            payer = ConstantParam.EVM_ADDRESS_PREFIX + hexAddress;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return payer;
    }

    public static String EthAddrToOntAddr(String ethAddr) {
        if (ethAddr.startsWith(ConstantParam.EVM_ADDRESS_PREFIX)) {
            ethAddr = ethAddr.substring(2);
        }
        Address parse = Address.parse(ethAddr);
        return parse.toBase58();
    }

    public static BigInteger parseInputDataNumber(String hexNumber, boolean isNative) throws IOException {
        if (hexNumber.length() < 2) {
            return BigInteger.ZERO;
        }
        if (isNative) {
            byte[] bytes = com.github.ontio.common.Helper.hexToBytes(hexNumber.substring(0, 2));
            BigInteger number = com.github.ontio.common.Helper.BigIntFromNeoBytes(bytes);
            int value = number.intValue();
            if (value > 80 && value <= 96) {
                return number.subtract(BigInteger.valueOf(80));
            } else if (value == 0) {
                return BigInteger.ZERO;
            } else if (value == 79) {
                return BigInteger.ONE.negate();
            } else {
                bytes = com.github.ontio.common.Helper.hexToBytes(hexNumber);
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                BinaryReader reader = new BinaryReader(bais);
                byte[] numberBytes = reader.readVarBytes();
                reader.close();
                bais.close();
                return com.github.ontio.common.Helper.BigIntFromNeoBytes(numberBytes);
            }
        } else {
            byte[] bytes = com.github.ontio.common.Helper.hexToBytes(hexNumber);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            BinaryReader reader = new BinaryReader(bais);
            byte[] numberBytes = reader.readVarBytes();
            reader.close();
            bais.close();
            return com.github.ontio.common.Helper.BigIntFromNeoBytes(numberBytes);
        }
    }

    public static String parseInputDataString(String hexString) throws IOException {
        byte[] bytes = com.github.ontio.common.Helper.hexToBytes(hexString);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        BinaryReader reader = new BinaryReader(bais);
        byte[] readVarBytes = reader.readVarBytes2();
        reader.close();
        bais.close();
        return new String(readVarBytes);
    }

}
