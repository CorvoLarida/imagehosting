package kz.am.imagehosting.controllers;


import kz.am.imagehosting.domain.Image;
import kz.am.imagehosting.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class TestController {
    private final ImageRepository imageRepository;

    @Autowired
    public TestController(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/images";

    @GetMapping(path="/")
    private String index(){
        return "index";
    }

    @GetMapping(path="/all")
    public String getAll(Model model) {
        model.addAttribute("images", imageRepository.findAll());
        return "test/all";
    }

    @PostMapping(path="/upload")
    public String uploadImage(Model model, @RequestParam("image") MultipartFile file) {
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        try {
            Files.write(fileNameAndPath, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(file.getOriginalFilename());
//        imageRepository.save(new Image(fileNames.toString()));
        Image image = new Image();
        image.setImageLocation(fileNames.toString());
        imageRepository.save(image);
        model.addAttribute("msg", "Uploaded images: " + fileNames);
        return "redirect:/all";
    }

}

