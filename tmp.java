SpCount.js
javascript
Copy code
import React, { useEffect, useState } from 'react';

const SpCount = ({ onSpCountUpdate }) => {
  const [data, setData] = useState([]);

  useEffect(() => {
    // Fetch data from REST API
    fetch('YOUR_SP_COUNT_API_ENDPOINT')
      .then(response => response.json())
      .then(json => {
        setData(json.data);
        const sum = json.data.reduce((acc, item) => acc + parseInt(item.value, 10), 0);
        onSpCountUpdate(sum);  // Pass the sum to the parent
      })
      .catch(error => console.error('Error fetching SpCount data:', error));
  }, [onSpCountUpdate]);

  return null;  // No rendering is needed
};

export default SpCount;
WpCount.js
javascript
Copy code
import React, { useEffect, useState } from 'react';

const WpCount = ({ onWpCountUpdate }) => {
  const [data, setData] = useState([]);

  useEffect(() => {
    // Fetch data from REST API
    fetch('YOUR_WP_COUNT_API_ENDPOINT')
      .then(response => response.json())
      .then(json => {
        setData(json.data);
        const sum = json.data.reduce((acc, item) => acc + parseInt(item.value, 10), 0);
        onWpCountUpdate(sum);  // Pass the sum to the parent
      })
      .catch(error => console.error('Error fetching WpCount data:', error));
  }, [onWpCountUpdate]);

  return null;  // No rendering is needed
};

export default WpCount;
CountTable.js
javascript
Copy code
import React, { useState } from 'react';
import SpCount from './SpCount';
import WpCount from './WpCount';
import './CountTable.css';

const CountTable = () => {
  const [spCount, setSpCount] = useState(0);
  const [wpCount, setWpCount] = useState(0);

  const totalCount = spCount + wpCount;

  return (
    <div className="count-table-container">
      <h2>Item Count Summary</h2>
      <SpCount onSpCountUpdate={setSpCount} />
      <WpCount onWpCountUpdate={setWpCount} />
      <table className="count-table">
        <thead>
          <tr>
            <th>Category</th>
            <th>Count</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td className="category">spCount</td>
            <td>{spCount}</td>
          </tr>
          <tr>
            <td className="category">wpCount</td>
            <td>{wpCount}</td>
          </tr>
          <tr className="total-row">
            <td className="category">Total</td>
            <td>{totalCount}</td>
          </tr>
        </tbody>
      </table>
    </div>
  );
};

export default CountTable;
