package kz.am.imagehosting.service.Image;

import java.io.InputStream;

import kz.am.imagehosting.domain.Image;

public interface ImageService {
    public Image saveImage(byte[] fileBytes, String imageLocation);
    public InputStream getImage(String imageLocation);
}
