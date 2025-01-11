{
  "name": "compare-json-app",
  "version": "0.1.0",
  "private": true,
  "dependencies": {
    "react": "^18.2.0",
    "react-dom": "^18.2.0",
    "react-scripts": "5.0.1"
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






Folder Structure:
java
Copy code
compare-json-app/
├── public/
│   ├── index.html
├── src/
│   ├── components/
│   │   ├── CompareJson.jsx
│   ├── App.js
│   ├── index.js
│   ├── App.css
│   └── utils.js
├── package.json
└── .gitignore
Step 1: Setup the React Project
First, make sure you have node.js and npm installed. You can create a React app using create-react-app:

bash
Copy code
npx create-react-app compare-json-app
cd compare-json-app
npm start
Once the app is set up, you can organize your components and files according to the folder structure mentioned above.

Step 2: Create the CompareJson Component
This component will contain three text areas (left, right, and output) and the button for comparison.

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
    const leftData = JSON.parse(leftJson);
    const rightData = JSON.parse(rightJson);
    const result = compareJsonObjects(leftData, rightData);
    setOutputJson(JSON.stringify(result, null, 2));
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
        <textarea
          className="json-textarea output"
          placeholder="Output JSON"
          value={outputJson}
          onChange={(e) => setOutputJson(e.target.value)}
          readOnly
        />
      </div>
      <button className="compare-button" onClick={handleCompare}>
        Compare
      </button>
    </div>
  );
};

export default CompareJson;
Step 3: Add the compareJsonObjects Utility Function
This function compares two JSON objects and returns a modified version of the output JSON with the necessary highlights.

src/utils.js
javascript
Copy code
export const compareJsonObjects = (left, right) => {
  const compare = (leftObj, rightObj) => {
    const result = {};

    const keys = new Set([
      ...Object.keys(leftObj),
      ...Object.keys(rightObj),
    ]);

    keys.forEach((key) => {
      if (leftObj.hasOwnProperty(key) && !rightObj.hasOwnProperty(key)) {
        result[key] = { value: leftObj[key], status: "missing-right" };
      } else if (!leftObj.hasOwnProperty(key) && rightObj.hasOwnProperty(key)) {
        result[key] = { value: rightObj[key], status: "extra-right" };
      } else if (leftObj[key] !== rightObj[key]) {
        result[key] = { value: leftObj[key], status: "value-mismatch" };
      } else {
        result[key] = { value: leftObj[key], status: "no-change" };
      }
    });

    return result;
  };

  return compare(left, right);
};
Step 4: Style the Components
Create a CSS file for styling the components and handling the different highlight colors for the output JSON.

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
}

.json-textarea.output {
  background-color: #f0f0f0;
  resize: none;
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

.json-textarea output .missing-right {
  background-color: red;
}

.json-textarea output .extra-right {
  background-color: green;
}

.json-textarea output .value-mismatch {
  background-color: brown;
}

.json-textarea output .no-change {
  background-color: white;
}
Step 5: Integrate the Component in the Main App
You can now integrate the CompareJson component into the main App component.

src/App.js
jsx
Copy code
import React from "react";
import CompareJson from "./components/CompareJson";
import './App.css';

const App = () => {
  return (
    <div className="App">
      <h1>JSON Comparator</h1>
      <CompareJson />
    </div>
  );
};

export default App;
Step 6: Render the App
Finally, render the App component in index.js.

src/index.js
jsx
Copy code
import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';

ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.getElementById('root')
);
Final Adjustments and Running the App
Ensure that the app is running correctly by typing valid JSON objects into the "Left JSON" and "Right JSON" text areas.
When you click the "Compare" button, the app will compare the two JSON objects and highlight differences in the "Output JSON" text area.
You can adjust the comparison logic if needed (e.g., for nested objects, arrays, etc.).
Additional Features:
Validation: Add validation to handle invalid JSON format inputs.
Nested JSON Comparison: Modify the compareJsonObjects function to handle nested objects, deep equality checking, and array comparison.
Example Input:
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
