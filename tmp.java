import React, { useState } from 'react';
import './App.css';
import JsonDiffPatch from 'jsondiffpatch'; // Import the jsondiffpatch library

const App = () => {
  const [jsonFile1, setJsonFile1] = useState(null);
  const [jsonFile2, setJsonFile2] = useState(null);
  const [diff, setDiff] = useState(null);

  const handleFileUpload = (event, fileNumber) => {
    const file = event.target.files[0];

    if (file && file.type === 'application/json') {
      const reader = new FileReader();
      reader.onload = () => {
        try {
          const json = JSON.parse(reader.result);

          if (fileNumber === 1) {
            setJsonFile1(json);
          } else {
            setJsonFile2(json);
          }

          if (jsonFile1 && jsonFile2) {
            compareJsonFiles(jsonFile1, json);
          }
        } catch (e) {
          alert('Invalid JSON file');
        }
      };
      reader.readAsText(file);
    } else {
      alert('Please upload a valid JSON file');
    }
  };

  const compareJsonFiles = (json1, json2) => {
    const diffResult = JsonDiffPatch.diff(json1, json2);
    setDiff(diffResult);
  };

  return (
    <div className="container">
      <h1>Compare JSON Files</h1>
      <input type="file" onChange={(e) => handleFileUpload(e, 1)} />
      <input type="file" onChange={(e) => handleFileUpload(e, 2)} />
      
      {diff && (
        <div>
          <h2>Differences:</h2>
          <pre className="diff-output">{renderDiff(diff)}</pre>
        </div>
      )}
    </div>
  );
};

// Function to render the JSON diff in a color-coded format
const renderDiff = (diff) => {
  const jsondiffpatch = JsonDiffPatch.create();
  const html = jsondiffpatch.formatters.html.format(diff);
  return <div dangerouslySetInnerHTML={{ __html: html }} />;
};

export default App;




.diff-output {
  font-family: monospace;
  background-color: #f8f8f8;
  padding: 10px;
  border-radius: 5px;
  white-space: pre-wrap;
  word-wrap: break-word;
}

/* Color for removed content (deletion) */
.diff-deleted {
  background-color: #f8d7da;
  color: #721c24;
  text-decoration: line-through;
}

/* Color for added content (insertion) */
.diff-added {
  background-color: #d4edda;
  color: #155724;
}



