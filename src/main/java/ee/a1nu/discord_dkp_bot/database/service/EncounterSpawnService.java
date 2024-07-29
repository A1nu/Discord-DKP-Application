package ee.a1nu.discord_dkp_bot.database.service;

import ee.a1nu.discord_dkp_bot.database.model.EncounterSpawn;
import ee.a1nu.discord_dkp_bot.database.repository.EncounterSpawnRepository;
import org.springframework.stereotype.Service;

@Service
public class EncounterSpawnService {

    private final EncounterSpawnRepository encounterSpawnRepository;

    public EncounterSpawnService(EncounterSpawnRepository encounterSpawnRepository) {
        this.encounterSpawnRepository = encounterSpawnRepository;
    }

    public void deleteEncounterSpawn(EncounterSpawn encounterSpawn) {
        encounterSpawnRepository.delete(encounterSpawn);
    }
}
