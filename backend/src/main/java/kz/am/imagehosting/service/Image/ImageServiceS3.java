package kz.am.imagehosting.service.Image;

import java.io.InputStream;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kz.am.imagehosting.config.S3Config;
import kz.am.imagehosting.domain.Image;
import kz.am.imagehosting.repository.ImageRepository;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service("img-service-s3")
public class ImageServiceS3 implements ImageService {
    private final S3Client s3Client;
    private final S3Config s3Config;
    private final ImageRepository imageRepository;

    @Autowired
    public ImageServiceS3(S3Client s3Client, S3Config s3Config, ImageRepository imageRepository) {
        this.s3Client = s3Client;
        this.s3Config = s3Config;
        this.imageRepository = imageRepository;
    }

    public Image saveImage(byte[] fileBytes, String imageLocation) {
        System.out.println("S3 SAVE IMAGE");
        try {
            String bucket = s3Config.getBucket();
            String key = imageLocation;
            PutObjectResponse putObjectResult = s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .contentLength((long) fileBytes.length)
                        .build(),
                RequestBody.fromBytes(fileBytes)
            );

            System.out.println("putObjectResult");
            System.out.println(putObjectResult);
            final URL reportUrl = s3Client.utilities().getUrl(GetUrlRequest.builder().bucket(bucket).key(key).build());
            System.out.println("reportUrl");
            System.out.println(reportUrl.toString());

            Image uploadedImage = new Image();
            uploadedImage.setImageLocation(key);
            imageRepository.save(uploadedImage);
            return uploadedImage;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public InputStream getImage(String imageLocation) {
        System.out.println("S3 GET IMAGE");
        String bucket = s3Config.getBucket();
        ResponseInputStream<GetObjectResponse> resp = s3Client.getObject(
            GetObjectRequest.builder()
            .bucket(bucket)
            .key(imageLocation)
            .build()
        );
        return resp;
    }
}
