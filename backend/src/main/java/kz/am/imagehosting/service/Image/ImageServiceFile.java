package kz.am.imagehosting.service.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import kz.am.imagehosting.domain.Image;
import kz.am.imagehosting.repository.ImageRepository;
import kz.am.imagehosting.utils.ImageUtils;

@Service("img-service-file")
public class ImageServiceFile implements ImageService {

    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/images";

    private final ImageRepository imageRepository;

    @Autowired
    public ImageServiceFile(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image saveImage(byte[] fileBytes, String imageLocation) {
        System.out.println("FILE SAVE IMAGE");
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, imageLocation);
        try {
            Files.write(fileNameAndPath, fileBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Image uploadedImage = new Image();
        uploadedImage.setImageLocation(imageLocation);
        imageRepository.save(uploadedImage);
        return uploadedImage;
    }

    public InputStream getImage(String imageLocation) {
        System.out.println("FILE GET IMAGE");
        String imageFilePath = ImageUtils.getImagePath(imageLocation);
        try {
            return new FileInputStream(imageFilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        
    }
}
