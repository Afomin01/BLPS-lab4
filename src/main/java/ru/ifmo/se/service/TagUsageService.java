package ru.ifmo.se.service;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.ifmo.se.model.Tag;
import ru.ifmo.se.repository.TagRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TagUsageService {
    private final TagRepository tagRepository;

    public TagUsageService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @KafkaListener(topics = "tagUsageRequests", batch = "true", properties = {"fetch.min.bytes=500"})
    private void consumeTagUsageRequestsFromFirstPartition(@Payload List<String> tagNames) {
        log.info("Kafka listener get {} tags: {}", tagNames.size(), String.join(",", tagNames));

        Map<String, Integer> counters = new HashMap<>();
        for (String tagName : tagNames) {
            int count = counters.getOrDefault(tagName, 0);
            counters.put(tagName, count + 1);
        }

        for (var entry : counters.entrySet()) {
            String tagName = entry.getKey();
            int count = entry.getValue();

            Tag tag = tagRepository.findByName(tagName).orElseThrow(RuntimeException::new);
            tag.setCount(tag.getCount() + count);

            tagRepository.save(tag);
        }
    }
}
