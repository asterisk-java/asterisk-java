package org.asteriskjava.ami.databind.serializer.std;

import org.asteriskjava.ami.databind.AsteriskGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.core.NewlineDelimiter.CRLF;

class MapSerializerTest {
    private final AsteriskGenerator asteriskGenerator = new AsteriskGenerator(CRLF);

    @Test
    void shouldSerializeMap() {
        //given
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        MapSerializer mapSerializer = new MapSerializer();

        //when
        mapSerializer.serialize("Field", map, asteriskGenerator);

        //then
        String expected = "Field: key1=value1" + CRLF.getPattern();
        expected += "Field: key2=value2" + CRLF.getPattern();
        assertThat(asteriskGenerator.generate()).isEqualTo(expected);
    }
}
