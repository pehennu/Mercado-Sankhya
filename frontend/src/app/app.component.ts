import { Component } from '@angular/core';
import {ProductListComponent} from './components/product-list/product-list.component';
import {CartComponent} from './components/cart/cart.component';
import {CheckoutDialogComponent} from './components/checkout-dialog/checkout-dialog.component';
import {DatePipe} from '@angular/common';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  imports: [
    ProductListComponent,
    CartComponent,
    CheckoutDialogComponent,
    DatePipe,
    RouterOutlet
  ],
  styleUrls: ['../styles/app.component.scss']
})
export class AppComponent {
  title = 'Market';
  today = new Date();
}
