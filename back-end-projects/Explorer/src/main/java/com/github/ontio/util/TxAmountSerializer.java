package com.github.ontio.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
public class TxAmountSerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeString(bigDecimal.stripTrailingZeros().toPlainString());
    }

    @Override
    public void serializeWithType(BigDecimal value, JsonGenerator gen,
                                  SerializerProvider provider, TypeSerializer typeSer)
            throws IOException, JsonProcessingException {

        gen.writeStartArray();
        gen.writeString(BigDecimal.class.getName());
        //gen.writeStartObject();
        gen.writeString(value.stripTrailingZeros().toPlainString());
        //gen.writeEndObject();
        gen.writeEndArray();

/*        WritableTypeId typeId = typeSer.typeId(value, START_OBJECT);
        typeSer.writeTypePrefix(gen, typeId);
        serialize(value, gen, provider); // call your customized serialize method
        typeSer.writeTypeSuffix(gen, typeId);*/
    }


}
