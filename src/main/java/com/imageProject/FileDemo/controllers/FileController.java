package com.imageProject.FileDemo.controllers;

import com.imageProject.FileDemo.payload.FileResponse;
import com.imageProject.FileDemo.services.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @PostMapping("/upload")
    public ResponseEntity<FileResponse> fileUpload(@RequestParam("image")MultipartFile image){
        String fileName = null;
        try {
            fileName = this.fileService.uploadImage(path, image);
        }catch (IOException e){
            e.printStackTrace();
            return new ResponseEntity<>(new FileResponse(fileName,"Image is not uploaded due to error on server !!!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new FileResponse(fileName,"Image is uploaded successfully"), HttpStatus.OK);
    }

     //method to serve file
    @GetMapping(value="/profiles/{imageName}",produces=MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response) throws IOException {
        InputStream  resource = this.fileService.getResource(path,imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

    //url = http://localhost:8080/file/profiles/9c999ff8-b642-4850-81f6-ef00f480b727.png

}
