package ee.a1nu.discord_dkp_bot.api.listener;

import ee.a1nu.discord_dkp_bot.api.service.ImageService;
import ee.a1nu.discord_dkp_bot.database.model.ImageData;
import jakarta.persistence.PreRemove;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ImageDataListener {
    private final ImageService imageService;

    public ImageDataListener(ImageService imageService) {
        this.imageService = imageService;
    }

    @PreRemove
    public void onImageDataDeleted(ImageData imageData) {
        try {
            imageService.deleteImage(imageData.getName(), imageData.getGuild().getSnowflake());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
