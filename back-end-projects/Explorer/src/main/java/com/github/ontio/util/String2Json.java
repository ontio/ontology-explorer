package com.github.ontio.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/5/9
 */
public class String2Json extends JsonSerializer<String> {


    @Override
    public void serialize(String str, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeObject(JSON.parseObject(str));

/*        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("id");
        jsonGenerator.writeString("自定义");
        jsonGenerator.writeObjectField("buildDate",bigDecimal.divide(new BigDecimal("10")));
        jsonGenerator.writeEndObject();*/
    }



}
