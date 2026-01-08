package fr.eni.projeteniencheres.bll;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageStorageService {

    @Value("${app.upload.ventes-path}")
    private String ventesPath;

    public String saveVenteImage(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            return "";
        }

        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + "." + extension;

        Path uploadDir = Paths.get(ventesPath);
        Files.createDirectories(uploadDir);

        Path destination = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), destination);

        return "/ventes/" + filename;
    }
}

