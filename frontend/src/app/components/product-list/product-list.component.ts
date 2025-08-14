import { Component, OnInit } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { ProductService } from '../../core/services/product.service';
import { Product } from '../../core/models/product.model';
import { Subject } from 'rxjs';
import { debounceTime, switchMap } from 'rxjs/operators';
import { CartService } from '../../core/services/cart.service';
import {PaginationComponent} from '../../shared/components/pagination/pagination.component';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss'],
  standalone: true,
  imports: [CommonModule, CurrencyPipe, PaginationComponent]
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  searchTerm = '';
  page = 0;
  size = 10;
  total = 0;
  private searchSubject = new Subject<string>();

  constructor(private productService: ProductService, private cartService: CartService) {}

  ngOnInit() {
    this.searchSubject.pipe(
      debounceTime(300),
      switchMap(term => this.productService.getProducts(term, this.page, this.size))
    ).subscribe(res => {
      this.products = res.content;
      this.total = res.totalElements;
    });
    this.search('');
  }

  search(term: string) {
    this.searchTerm = term;
    this.searchSubject.next(term);
  }

  onPageChange(page: number) {
    this.page = page;
    this.searchSubject.next(this.searchTerm);
  }

  addToCart(product: Product) {
    this.cartService.addItem(product);
  }
}
