package kz.am.imagehosting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletResponse;
import kz.am.imagehosting.utils.ImageUtils;
import kz.am.imagehosting.service.Image.ImageService;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;

@Controller
@RequestMapping(path="/images")
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(path="/{imageLocation}")
    private void getImage(@PathVariable(value="imageLocation") String imageLocation,
                                HttpServletResponse response) {
        String imageExt = ImageUtils.getImageExtension(imageLocation);
        response.setHeader("Content-Disposition",
                String.format("attachment; filename=%s.%s", imageLocation, imageExt));
        try (InputStream is = imageService.getImage(imageLocation);
            OutputStream os = response.getOutputStream()) {
            IOUtils.copy(is, os);
            response.flushBuffer();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("IOError writing file to output stream");
        }
    }
    
}
