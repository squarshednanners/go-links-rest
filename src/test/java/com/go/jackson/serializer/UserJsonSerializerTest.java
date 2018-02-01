package com.go.jackson.serializer;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.go.model.RoleEnum;
import com.go.model.User;

public class UserJsonSerializerTest {

	@Test
	public void testUserSerialization() throws Exception {
		Writer jsonWriter = new StringWriter();
		JsonGenerator jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
		SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
		new UserJsonSerializer().serialize(createUser(RoleEnum.USER), jsonGenerator, serializerProvider);
		jsonGenerator.flush();
		Assert.assertEquals("{\"username\":\"test@test.com\",\"active\":true,\"roleList\":[\"USER\"]}",
				jsonWriter.toString());
	}

	@Test
	public void testUserSerializationWithTwoRoles() throws Exception {
		Writer jsonWriter = new StringWriter();
		JsonGenerator jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
		SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
		new UserJsonSerializer().serialize(createUser(RoleEnum.USER, RoleEnum.ADMIN), jsonGenerator,
				serializerProvider);
		jsonGenerator.flush();
		Assert.assertEquals("{\"username\":\"test@test.com\",\"active\":true,\"roleList\":[\"USER\",\"ADMIN\"]}",
				jsonWriter.toString());
	}

	private User createUser(RoleEnum... roles) {
		User user = new User();
		user.setActive(true);
		user.setUsername("test@test.com");
		user.setPassword("pass");
		for (RoleEnum roleEnum : roles) {
			user.addRole(roleEnum);
		}
		return user;
	}

}
