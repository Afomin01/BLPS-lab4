package ru.ifmo.se.delegate;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class PublishTagUsageToKafka implements JavaDelegate {
    private final Producer<String, String> tagUsageRequestProducer;

    public PublishTagUsageToKafka(Producer<String, String> tagUsageRequestProducer) {
        this.tagUsageRequestProducer = tagUsageRequestProducer;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String tagName = (String) delegateExecution.getVariable("tag");
        ProducerRecord<String, String> record = new ProducerRecord<>("tagUsageRequests", tagName);
        tagUsageRequestProducer.send(record);

        System.out.println("publish " + tagName);
    }
}
