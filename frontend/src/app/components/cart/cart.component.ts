import { Component } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { CartService } from '../../core/services/cart.service';
import { OrderService } from '../../core/services/order.service';
import { MatDialog } from '@angular/material/dialog';
import { CheckoutDialogComponent } from '../checkout-dialog/checkout-dialog.component';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss'],
  standalone: true,
  imports: [CommonModule, CurrencyPipe]
})
export class CartComponent {
  constructor(
    public cartService: CartService,
    private orderService: OrderService,
    private dialog: MatDialog
  ) {}

  checkout() {
    this.orderService.checkout(this.cartService.getItems()).subscribe({
      next: (order) => {
        this.dialog.open(CheckoutDialogComponent, {
          data: { success: true, orderId: order.id }
        });
        this.cartService.clear();
      },
      error: (err) => {
        if (err.status === 409) {
          this.dialog.open(CheckoutDialogComponent, {
            data: { success: false, unavailableItems: err.error }
          });
        } else {
          alert('Erro ao finalizar pedido');
        }
      }
    });
  }
}
