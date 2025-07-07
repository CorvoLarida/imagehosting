package kz.am.imagehosting.service.Image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageServiceConfig {

    @Value("${app.image-service}")
    private String imageServiceName;

    private final ImageServiceFile imgsFile;
    private final ImageServiceS3 imgsS3;

    @Autowired
    public ImageServiceConfig(ImageServiceFile imgsFile, ImageServiceS3 imgsS3) {
        this.imgsFile = imgsFile;
        this.imgsS3 = imgsS3;
    }

    @Bean
    public ImageService imageService() {
        ImageService imgs = null;
        switch (this.imageServiceName) {
            case "img-service-file":
                imgs = imgsFile; 
                break;
            case "img-service-s3":
                imgs = imgsS3;
                break;
            default:
                imgs = imgsS3;
                break;
        }
        return imgs; 
    }
}

