import React from 'react';
import './CountTable.css'; // Assuming you are using an external CSS file for styles

const CountTable = ({ vegetablesCount, fruitsCount }) => {
  // Calculate the total count
  const totalCount = vegetablesCount + fruitsCount;

  return (
    <div className="count-table-container">
      <h2>Item Count Summary</h2>
      <table className="count-table">
        <thead>
          <tr>
            <th>Category</th>
            <th>Count</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td className="category">Vegetables</td>
            <td>{vegetablesCount}</td>
          </tr>
          <tr>
            <td className="category">Fruits</td>
            <td>{fruitsCount}</td>
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







.count-table-container {
  width: 70%;
  margin: 20px auto;
  font-family: Arial, sans-serif;
}

h2 {
  text-align: center;
  color: #4caf50;
  margin-bottom: 20px;
}

.count-table {
  width: 100%;
  border-collapse: collapse;
  background-color: #f9f9f9;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.count-table th,
.count-table td {
  padding: 12px;
  text-align: center;
  border: 1px solid #ddd;
}

.count-table th {
  background-color: #4caf50;
  color: white;
  font-weight: bold;
}

.count-table td {
  font-size: 16px;
}

.count-table tr:hover {
  background-color: #f1f1f1;
}

.category {
  font-weight: bold;
  color: #333;
}

.total-row {
  background-color: #e0f7fa;
}

.total-row td {
  font-weight: bold;

}
