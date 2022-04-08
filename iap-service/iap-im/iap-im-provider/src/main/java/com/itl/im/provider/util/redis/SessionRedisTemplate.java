package com.itl.im.provider.util.redis;

import iap.im.api.sendDto.IapImSessionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class SessionRedisTemplate extends RedisTemplate<String, IapImSessionDto> implements RedisSerializer<IapImSessionDto> {

	private final static String CACHE_PREFIX = "session_";

	@Autowired
	public SessionRedisTemplate(RedisConnectionFactory connectionFactory) {
		StringRedisSerializer stringSerializer = new StringRedisSerializer();

		setKeySerializer(stringSerializer);
		setHashKeySerializer(stringSerializer);

		setValueSerializer(this);
		setHashValueSerializer(this);
		setConnectionFactory(connectionFactory);
		afterPropertiesSet();
	}

	@Override
	protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
		return new DefaultStringRedisConnection(connection);
	}

	public void save(IapImSessionDto session) {
		String key = CACHE_PREFIX + session.getAccount().toLowerCase();
		super.boundValueOps(key).set(session);
	}

	public IapImSessionDto get(String account) {
		String key = CACHE_PREFIX + account.toLowerCase();
		return super.boundValueOps(key).get();
	}

	private void remove(String account) {
		String key = CACHE_PREFIX + account.toLowerCase();
		super.delete(key);
	}

	public void saveOrRemove(String account, IapImSessionDto session) {
		if (session == null) {
			remove(account);
		} else {
			save(session);
		}
	}

	@Override
	public byte[] serialize(IapImSessionDto t) throws SerializationException {
		return t.getProtobufBody();
	}

	@Override
	public IapImSessionDto deserialize(byte[] bytes) throws SerializationException {

		try {
			return IapImSessionDto.decode(bytes);
		} catch (Exception ignore) {
			return null;
		}
	}

}
