package com.github.ontio.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

public class TxAmountSerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(bigDecimal.stripTrailingZeros().toPlainString());


/*        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("id");
        jsonGenerator.writeString("自定义");
        jsonGenerator.writeObjectField("buildDate",bigDecimal.divide(new BigDecimal("10")));
        jsonGenerator.writeEndObject();*/
    }

/*
    @Override
    public void serializeWithType(BigDecimal value, JsonGenerator gen,
                                  SerializerProvider provider, TypeSerializer typeSer)
            throws IOException, JsonProcessingException {

        typeSer.writeTypePrefixForObject(value, gen);
        serialize(value, gen, provider); // call your customized serialize method
        typeSer.writeTypeSuffixForObject(value, gen);
    }
*/




}
