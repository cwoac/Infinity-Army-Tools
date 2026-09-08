package net.codersoffortune.infinity.metadata.unit;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

/**
 * spectables.*.items[].attrs[] entries are a tagged union: "weapon"/"skill"/"equip" share
 * ProfileItem's shape (id, q, extra), while "stat" has its own shape (stat, q, extra).
 */
public class SpecAttrDeserializer extends StdDeserializer<Object> {
    protected SpecAttrDeserializer() {
        this(null);
    }

    protected SpecAttrDeserializer(final Class<?> vc) {
        super(vc);
    }

    @Override
    public Object deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
        final JsonNode node = parser.getCodec().readTree(parser);
        final ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        final String type = node.get("type").asText();
        // "type" is only a discriminator; neither ProfileItem nor StatModifier declares it,
        // and this project's ObjectMapper fails on unrecognized properties.
        if (node instanceof ObjectNode) {
            ((ObjectNode) node).remove("type");
        }
        if ("stat".equals(type)) {
            return mapper.treeToValue(node, StatModifier.class);
        }
        // weapon, skill, equip all share ProfileItem's shape.
        return mapper.treeToValue(node, ProfileItem.class);
    }
}
