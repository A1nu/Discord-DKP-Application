package ee.a1nu.discord_dkp_bot.database.service;

import ee.a1nu.discord_dkp_bot.api.util.Action;
import ee.a1nu.discord_dkp_bot.api.util.ChangeContext;
import ee.a1nu.discord_dkp_bot.database.model.GuildEntity;
import ee.a1nu.discord_dkp_bot.database.repository.GuildEntityRepository;
import org.springframework.stereotype.Service;

@Service
public class GuildEntityService {
    private final GuildEntityRepository guildEntityRepository;
    private final TransactionService transactionService;

    public GuildEntityService(GuildEntityRepository guildEntityRepository, TransactionService transactionService) {
        this.guildEntityRepository = guildEntityRepository;
        this.transactionService = transactionService;
    }

    public GuildEntity getGuildEntity(Long guildId) {
        if (guildEntityRepository.existsBySnowflake(guildId)) {
            return guildEntityRepository.findBySnowflake(guildId);
        }
        GuildEntity guildEntity = createGuildEntity(guildId);
        transactionService.saveTransaction(0L, ChangeContext.GUILD, guildId.toString(), Action.CREATE);

        return guildEntity;
    }

    private GuildEntity createGuildEntity(Long guildId) {
        GuildEntity guildEntity = new GuildEntity();
        guildEntity.setSnowflake(guildId);
        guildEntity.setCreatorSnowflake(0L);
        return guildEntityRepository.save(guildEntity);
    }

    public void updateGuild(GuildEntity guildEntity) {
        if (!guildEntity.isNew()) {
            guildEntity.setEditorSnowflake(0L);
            transactionService.saveTransaction(0L, ChangeContext.GUILD, guildEntity.getSnowflake().toString(), Action.UPDATE);
            guildEntityRepository.save(guildEntity);
        }
    }
}
