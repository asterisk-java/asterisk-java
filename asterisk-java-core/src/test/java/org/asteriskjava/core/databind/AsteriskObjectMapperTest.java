package org.asteriskjava.core.databind;

import org.asteriskjava.core.databind.annotation.AsteriskName;
import org.asteriskjava.core.databind.annotation.AsteriskSerialize;
import org.asteriskjava.core.databind.serializer.custom.ComaJoiningSerializer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.core.NewlineDelimiter.CRLF;
import static org.asteriskjava.core.databind.AsteriskObjectMapper.builder;
import static org.asteriskjava.core.databind.AsteriskObjectMapperTest.SimpleBean.AuthType.MD5;

class AsteriskObjectMapperTest {
    private final AsteriskObjectMapper asteriskObjectMapper = builder()
        .crlfNewlineDelimiter()
        .build();

    @Test
    void shouldGenerateSimpleBean() {
        //given
        SimpleBean bean = new SimpleBean();
        bean.setActionId("id-1");
        bean.setAuthType(MD5);
        bean.setCodecs(List.of("codec1", "codec2"));

        //when
        String string = asteriskObjectMapper.writeValue(bean);

        //then
        String expected = "Action: SimpleBean" + CRLF.getPattern();
        expected += "ActionID: id-1" + CRLF.getPattern();
        expected += "AuthType: MD5" + CRLF.getPattern();
        expected += "Codecs: codec1,codec2" + CRLF.getPattern();
        assertThat(string).isEqualTo(expected);
    }

    public static class SimpleBean {
        public enum AuthType {
            MD5,
        }

        private String actionId;

        private AuthType authType;

        private List<String> codecs;

        public String getAction() {
            return "SimpleBean";
        }

        @AsteriskName("ActionID")
        public String getActionId() {
            return actionId;
        }

        public void setActionId(String actionId) {
            this.actionId = actionId;
        }

        public AuthType getAuthType() {
            return authType;
        }

        public void setAuthType(AuthType authType) {
            this.authType = authType;
        }

        @AsteriskSerialize(ComaJoiningSerializer.class)
        public List<String> getCodecs() {
            return codecs;
        }

        public void setCodecs(List<String> codecs) {
            this.codecs = codecs;
        }
    }
}
