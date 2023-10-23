package kz.am.imagehosting.controllers;


import kz.am.imagehosting.ImagehostingApplication;
import kz.am.imagehosting.domain.Image;
import kz.am.imagehosting.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class PeopleController {
    private ImageRepository imageRepository;

    @Autowired
    public PeopleController(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/images";

    @GetMapping("/all")
    public String getAll(Model model) {
        model.addAttribute("images", imageRepository.findAll());
        return "all";
    }

    @PostMapping("/upload")
    public String uploadImage(Model model, @RequestParam("image") MultipartFile file) throws IOException {
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
        System.out.println(file.getOriginalFilename());
        imageRepository.save(new Image(fileNames.toString()));
        model.addAttribute("msg", "Uploaded images: " + fileNames.toString());
        return "redirect:/all";
    }
}

