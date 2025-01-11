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

      let outputJson = '';
      const allKeys = new Set([...Object.keys(left), ...Object.keys(right)]);
      let result = {};

      allKeys.forEach(key => {
        if (left[key] !== right[key]) {
          if (right[key]) {
            result[key] = {
              value: right[key],
              type: 'changed'
            };
          } else {
            result[key] = {
              value: left[key],
              type: 'removed'
            };
          }

          if (!left[key] && right[key]) {
            result[key] = {
              value: right[key],
              type: 'added'
            };
          }
        } else {
          result[key] = {
            value: left[key],
            type: 'same'
          };
        }
      });

      setOutput(JSON.stringify(result, null, 2));
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
      <textarea
        value={output}
        readOnly
        rows="10"
        cols="50"
        placeholder="Output will appear here"
        style={{ marginTop: '20px', backgroundColor: '#f4f4f4', fontFamily: 'monospace' }}
      />
    </div>
  );
};

export default JsonCompare;
2. Updated src/App.css
Now, let’s apply some basic styling to ensure everything looks neat, especially with the vertical layout and text areas.

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
