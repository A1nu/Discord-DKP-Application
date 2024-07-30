package ee.a1nu.discord_dkp_bot.database.service;

import ee.a1nu.discord_dkp_bot.api.dto.EncounterDTO;
import ee.a1nu.discord_dkp_bot.database.model.Encounter;
import ee.a1nu.discord_dkp_bot.database.repository.EncounterRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class EncounterService {
    private final EncounterRepository encounterRepository;

    public EncounterService(EncounterRepository encounterRepository) {
        this.encounterRepository = encounterRepository;
    }

    public Encounter saveEncounter(Encounter encounter) {
        return encounterRepository.save(encounter);
    }

    public Encounter getEncounter(UUID id) {
        return encounterRepository.findById(id).orElse(new Encounter());
    }

    public void deleteEncounter(UUID id) {
        encounterRepository.deleteById(id);
    }

    public boolean isExistByName(EncounterDTO dto, Long guildId) {
        return encounterRepository.existsByGuild_SnowflakeAndName(guildId, dto.name());
    }

    public Set<Encounter> getScheduledEncounters(Long guildId) {
        return encounterRepository.findAllByGuild_SnowflakeAndScheduledEncounter(guildId, true);
    }
}
