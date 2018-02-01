package com.go.jackson.serializer;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.go.model.User;

@JsonComponent
public class UserJsonSerializer extends JsonSerializer<User> {

	// Note that we must use this custom serializer, if we use inline annotations to
	// ignore the password element the the REDIS serializer ignores the password
	// field when saving user data

	@Override
	public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException, JsonProcessingException {
		jsonGenerator.writeStartObject();
		jsonGenerator.writeStringField("username", user.getUsername());
		jsonGenerator.writeBooleanField("active", user.getActive());
		jsonGenerator.writeArrayFieldStart("roleList");
		for (int i = 0; i < user.getRoleList().size(); i++) {
			jsonGenerator.writeString(user.getRoleList().get(i).toString());
		}
		jsonGenerator.writeEndArray();
		jsonGenerator.writeEndObject();
	}

}