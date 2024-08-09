package ee.a1nu.discord_dkp_bot.api.endpoint;

import ee.a1nu.discord_dkp_bot.api.service.ImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("api/image/{guildId}/{imageName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String guildId, @PathVariable String imageName) throws IOException {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageService.getImage(guildId, imageName));
    }
}
