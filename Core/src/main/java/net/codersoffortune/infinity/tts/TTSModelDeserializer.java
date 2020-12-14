package net.codersoffortune.infinity.tts;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class TTSModelDeserializer extends StdDeserializer<TTSModel> {
    protected TTSModelDeserializer() {
        this(null);
    }

    protected TTSModelDeserializer(final Class<?> vc) {
        super(vc);
    }

    @Override
    public TTSModel deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
        final JsonNode node = parser.getCodec().readTree(parser);
        final ObjectMapper mapper = (ObjectMapper)parser.getCodec();
        if (node.has("front")) {
            return mapper.treeToValue(node, FrontBackModel.class);
        } else {
            return mapper.treeToValue(node, DecalBlockModel.class);
        }
    }
}
