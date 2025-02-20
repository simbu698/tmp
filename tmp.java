import React from "react";
import * as XLSX from "xlsx";
import { saveAs } from "file-saver";

const DownloadExcelTemplate = () => {
  const handleDownload = () => {
    // Define data for Sheet 1
    const sheet1Data = [
      ["ID", "Name", "Age"],
      [1, "Alice", 25],
      [2, "Bob", 30],
    ];

    // Define data for Sheet 2
    const sheet2Data = [
      ["Product ID", "Product Name", "Price"],
      [101, "Laptop", 1000],
      [102, "Mouse", 25],
    ];

    // Create worksheets
    const ws1 = XLSX.utils.aoa_to_sheet(sheet1Data);
    const ws2 = XLSX.utils.aoa_to_sheet(sheet2Data);

    // Create a workbook and append sheets
    const wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws1, "Users");
    XLSX.utils.book_append_sheet(wb, ws2, "Products");

    // Write and download
    const excelBuffer = XLSX.write(wb, { bookType: "xlsx", type: "array" });
    const blob = new Blob([excelBuffer], { type: "application/octet-stream" });
    saveAs(blob, "template.xlsx");
  };

  return <button onClick={handleDownload}>Download Excel Template</button>;
};

export default DownloadExcelTemplate;
