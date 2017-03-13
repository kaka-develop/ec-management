/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.group2.webapp.web.mvc.ctrl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
       // Forward to "/WEB-INF/upload.html"
       return "file/upload";
   }
    
//    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @PostMapping("/upload")
    public @ResponseBody String uploadFileHandlerPOST(@RequestParam("name") String name, @RequestParam("file") MultipartFile file) {
        
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

                logger.info("Server File Location=" + serverFile.getAbsolutePath());

                return "You successfully uploaded file=" + name;
                
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }
}
