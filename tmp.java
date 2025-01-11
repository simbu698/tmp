1. Modify compareJsonObjects to Return HTML
We need to update the compareJsonObjects function to generate HTML content that includes inline styles for highlighting.

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
        outputHTML += `<div style="background-color: red;">"${key}": ${JSON.stringify(leftValue)}</div>`;
      } else if (!leftObj.hasOwnProperty(key) && rightObj.hasOwnProperty(key)) {
        outputHTML += `<div style="background-color: green;">"${key}": ${JSON.stringify(rightValue)}</div>`;
      } else if (leftValue !== rightValue) {
        outputHTML += `<div style="background-color: brown;">"${key}": ${JSON.stringify(leftValue)} (left) vs. ${JSON.stringify(rightValue)} (right)</div>`;
      } else {
        outputHTML += `<div>"${key}": ${JSON.stringify(leftValue)}</div>`;
      }
    });

    return outputHTML;
  };

  return compare(left, right);
};
Explanation:
This function now returns HTML instead of a plain JSON object. The result is wrapped in <div> elements with inline style attributes to apply background colors based on the comparison:
Red for keys missing on the right side.
Green for keys missing on the left side.
Brown for mismatched values.
Default (white background) for matching values.
2. Modify the CompareJson Component to Use dangerouslySetInnerHTML
Now, we modify the CompareJson component to inject this HTML into the output textarea.

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
Explanation:
The outputJson now stores HTML content instead of plain JSON. We use dangerouslySetInnerHTML to insert the HTML into the output area.
This allows the highlighting to be rendered as part of the content in the output, using the colors defined in the compareJsonObjects function.
3. Style Adjustments
We'll make a small adjustment to the App.css to style the output area correctly and ensure the output area doesn't behave like a typical <textarea> (which doesn't support HTML rendering).

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
Explanation:
The .json-textarea.output class styles the output section to resemble a textarea, but allows HTML rendering.
The white-space: pre-wrap ensures that formatting (like new lines and spaces) is preserved, and word-wrap: break-word prevents text overflow.
Final Changes and Expected Behavior
After making these changes, the comparison should work as follows:

Text Areas: There are three text areas: Left JSON, Right JSON, and Output JSON.
Button: When you click the "Compare" button, the left and right JSONs are compared.
Color Highlights:
Red: Keys that exist in the left JSON but are missing in the right JSON.
Green: Keys that exist in the right JSON but are missing in the left JSON.
Brown: Keys that exist in both JSONs but have different values.
White: Keys that exist and have the same value in both JSONs.
Example:
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
Expected Output:
age will be highlighted in brown (value mismatch).
address.city will be highlighted in brown (value mismatch).
phone will be highlighted in green (extra-right).
The name field will remain with a white background (no changes).
Now, the Output JSON area will display the JSON with color highlighting as expected, instead of just raw JSON text.



