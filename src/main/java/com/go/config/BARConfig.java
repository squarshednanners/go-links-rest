package com.go.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.go.model.ActionLog;
import com.go.model.GoLink;
import com.go.model.Log;
import com.go.model.User;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class BARConfig {
	public JedisPoolConfig poolConfig() {
		final JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setTestOnBorrow(true);
		jedisPoolConfig.setMaxTotal(10);
		return jedisPoolConfig;
	}

	@Bean
	public JedisConnectionFactory connectionFactory() {
		return new JedisConnectionFactory(poolConfig());
	}

	@Bean
	public RedisTemplate<String, User> userRedisTemplate() {
		final RedisTemplate<String, User> template = new RedisTemplate<String, User>();
		overrideDefaultSerializers(template, new Jackson2JsonRedisSerializer<User>(User.class));
		return template;
	}

	@Bean
	public RedisTemplate<String, GoLink> goLinkRedisTemplate() {
		final RedisTemplate<String, GoLink> template = new RedisTemplate<String, GoLink>();
		overrideDefaultSerializers(template, new Jackson2JsonRedisSerializer<GoLink>(GoLink.class));
		return template;
	}

	@Bean
	public RedisTemplate<String, ActionLog> actionLogRedisTemplate() {
		final RedisTemplate<String, ActionLog> template = new RedisTemplate<String, ActionLog>();
		overrideDefaultSerializers(template, new Jackson2JsonRedisSerializer<ActionLog>(ActionLog.class));
		return template;
	}

	@Bean
	public RedisTemplate<String, Log> usageLogRedisTemplate() {
		final RedisTemplate<String, Log> template = new RedisTemplate<String, Log>();
		overrideDefaultSerializers(template, new Jackson2JsonRedisSerializer<Log>(Log.class));
		return template;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	void overrideDefaultSerializers(RedisTemplate template, RedisSerializer jacksonSerializer) {
		template.setConnectionFactory(connectionFactory());
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(jacksonSerializer);
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(jacksonSerializer);
		template.setStringSerializer(new StringRedisSerializer());
	}
}
