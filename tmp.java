import React, { useState } from "react";
import * as XLSX from "xlsx";
import ConfirmDialog from "./ConfirmDialog";
import { uploadData } from "./ApiService";

const FileUpload = () => {
    const [fileData, setFileData] = useState(null);
    const [openDialog, setOpenDialog] = useState(false);

    const handleFileUpload = (e) => {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = (event) => {
                const data = new Uint8Array(event.target.result);
                const workbook = XLSX.read(data, { type: "array" });

                const sheet1 = XLSX.utils.sheet_to_json(workbook.Sheets[workbook.SheetNames[0]]);
                const sheet2 = XLSX.utils.sheet_to_json(workbook.Sheets[workbook.SheetNames[1]]);

                setFileData({ lookup: sheet1, map: sheet2 });
                setOpenDialog(true);
            };
            reader.readAsArrayBuffer(file);
        }
    };

    return (
        <div>
            <input type="file" accept=".xlsx, .xls" onChange={handleFileUpload} />
            <ConfirmDialog open={openDialog} onClose={() => setOpenDialog(false)} onConfirm={() => uploadData(fileData)} data={fileData} />
        </div>
    );
};

export default FileUpload;
