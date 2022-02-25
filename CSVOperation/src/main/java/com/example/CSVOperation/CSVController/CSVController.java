package com.example.CSVOperation.CSVController;

import com.example.CSVOperation.CSVEntity.CSVModel;
import com.example.CSVOperation.CSVException.ResourceNotFoundException;
import com.example.CSVOperation.CSVRepository.CSVService;
import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvFormat;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CSVController {
    @Autowired
    CSVService service;
    @PostMapping("/upload")
    public ResponseEntity<String> uploadData(@RequestParam("files") MultipartFile[] files) throws Exception {
        List<CSVModel> userdetails = new ArrayList<>();
        for (MultipartFile file : files) {
            String ext = FilenameUtils.getExtension(file.getOriginalFilename());
            if (ext == "") {
                return new ResponseEntity<>("Please attach csv file...", HttpStatus.NOT_ACCEPTABLE);
            }
            else {
                try {
                    InputStream inputStream = file.getInputStream();
                    CsvParserSettings setting = new CsvParserSettings();
                    setting.setHeaderExtractionEnabled(true);
                    setting.setDelimiterDetectionEnabled(true,'#');
                    CsvParser parser = new CsvParser(setting);
                    List<Record> parseAllRecords = parser.parseAllRecords(inputStream);
                    parseAllRecords.forEach(record -> {
                        CSVModel user = new CSVModel();
                        user.setName(record.getString("name"));
                        user.setAddress(record.getString("address"));
                        user.setEmail(record.getString("email"));
                        userdetails.add(user);
                    });
                    service.saveAll(userdetails);
                }
                catch(Exception e)
                {
                    return new ResponseEntity<>(ext+" files not supported...",HttpStatus.UNSUPPORTED_MEDIA_TYPE);
                }
            }


        }
        return new ResponseEntity<>("Upload Successful...", HttpStatus.OK);
    }
    @GetMapping("/employees")
    public ResponseEntity<List<CSVModel>> method()
    {
      try {
          List<CSVModel> employees=service.findAll();
          if(employees.isEmpty())
          {
              return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
          }
          return new ResponseEntity<>(employees,HttpStatus.OK);
      }
      catch(Exception e)
      {
          return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    @GetMapping("/employees/{id}")
    public CSVModel getUserById(@PathVariable (value="id") int userId)
    {
        return service.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found with id:"+userId));
    }
    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id)
    {
        try {
            service.deleteById(id);
            return new ResponseEntity<String>("deleted successfully",HttpStatus.OK);
        }
        catch(Exception e)
        {
            return new ResponseEntity<String>("No data",HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/updateemployee")
    public ResponseEntity<String> update(@RequestBody CSVModel model)
    {
        try{
            saveOrUpdate(model);
            return new ResponseEntity<String>("Updated Successfully",HttpStatus.OK);
        }
        catch(Exception e)
        {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }
    public void saveOrUpdate(CSVModel model)
    {
        service.save(model);
    }

}
