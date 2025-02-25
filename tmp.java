Backend (Spring Boot)
UploadApplication.java
java
Copy
Edit
package com.example.upload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UploadApplication {
    public static void main(String[] args) {
        SpringApplication.run(UploadApplication.class, args);
    }
}
controller/UploadController.java
java
Copy
Edit
package com.example.upload.controller;

import com.example.upload.dto.UploadRequest;
import com.example.upload.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/upload")
@CrossOrigin(origins = "http://localhost:3000")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> uploadData(@RequestBody UploadRequest request) {
        return uploadService.processUpload(request);
    }
}
service/UploadService.java
java
Copy
Edit
package com.example.upload.service;

import com.example.upload.dto.UploadRequest;
import com.example.upload.model.Lookup;
import com.example.upload.model.MapTable;
import com.example.upload.repository.LookupRepository;
import com.example.upload.repository.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UploadService {

    @Autowired
    private LookupRepository lookupRepository;

    @Autowired
    private MapRepository mapRepository;

    public ResponseEntity<Map<String, Object>> processUpload(UploadRequest request) {
        Map<String, Object> response = new HashMap<>();
        List<String> lookupDuplicates = new ArrayList<>();
        List<String> mapDuplicates = new ArrayList<>();

        for (Lookup lookup : request.getLookup()) {
            if (lookupRepository.existsById(lookup.getId())) {
                lookupDuplicates.add(lookup.getId());
            }
        }

        for (MapTable map : request.getMap()) {
            if (mapRepository.existsById(map.getPartnerId())) {
                mapDuplicates.add(map.getPartnerId());
            }
        }

        if (!lookupDuplicates.isEmpty() || !mapDuplicates.isEmpty()) {
            response.put("status", "error");
            response.put("lookupDuplicates", lookupDuplicates);
            response.put("mapDuplicates", mapDuplicates);
            return ResponseEntity.badRequest().body(response);
        }

        lookupRepository.saveAll(request.getLookup());
        mapRepository.saveAll(request.getMap());

        response.put("status", "success");
        response.put("message", "Data uploaded successfully!");
        return ResponseEntity.ok(response);
    }
}
model/Lookup.java
java
Copy
Edit
package com.example.upload.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Lookup {
    @Id
    private String id;
    private String fileType;
    private String fileFormat;
    private String templateName;
}
model/MapTable.java
java
Copy
Edit
package com.example.upload.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MapTable {
    @Id
    private String partnerId;
    private String dataType;
    private String dataFormat;
    private String autoEnabled;
}
dto/UploadRequest.java
java
Copy
Edit
package com.example.upload.dto;

import com.example.upload.model.Lookup;
import com.example.upload.model.MapTable;
import java.util.List;

public class UploadRequest {
    private List<Lookup> lookup;
    private List<MapTable> map;

    public List<Lookup> getLookup() { return lookup; }
    public List<MapTable> getMap() { return map; }
}
🎨 Frontend (React)
components/FileUpload.js
javascript
Copy
Edit
import React, { useState } from "react";
import * as XLSX from "xlsx";
import ApiService from "./ApiService";
import ConfirmDialog from "./ConfirmDialog";

const FileUpload = () => {
    const [file, setFile] = useState(null);
    const [data, setData] = useState({ lookup: [], map: [] });
    const [showConfirm, setShowConfirm] = useState(false);

    const handleFileChange = (event) => {
        const uploadedFile = event.target.files[0];
        setFile(uploadedFile);
        readExcel(uploadedFile);
    };

    const readExcel = (file) => {
        const reader = new FileReader();
        reader.onload = (e) => {
            const binaryStr = e.target.result;
            const workbook = XLSX.read(binaryStr, { type: "binary" });
            const sheet1 = XLSX.utils.sheet_to_json(workbook.Sheets["Sheet1"]);
            const sheet2 = XLSX.utils.sheet_to_json(workbook.Sheets["Sheet2"]);
            setData({ lookup: sheet1, map: sheet2 });
            setShowConfirm(true);
        };
        reader.readAsBinaryString(file);
    };

    const handleConfirm = async () => {
        const response = await ApiService.uploadData(data);
        alert(response.status === "success" ? "Upload successful!" : "Duplicate records found");
        setShowConfirm(false);
    };

    return (
        <div>
            <h3>Upload Excel File</h3>
            <input type="file" accept=".xlsx, .xls" onChange={handleFileChange} />
            <ConfirmDialog open={showConfirm} onClose={() => setShowConfirm(false)} onConfirm={handleConfirm} data={data} />
        </div>
    );
};

export default FileUpload;
components/ApiService.js
javascript
Copy
Edit
const ApiService = {
    uploadData: async (data) => {
        const response = await fetch("http://localhost:8080/upload", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data),
        });
        return response.json();
    }
};

export default ApiService;
components/ConfirmDialog.js
javascript
Copy
Edit
import React from "react";
import "../styles/ConfirmDialog.css";

const ConfirmDialog = ({ open, onClose, onConfirm, data }) => {
    if (!open || !data) return null;

    return (
        <div className="overlay">
            <div className="dialog">
                <h2>Confirm File Upload</h2>
                <button onClick={onClose} className="cancel">Cancel</button>
                <button onClick={onConfirm} className="confirm">Confirm</button>
            </div>
        </div>
    );
};

export default ConfirmDialog;
📜 Summary
✅ React frontend with file upload & preview using CSS Grid
✅ Spring Boot backend handling validation & database operations
✅ No @mui/material, replaced with plain HTML + CSS

Let me know if you need more modifications! 🚀


.overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
}

.dialog {
    background: white;
    padding: 20px;
    border-radius: 5px;
    width: 50%;
    text-align: center;
}

.grid-container {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 10px;
    margin-top: 10px;
}

.grid-header {
    font-weight: bold;
    display: contents;
}

.grid-row {
    display: contents;
}

.dialog-actions {
    margin-top: 20px;
}

.cancel {
    margin-right: 10px;
}



import React, { useState } from "react";
import "../styles/ConfirmDialog.css";

const ConfirmDialog = ({ open, onClose, onConfirm, data }) => {
    if (!open || !data) return null;

    const [activeTab, setActiveTab] = useState("lookup");

    return (
        <div className="overlay">
            <div className="dialog scrollable-page">
                <h2>Confirm File Upload</h2>
                
                <div className="tabs">
                    <button 
                        className={activeTab === "lookup" ? "active" : ""} 
                        onClick={() => setActiveTab("lookup")}
                    >LOOKUP Table</button>
                    <button 
                        className={activeTab === "map" ? "active" : ""} 
                        onClick={() => setActiveTab("map")}
                    >MAP Table</button>
                </div>

                {activeTab === "lookup" && (
                    <div className="grid-container scrollable">
                        <div className="grid-header">
                            <div>ID</div>
                            <div>FILE_TYPE</div>
                            <div>FILE_FORMAT</div>
                            <div>TEMPLATE_NAME</div>
                        </div>
                        {data.lookup.map((row, index) => (
                            <div className="grid-row" key={index}>
                                <div>{row.ID}</div>
                                <div>{row.FILE_TYPE}</div>
                                <div>{row.FILE_FORMAT}</div>
                                <div>{row.TEMPLATE_NAME}</div>
                            </div>
                        ))}
                    </div>
                )}

                {activeTab === "map" && (
                    <div className="grid-container scrollable">
                        <div className="grid-header">
                            <div>PARTNER_ID</div>
                            <div>DATA_TYPE</div>
                            <div>DATA_FORMAT</div>
                            <div>AUTO_ENABLED</div>
                        </div>
                        {data.map.map((row, index) => (
                            <div className="grid-row" key={index}>
                                <div>{row.PARTNER_ID}</div>
                                <div>{row.DATA_TYPE}</div>
                                <div>{row.DATA_FORMAT}</div>
                                <div>{row.AUTO_ENABLED}</div>
                            </div>
                        ))}
                    </div>
                )}

                <div className="dialog-actions">
                    <button onClick={onClose} className="cancel">Cancel</button>
                    <button onClick={onConfirm} className="confirm">Confirm</button>
                </div>
            </div>
        </div>
    );
};

export default ConfirmDialog;


/* Overlay to blur background */
.overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    align-items: center;
    justify-content: center;
}

/* Dialog container */
.dialog {
    background: white;
    padding: 20px;
    border-radius: 8px;
    width: 80%;
    max-width: 900px;
    max-height: 90vh;
    display: flex;
    flex-direction: column;
    overflow: auto;
    border: 1px solid black;
}

/* Tabs container */
.tabs {
    display: flex;
    justify-content: center;
    margin-bottom: 10px;
}

.tabs button {
    padding: 10px 20px;
    margin: 0 5px;
    border: none;
    background: #ddd;
    cursor: pointer;
    font-weight: bold;
}

.tabs button.active {
    background: black;
    color: white;
}

/* Table container */
.grid-container {
    display: flex;
    flex-direction: column;
    overflow-x: auto;
    border: 1px solid black;
    max-height: 60vh;
}

/* Table header */
.grid-header {
    display: grid;
    font-weight: bold;
    background: #f0f0f0;
    text-align: center;
}

.lookup-table {
    grid-template-columns: repeat(4, 1fr);
}

.map-table {
    grid-template-columns: repeat(7, 1fr);
}

/* Table row */
.grid-row {
    display: grid;
    text-align: center;
}

.grid-header div, .grid-row div {
    padding: 8px;
    border: 1px solid black;
}

/* Scrollable content */
.scrollable {
    overflow-y: auto;
}

/* Dialog actions */
.dialog-actions {
    display: flex;
    justify-content: flex-end;
    margin-top: 10px;
}

.dialog-actions button {
    padding: 8px 15px;
    margin-left: 10px;
    border: none;
    cursor: pointer;
    font-weight: bold;
}

.dialog-actions .cancel {
    background: red;
    color: white;
}

.dialog-actions .confirm {
    background: green;
    color: white;
}

/* Resizable window */
.resizable-dialog {
    resize: both;
    overflow: auto;
}




