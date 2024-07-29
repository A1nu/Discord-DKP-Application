package ee.a1nu.discord_dkp_bot.database.service;

import ee.a1nu.discord_dkp_bot.database.model.Weight;
import ee.a1nu.discord_dkp_bot.database.repository.WeightRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeightService {
    private final WeightRepository weightRepository;

    public WeightService(WeightRepository weightRepository) {
        this.weightRepository = weightRepository;
    }

    public List<Weight> getWeights() {
        return weightRepository.findAll();
    }

    public Weight getWeight(byte weight) {
        return weightRepository.findByWeight(weight);
    }
}
