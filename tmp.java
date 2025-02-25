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


/* Table container */
.grid-container {
    display: flex;
    flex-direction: column;
    overflow: auto;
    border: 1px solid black;
    max-height: 65vh;
    max-width: 100%;
    resize: both;
}

/* Common grid styling */
.grid-header, .grid-row {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(120px, 1fr)); /* Ensure uniform column width */
    text-align: center;
    border-bottom: 1px solid black;
}

/* Header styling */
.grid-header {
    font-weight: bold;
    background: #f0f0f0;
    position: sticky;
    top: 0;
}

/* Row styling */
.grid-row div {
    padding: 8px;
    border-right: 1px solid black;
}

/* Ensure last column does not have an extra border */
.grid-row div:last-child, .grid-header div:last-child {
    border-right: none;
}















Got it! Your table names (lookup, map, etc.) are constant, but the data is coming dynamically from an uploaded Excel file. The ConfirmDialog.js will now handle any number of tables dynamically based on the parsed Excel data.

Updated ConfirmDialog.js (Dynamic Data Handling)

import React, { useState } from "react";
import "../styles/ConfirmDialog.css";

const ConfirmDialog = ({ open, onClose, onConfirm, data }) => {
    const tableNames = Object.keys(data); // Get table names dynamically
    const [activeTab, setActiveTab] = useState(tableNames[0]); // Default to first table

    if (!open || !data || tableNames.length === 0) return null;

    return (
        <div className="overlay">
            <div className="dialog scrollable-page resizable-dialog scalable large-dialog">
                <h2>Confirm File Upload</h2>

                {/* Scrollable Tabs Container */}
                <div className="tabs-container">
                    <div className="tabs">
                        {tableNames.map((tableName, index) => (
                            <button
                                key={index}
                                className={activeTab === tableName ? "active" : ""}
                                onClick={() => setActiveTab(tableName)}
                            >
                                {tableName.toUpperCase()}
                            </button>
                        ))}
                    </div>
                </div>

                {/* Dynamic Table Rendering */}
                <div className="grid-container scrollable scalable">
                    {data[activeTab]?.length > 0 ? (
                        <>
                            <div className="grid-header dynamic-table">
                                {Object.keys(data[activeTab][0]).map((column, index) => (
                                    <div key={index}>{column}</div>
                                ))}
                            </div>
                            {data[activeTab].map((row, index) => (
                                <div className="grid-row dynamic-table" key={index}>
                                    {Object.values(row).map((value, idx) => (
                                        <div key={idx}>{value}</div>
                                    ))}
                                </div>
                            ))}
                        </>
                    ) : (
                        <p className="no-data">No Data Available</p>
                    )}
                </div>

                {/* Action Buttons */}
                <div className="dialog-actions">
                    <button onClick={onClose} className="cancel">Cancel</button>
                    <button onClick={onConfirm} className="confirm">Confirm</button>
                </div>
            </div>
        </div>
    );
};

export default ConfirmDialog;

Updated ConfirmDialog.css

/* Tabs Container (Scrollable) */
.tabs-container {
    overflow-x: auto;
    white-space: nowrap;
    margin-bottom: 10px;
    padding-bottom: 5px;
    border-bottom: 2px solid #ccc;
}

.tabs {
    display: flex;
    gap: 8px;
}

.tabs button {
    padding: 10px 15px;
    border: none;
    background: #ddd;
    cursor: pointer;
    font-weight: bold;
    flex-shrink: 0;
}

.tabs button.active {
    background: black;
    color: white;
}

/* Table Container */
.grid-container {
    display: flex;
    flex-direction: column;
    overflow: auto;
    border: 1px solid black;
    max-height: 65vh;
    max-width: 100%;
    resize: both;
}

/* Grid Styling */
.grid-header, .grid-row {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
    text-align: center;
    border-bottom: 1px solid black;
}

.grid-header {
    font-weight: bold;
    background: #f0f0f0;
    position: sticky;
    top: 0;
}

.grid-row div {
    padding: 8px;
    border-right: 1px solid black;
}

.grid-row div:last-child, .grid-header div:last-child {
    border-right: none;
}

/* No Data Message */
.no-data {
    text-align: center;
    color: red;
    font-weight: bold;
    margin-top: 20px;
}

Key Enhancements:

✅ Dynamic Excel Data Handling – Parses table names and structures data dynamically.
✅ Handles Variable Columns – Reads column headers directly from Excel.
✅ Displays “No Data Available” Message – If a table has no records.
✅ Fully Scrollable & Resizable – Handles large tables efficiently.

Integration Example (Data from Excel)

Once the Excel file is parsed, pass the data like this:

const excelData = {
    lookup: [
        { ID: "ABC", FILE_TYPE: "PAY", FILE_FORMAT: "CREDIT", TEMPLATE_NAME: "TEMP1" },
        { ID: "DEF", FILE_TYPE: "PAY", FILE_FORMAT: "DEBIT", TEMPLATE_NAME: "TEMP2" }
    ],
    map: [
        { PARTNER_ID: "ABC", DATA_TYPE: "PAY", DATA_FORMAT: "CREDIT", AUTO_ENABLED: "Y" },
        { PARTNER_ID: "DEF", DATA_TYPE: "PAY", DATA_FORMAT: "DEBIT", AUTO_ENABLED: "Y" }
    ],
    table3: [],
    table4: [{ Col1: "D", Col2: "E", Col3: "F" }],
    table5: [{ Col1: "G", Col2: "H", Col3: "I" }]
};

Then use:

<ConfirmDialog open={true} onClose={handleClose} onConfirm={handleUpload} data={excelData} />

This will automatically generate tabs and tables from the Excel file data, regardless of how many tables and columns there are. Let me know if you need any further tweaks! 🚀


