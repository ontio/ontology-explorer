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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @author zhouq
 * @date 2018/2/27
 */
public class Helper {

    private static final String SEPARATOR = "\\|\\|";

    /**
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
            if (attrName.startsWith(ConstantParam.CLAIM)) {
                descriptionSb.append("claim");
            } else {
                descriptionSb.append(desArray[3]);
            }
        } else if (OntIdEventDesType.RECOVERYOPE.value().equals(action)) {
            descriptionSb.append(desArray[1]);
            descriptionSb.append(" recovery:");
            descriptionSb.append(desArray[3]);
        }

        return descriptionSb.toString();
    }


    public static String currentMethod() {
        return new Exception("").getStackTrace()[1].getMethodName();
    }


    /**
     * get请求
     * @param urlParam
     * @param params
     * @return
     * @throws IOException
     */
    public static String sendGet(String urlParam, Map<String,Object> params) throws IOException {

        // 构建请求参数
        StringBuffer sbParams = new StringBuffer();
        if (params != null && params.size() > 0) {
            sbParams.append("?");
            for (Map.Entry<String, Object> e : params.entrySet()) {
                sbParams.append(e.getKey());
                sbParams.append("=");
                sbParams.append(e.getValue());
                sbParams.append("&");
            }
        }
        HttpURLConnection connection = null;
        OutputStream out = null;
        BufferedReader responseReader = null;
        // 发送请求
        try {
            URL url = new URL(urlParam + sbParams.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            //connection.setDoOutput(true);
            //设置超时时间
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            //connection.setRequestProperty("Content-type", "application/json");
            connection.connect();

            //获取输出
            StringBuffer sb = new StringBuffer();
            String readLine = "";
            responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            while ((readLine = responseReader.readLine()) != null) {
                sb.append(readLine);
            }

            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.disconnect();
                connection = null;
            }
            if (out != null) {
                out.close();
            }
            if (responseReader != null) {
                responseReader.close();
            }
        }

    }




}
