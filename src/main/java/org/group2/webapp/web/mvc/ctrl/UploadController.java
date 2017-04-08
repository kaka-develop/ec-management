/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.group2.webapp.web.mvc.ctrl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import javax.servlet.ServletContext;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author dfChicken
 */
@Controller
@RequestMapping("/file")
public class UploadController {

    @Autowired
    ServletContext servletContext;

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @GetMapping("/upload")
    public String uploadFileHandler() {
        // Forward to templates "/WEB-INF/upload.html"
        return "file/upload";
    }

    @GetMapping("/multiupload")
    public String uploadMultiFileHandler() {
        // Forward to templates "/WEB-INF/upload.html"
        return "file/multiupload";
    }

//    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @PostMapping("/upload")
    public @ResponseBody
    String uploadFileHandlerPOST(@RequestParam("name") String name, @RequestParam("file") MultipartFile file) {

        String actualPath = servletContext.getRealPath("");
        String fileLocation = actualPath + File.separator + "evidents";

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();

                File dir = new File(fileLocation);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                File serverFile = new File(dir.getAbsolutePath()
                        + File.separator + name);

                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                logger.info("Saved to: " + serverFile.getAbsolutePath());

                return "You successfully uploaded file=" + name;

            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }

    @PostMapping("/multiupload")
    public @ResponseBody
    String uploadMultipleFileHandler(@RequestParam("evidenceFiles") MultipartFile[] files) {

//        if (files.length != names.length) {
//            return "Mandatory information missing";
//        }
        String name;

        String message = "";
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            try {
                byte[] bytes = file.getBytes();

                long time = System.currentTimeMillis();
                java.sql.Timestamp timestmp = new java.sql.Timestamp(time);
                name = "evidence" + i + "_" + timestmp.getTime();
                
                 logger.info(FilenameUtils.getExtension(file.getOriginalFilename()));

                // Creating the directory to store file
                String actualPath = servletContext.getRealPath("");
                String fileLocation = actualPath + File.separator + "evidents";

                File dir = new File(fileLocation);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                // Create the file on server
                File serverFile = new File(dir.getAbsolutePath()
                        + File.separator + name);

                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                logger.info("Saved to: "
                        + serverFile.getAbsolutePath());

                message = message + "You successfully uploaded file=" + name;

            } catch (Exception e) {
                return "You failed to upload evidents" + e.getMessage();
            }
        }
        return message;
    }

}
