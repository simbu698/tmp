import React from "react";
import { Dialog, DialogTitle, DialogContent, DialogActions, Button, Table, TableHead, TableRow, TableCell, TableBody } from "@mui/material";

const ConfirmDialog = ({ open, onClose, onConfirm, data }) => {
    if (!data) return null;

    return (
        <Dialog open={open} onClose={onClose} maxWidth="md" fullWidth>
            <DialogTitle>Confirm File Upload</DialogTitle>
            <DialogContent>
                <h3>LOOKUP Table</h3>
                <Table size="small">
                    <TableHead>
                        <TableRow>
                            <TableCell>ID</TableCell><TableCell>FILE_TYPE</TableCell><TableCell>FILE_FORMAT</TableCell><TableCell>TEMPLATE_NAME</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {data.lookup.map((row, index) => (
                            <TableRow key={index}>
                                <TableCell>{row.ID}</TableCell>
                                <TableCell>{row.FILE_TYPE}</TableCell>
                                <TableCell>{row.FILE_FORMAT}</TableCell>
                                <TableCell>{row.TEMPLATE_NAME}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose} color="secondary">Cancel</Button>
                <Button onClick={onConfirm} color="primary">Confirm</Button>
            </DialogActions>
        </Dialog>
    );
};

export default ConfirmDialog;
