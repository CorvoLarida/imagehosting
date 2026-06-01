package kz.am.imagehosting.json.deserializers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kz.am.imagehosting.domain.AuthRole;
import kz.am.imagehosting.service.AuthRoleService;

public class AuthRoleDesirializer extends JsonDeserializer<AuthRole> { 

    private AuthRoleService roleService;

    @Autowired
    public AuthRoleDesirializer(AuthRoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public AuthRole deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        // System.out.println("AuthRoleDesirializer");
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);
        // System.out.println(jsonNode);

        String nodeRoleName = jsonNode.get("name").asText();
        // System.out.print("nodeRoleName ");
        // System.out.println(nodeRoleName);
        String roleName = roleService.removeRolePrefix(nodeRoleName);
        // System.out.print("roleName ");
        // System.out.println(roleName);
        // AuthRole role = roleService.getRole(roleName);
        AuthRole role = new AuthRole();
        role.setName(roleName);
        // System.out.print("role ");
        // System.out.println(role);

        return role;
    }
    
}
