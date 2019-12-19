package jun.study.kafka.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jun.study.kafka.config.RunningConfig;
import jun.study.kafka.model.Production;
import jun.study.kafka.processor.support.BaseProcessor;
import jun.study.kafka.repository.ProductionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static jun.study.kafka.config.RunningConfig.PRODUCTION;

@Service
@RequiredArgsConstructor
public class ProductionProcessor extends BaseProcessor {

    private final ProductionRepository repository;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void streamProcess(KStream<String, String> stream) {
        stream.mapValues((ValueMapper<String, Long>) Long::valueOf)
                .mapValues(repository::findById)
                .mapValues((ValueMapper<Optional<Production>, Object>) value -> convert(value.get()))
                .to(PRODUCTION.desTopic());
    }

    @Override
    public RunningConfig runningConfig() {
        return PRODUCTION;
    }

    public String convert(Production production) {
        try {
            return objectMapper.writeValueAsString(production);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
