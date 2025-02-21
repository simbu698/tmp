package com.example.excelupload;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ExcelUploadController {

    private final RecordRepository recordRepository;

    public ExcelUploadController(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadExcel(@RequestParam("file") MultipartFile file) {
        if (!file.getOriginalFilename().endsWith(".xlsx")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Only .xlsx files are supported"));
        }

        List<Record> insertedRecords = new ArrayList<>();
        List<Record> existingRecords = new ArrayList<>();

        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) rowIterator.next(); // Skip header

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                String name = row.getCell(0).getStringCellValue();
                int age = (int) row.getCell(1).getNumericCellValue();
                Record record = new Record(name, age);

                if (recordRepository.existsByName(name)) {
                    existingRecords.add(record);
                } else {
                    insertedRecords.add(recordRepository.save(record));
                }
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error processing the file"));
        }

        return ResponseEntity.ok(Map.of("inserted", insertedRecords, "existing", existingRecords));
    }

    @GetMapping("/download-template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        String[] tableNames = {"Table1", "Table2"};
        String[][] headers = {{"Name", "Age"}, {"ID", "Description"}};

        for (int i = 0; i < tableNames.length; i++) {
            Sheet sheet = workbook.createSheet(tableNames[i]);
            Row headerRow = sheet.createRow(0);
            for (int j = 0; j < headers[i].length; j++) {
                headerRow.createCell(j).setCellValue(headers[i][j]);
            }
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=template.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}

package com.example.excelupload;

import javax.persistence.*;

@Entity
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int age;

    public Record() {}

    public Record(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}


package com.example.excelupload;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {
    boolean existsByName(String name);
}


package com.example.excelupload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExcelUploadApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExcelUploadApplication.class, args);
    }
}





@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

  Frontend - React.js
1. File Upload Component (FileUpload.js)
javascript
Copy
Edit
import React, { useState } from 'react';
import axios from 'axios';

const FileUpload = () => {
    const [file, setFile] = useState(null);
    const [response, setResponse] = useState(null);
    const [error, setError] = useState(null);

    const handleFileChange = (event) => {
        const selectedFile = event.target.files[0];
        if (selectedFile && selectedFile.name.endsWith('.xlsx')) {
            setFile(selectedFile);
            setError(null);
        } else {
            setError('Only .xlsx files are allowed');
        }
    };

    const handleUpload = async () => {
        if (!file) {
            setError('Please select a file');
            return;
        }

        const formData = new FormData();
        formData.append('file', file);

        try {
            const res = await axios.post('/api/upload', formData, {
                headers: { 'Content-Type': 'multipart/form-data' }
            });
            setResponse(res.data);
        } catch (err) {
            setError('Error uploading file');
        }
    };

    const handleDownloadTemplate = () => {
        window.location.href = '/api/download-template';
    };

    return (
        <div>
            <input type="file" onChange={handleFileChange} />
            <button onClick={handleUpload}>Upload</button>
            <button onClick={handleDownloadTemplate}>Download Empty Template</button>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {response && (
                <div>
                    <h3>Upload Result</h3>
                    <p>Inserted Records: {response.inserted?.length}</p>
                    <p>Existing Records: {response.existing?.length}</p>
                </div>
            )}
        </div>
    );
};

export default FileUpload;
2. App Component (App.js)
javascript
Copy
Edit
import React from 'react';
import FileUpload from './FileUpload';

function App() {
    return (
        <div>
            <h1>Excel File Upload</h1>
            <FileUpload />
        </div>
    );
}

export default App;
3. index.js
javascript
Copy
Edit
import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';

ReactDOM.render(<App />, document.getElementById('root'));
Setup & Run Instructions
Backend (Spring Boot)
Create a Spring Boot Project (Maven)
Add dependencies:
Spring Boot Web
Spring Data JPA
H2 Database
Apache POI
Run the Application:
sh
Copy
Edit
mvn spring-boot:run

{
  "name": "excel-upload",
  "version": "1.0.0",
  "private": true,
  "dependencies": {
    "axios": "^1.6.0",
    "react": "^18.2.0",
    "react-dom": "^18.2.0",
    "react-scripts": "5.0.1",
    "web-vitals": "^2.1.4"
  },
  "scripts": {
    "start": "react-scripts start",
    "build": "react-scripts build",
    "test": "react-scripts test",
    "eject": "react-scripts eject"
  },
  "eslintConfig": {
    "extends": [
      "react-app",
      "react-app/jest"
    ]
  },
  "browserslist": {
    "production": [
      ">0.2%",
      "not dead",
      "not op_mini all"
    ],
    "development": [
      "last 1 chrome version",
      "last 1 firefox version",
      "last 1 safari version"
    ]
  }
}
