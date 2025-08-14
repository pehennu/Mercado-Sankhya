import { Injectable } from '@angular/core';
import { Product } from '../../core/models/product.model';

@Injectable({ providedIn: 'root' })
export class CartService {
  items: { product: Product, quantity: number }[] = [];

  addItem(product: Product) {
    const found = this.items.find(i => i.product.id === product.id);
    if (found) {
      found.quantity++;
    } else {
      this.items.push({ product, quantity: 1 });
    }
  }

  removeItem(product: Product) {
    const found = this.items.find(i => i.product.id === product.id);
    if (found) {
      found.quantity--;
      if (found.quantity <= 0) {
        this.items = this.items.filter(i => i.product.id !== product.id);
      }
    }
  }

  getItems() {
    return this.items.map(i => ({ productId: i.product.id, quantity: i.quantity }));
  }

  subtotal() {
    return this.items.reduce((sum, i) => sum + i.product.price * i.quantity, 0);
  }

  clear() {
    this.items = [];
  }
}