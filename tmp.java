src/utils.js
javascript
Copy code
export const compareJsonObjects = (left, right) => {
  const compare = (leftObj, rightObj) => {
    const keys = new Set([
      ...Object.keys(leftObj),
      ...Object.keys(rightObj),
    ]);

    let outputHTML = '';

    keys.forEach((key) => {
      let leftValue = leftObj[key];
      let rightValue = rightObj[key];

      if (leftObj.hasOwnProperty(key) && !rightObj.hasOwnProperty(key)) {
        // Highlight missing in right side (red)
        outputHTML += `<div style="background-color: red;">"${key}": ${JSON.stringify(leftValue)}</div>`;
      } else if (!leftObj.hasOwnProperty(key) && rightObj.hasOwnProperty(key)) {
        // Highlight extra in right side (green)
        outputHTML += `<div style="background-color: green;">"${key}": ${JSON.stringify(rightValue)}</div>`;
      } else if (typeof leftValue === 'object' && typeof rightValue === 'object' && leftValue !== null && rightValue !== null) {
        // Recursively compare nested objects
        outputHTML += `<div>"${key}": {</div>`;
        outputHTML += compare(leftValue, rightValue); // Recursive comparison
        outputHTML += `<div>}</div>`;
      } else if (leftValue !== rightValue) {
        // Highlight value mismatch (brown)
        outputHTML += `<div style="background-color: brown;">"${key}": ${JSON.stringify(leftValue)} (left) vs. ${JSON.stringify(rightValue)} (right)</div>`;
      } else {
        // No change
        outputHTML += `<div>"${key}": ${JSON.stringify(leftValue)}</div>`;
      }
    });

    return outputHTML;
  };

  return compare(left, right);
};
Explanation of Updates:
Recursive Comparison for Nested Objects:

If both leftValue and rightValue are objects (and not null), we recursively call the compare function to handle nested objects (like address).
For each nested object, we highlight differences at the key-value level using the same logic (highlighting missing keys, extra keys, or mismatched values).
Value Mismatches:

If the values are different and they are not objects, we highlight the mismatch with a brown color.
Handling Other Cases:

Keys missing in one object and present in the other are highlighted in red (for the missing key) or green (for the extra key).
Adjusting CompareJson.jsx (No Changes Needed)
We don't need to make any changes to the CompareJson.jsx file, as the logic for rendering the output remains the same. The only difference is that the recursive comparison will now highlight the differences in nested objects as well.

src/components/CompareJson.jsx
jsx
Copy code
import React, { useState } from "react";
import { compareJsonObjects } from "../utils";

const CompareJson = () => {
  const [leftJson, setLeftJson] = useState("");
  const [rightJson, setRightJson] = useState("");
  const [outputJson, setOutputJson] = useState("");

  const handleCompare = () => {
    try {
      const leftData = JSON.parse(leftJson);
      const rightData = JSON.parse(rightJson);
      const result = compareJsonObjects(leftData, rightData);
      setOutputJson(result); // Set the HTML content to be injected
    } catch (error) {
      alert("Invalid JSON format.");
    }
  };

  return (
    <div className="compare-container">
      <div className="textarea-container">
        <textarea
          className="json-textarea"
          placeholder="Left JSON"
          value={leftJson}
          onChange={(e) => setLeftJson(e.target.value)}
        />
        <textarea
          className="json-textarea"
          placeholder="Right JSON"
          value={rightJson}
          onChange={(e) => setRightJson(e.target.value)}
        />
        <div
          className="json-textarea output"
          dangerouslySetInnerHTML={{ __html: outputJson }}
        />
      </div>
      <button className="compare-button" onClick={handleCompare}>
        Compare
      </button>
    </div>
  );
};

export default CompareJson;
CSS Adjustments (No Changes Needed)
The existing CSS is sufficient for highlighting and rendering the output, so no changes are required in the App.css.

src/App.css
css
Copy code
.compare-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

.textarea-container {
  display: flex;
  width: 80%;
  justify-content: space-between;
  margin-bottom: 20px;
}

.json-textarea {
  width: 30%;
  height: 300px;
  padding: 10px;
  font-family: monospace;
  font-size: 14px;
  border-radius: 4px;
  border: 1px solid #ccc;
  margin-right: 10px;
  white-space: pre-wrap;
  word-wrap: break-word;
}

.json-textarea.output {
  background-color: #f0f0f0;
  resize: none;
  width: 30%;
  height: 300px;
  overflow-y: auto;
  padding: 10px;
}

.compare-button {
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
  border: none;
  background-color: #4caf50;
  color: white;
  border-radius: 5px;
}
Example Input
Left JSON:
json
Copy code
{
  "name": "John",
  "age": 25,
  "address": {
    "street": "123 Main St",
    "city": "New York"
  }
}
Right JSON:
json
Copy code
{
  "name": "John",
  "age": 30,
  "address": {
    "street": "123 Main St",
    "city": "San Francisco"
  },
  "phone": "555-5555"
}
