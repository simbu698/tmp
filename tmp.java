import React from 'react';

const CountTable = ({ vegetablesCount, fruitsCount }) => {
  // Calculate the total count
  const totalCount = vegetablesCount + fruitsCount;

  return (
    <div>
      <h2>Count Table</h2>
      <table border="1">
        <thead>
          <tr>
            <th>Category</th>
            <th>Count</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>Vegetables</td>
            <td>{vegetablesCount}</td>
          </tr>
          <tr>
            <td>Fruits</td>
            <td>{fruitsCount}</td>
          </tr>
          <tr>
            <td>Total</td>
            <td>{totalCount}</td>
          </tr>
        </tbody>
      </table>
    </div>
  );
};

export default CountTable;
