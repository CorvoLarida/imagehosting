package kz.am.imagehosting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import kz.am.imagehosting.domain.Image;
import kz.am.imagehosting.utils.ImageUtils;
import kz.am.imagehosting.repository.ImageRepository;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;

@Controller
@RequestMapping(path="/images")
public class ImageController {

    private final ImageRepository imageRepository;

    @Autowired
    public ImageController(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @GetMapping(path="/{imageName}")
    private void getImage(@PathVariable(value="imageName") String imageName,
                                HttpServletResponse response) {
        // response.setContentType("image/jpeg");
        String imageExt = ImageUtils.getImageExtension(imageName);
        response.setHeader("Content-Disposition",
                String.format("attachment; filename=%s.%s", imageName, imageExt));
        String imageFilePath = ImageUtils.getImagePath(imageName);
        try(InputStream is = new FileInputStream(imageFilePath);
            OutputStream os = response.getOutputStream()) {
            IOUtils.copy(is, os);
            response.flushBuffer();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("IOError writing file to output stream");
        }
    }
    
}
