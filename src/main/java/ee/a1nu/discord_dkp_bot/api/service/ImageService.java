package ee.a1nu.discord_dkp_bot.api.service;

import com.nimbusds.jose.util.StandardCharset;
import ee.a1nu.discord_dkp_bot.api.dto.ImageDTO;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${application.dataDir}")
    String dataDir;

    public String saveBase64ImageToStorage(ImageDTO imageData, Long guildId) throws IOException {
        InputStream targetStream = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(imageData.imageData()));
        UUID uuid = UUID.randomUUID();
        String uniqueFileName = uuid + ".png";

        Path uploadPath = Path.of(System.getProperty("user.home"), dataDir, String.valueOf(guildId));
        Path filePath = uploadPath.resolve(uniqueFileName);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Files.copy(targetStream, filePath, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }

    public String getImageAsBase64(String imageName, Long guildId) throws IOException {
        Path filePath = Path.of(System.getProperty("user.home"), dataDir, guildId.toString(), imageName);
        if (Files.exists(filePath)) {
            byte[] imageBytes = Files.readAllBytes(filePath);

            return new String(Base64.encodeBase64(imageBytes), StandardCharset.UTF_8);
        }

        return null;
    }

    public void deleteImage(String imageName, Long guildId) throws IOException {
        Path filePath = Path.of(System.getProperty("user.home"), dataDir, guildId.toString(), imageName);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }

    public byte[] getImage(String guildId, String imageName) throws IOException {
        Path filePath = Path.of(System.getProperty("user.home"), dataDir, guildId, imageName);
        if (Files.exists(filePath)) {
            return Files.readAllBytes(filePath);
        } else {
            return null;
        }
    }
}
