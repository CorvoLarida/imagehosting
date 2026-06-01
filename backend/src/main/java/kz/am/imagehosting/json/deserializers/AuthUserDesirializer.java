package kz.am.imagehosting.json.deserializers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kz.am.imagehosting.domain.AuthUser;
import kz.am.imagehosting.service.AuthRoleService;

public class AuthUserDesirializer extends JsonDeserializer<AuthUser> { 

    private AuthRoleService roleService;

    @Autowired
    public AuthUserDesirializer(AuthRoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public AuthUser deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        // System.out.println("AuthUserDesirializer");
        // System.out.println(ctxt);
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);
        // System.out.println(jsonNode);

        return null;
    }

}
