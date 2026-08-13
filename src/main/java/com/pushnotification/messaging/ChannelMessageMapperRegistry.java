package com.pushnotification.messaging;

import com.pushnotification.domain.NotificationChannel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ChannelMessageMapperRegistry {

    private final Map<NotificationChannel, ChannelMessageMapper> mappersByChannel;

    public ChannelMessageMapperRegistry(List<ChannelMessageMapper> mappers) {
        this.mappersByChannel = mappers.stream()
                .collect(Collectors.toUnmodifiableMap(ChannelMessageMapper::channel, Function.identity()));
    }

    public ChannelMessageMapper get(NotificationChannel channel) {
        ChannelMessageMapper mapper = mappersByChannel.get(channel);
        if (mapper == null) {
            throw new IllegalArgumentException("Unsupported notification channel: " + channel);
        }
        return mapper;
    }
}
