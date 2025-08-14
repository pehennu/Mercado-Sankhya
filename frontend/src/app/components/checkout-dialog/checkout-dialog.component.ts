import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import {CommonModule} from '@angular/common';

export interface CheckoutDialogData {
  success: boolean;
  orderId?: number;
  unavailableItems?: { productId: number; available: number }[];
}

@Component({
  selector: 'app-checkout-dialog',
  templateUrl: './checkout-dialog.component.html',
  styleUrls: ['./checkout-dialog.component.scss'],
  standalone: true,
  imports: [CommonModule]
})
export class CheckoutDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<CheckoutDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: CheckoutDialogData
  ) {}

  close(): void {
    this.dialogRef.close();
  }
}
