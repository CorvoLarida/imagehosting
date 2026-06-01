package kz.am.imagehosting.json.serializers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import kz.am.imagehosting.domain.AuthUser;
import kz.am.imagehosting.service.AuthRoleService;

public class AuthUserSerializer extends JsonSerializer<AuthUser> { 

    @Override
    public void serialize(AuthUser arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'serialize'");
    }

}
