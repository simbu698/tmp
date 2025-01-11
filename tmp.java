1. Updated src/JsonCompare.js
jsx
Copy code
// src/JsonCompare.js
import React, { useState } from 'react';
import './App.css';

const JsonCompare = () => {
  const [leftJson, setLeftJson] = useState('{"fruit": "Apple", "size": "Large", "color": "Red"}');
  const [rightJson, setRightJson] = useState('{"fruit": "Banana", "size": "Large", "price": "1$"}');
  const [output, setOutput] = useState('');

  const handleJsonChange = () => {
    try {
      const left = JSON.parse(leftJson);
      const right = JSON.parse(rightJson);

      const allKeys = new Set([...Object.keys(left), ...Object.keys(right)]);
      let result = {};

      allKeys.forEach((key) => {
        if (left[key] !== right[key]) {
          if (right[key] && left[key]) {
            result[key] = { value: right[key], type: 'changed' };
          } else if (left[key] && !right[key]) {
            result[key] = { value: left[key], type: 'removed' };
          } else if (!left[key] && right[key]) {
            result[key] = { value: right[key], type: 'added' };
          }
        } else {
          result[key] = { value: left[key], type: 'same' };
        }
      });

      let outputJson = '';
      Object.keys(result).forEach((key) => {
        const value = result[key].value;
        const type = result[key].type;

        // Highlighting the rows based on the type
        let highlightClass = '';
        if (type === 'added') highlightClass = 'added';
        if (type === 'removed') highlightClass = 'removed';
        if (type === 'changed') highlightClass = 'changed';
        if (type === 'same') highlightClass = 'same';

        outputJson += `<div class="${highlightClass}"><strong>${key}</strong>: ${JSON.stringify(value)}</div>`;
      });

      setOutput(outputJson);
    } catch (error) {
      setOutput('Invalid JSON input');
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', margin: '20px' }}>
      <textarea
        value={leftJson}
        onChange={(e) => setLeftJson(e.target.value)}
        rows="10"
        cols="50"
        placeholder="Enter Left JSON (Base)"
      />
      <textarea
        value={rightJson}
        onChange={(e) => setRightJson(e.target.value)}
        rows="10"
        cols="50"
        placeholder="Enter Right JSON (New)"
      />
      <button onClick={handleJsonChange}>Compare</button>
      <div
        style={{ marginTop: '20px' }}
        dangerouslySetInnerHTML={{ __html: output }}  // Rendering the highlighted HTML content
      />
    </div>
  );
};

export default JsonCompare;
2. Updated src/App.css
Update the CSS to ensure that each row (key-value pair) is highlighted in different colors based on its status.

css
Copy code
/* src/App.css */
.added {
  background-color: #a9c46c;
}

.removed {
  background-color: #ff9c73;
}

.changed {
  background-color: #e5d0ac;
}

.same {
  background-color: #d3d3d3;
}

textarea {
  margin: 10px 0;
  padding: 10px;
  font-family: monospace;
  font-size: 14px;
  resize: vertical;
}

button {
  padding: 10px;
  background-color: #4caf50;
  color: white;
  border: none;
  cursor: pointer;
  margin-bottom: 20px;
}

button:hover {
  background-color: #45a049;
}
