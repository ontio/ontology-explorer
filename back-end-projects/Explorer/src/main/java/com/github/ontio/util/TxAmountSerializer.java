package com.github.ontio.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;

import static com.fasterxml.jackson.core.JsonToken.START_OBJECT;

@Slf4j
public class TxAmountSerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        log.info("serializeserializeserialize");

        jsonGenerator.writeString(bigDecimal.stripTrailingZeros().toPlainString());


/*        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("id");
        jsonGenerator.writeString("自定义");
        jsonGenerator.writeObjectField("buildDate",bigDecimal.divide(new BigDecimal("10")));
        jsonGenerator.writeEndObject();*/
    }

    @Override
    public void serializeWithType(BigDecimal value, JsonGenerator gen,
                                  SerializerProvider provider, TypeSerializer typeSer)
            throws IOException, JsonProcessingException {

        log.info("serializeWithTypeserializeWithTypeserializeWithType");
/*        typeSer.writeTypePrefixForObject(value, gen);
        serialize(value, gen, provider); // call your customized serialize method
        typeSer.writeTypeSuffixForObject(value, gen);*/
        WritableTypeId typeId = typeSer.typeId(value, START_OBJECT);
        typeSer.writeTypePrefix(gen, typeId);
        serialize(value, gen, provider); // call your customized serialize method
        typeSer.writeTypeSuffix(gen, typeId);
    }




}
