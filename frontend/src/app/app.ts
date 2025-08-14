import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ProductListComponent } from './components/product-list/product-list.component';
import { CartComponent } from './components/cart/cart.component';
import { CheckoutDialogComponent } from './components/checkout-dialog/checkout-dialog.component';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    ProductListComponent,
    CartComponent,
    CheckoutDialogComponent,
    DatePipe
  ],
  templateUrl: './app.component.html',
  styleUrls: ['../styles/app.component.scss']
})
export class AppComponent {
  protected readonly title = signal('frontend');
  today = new Date();
}
