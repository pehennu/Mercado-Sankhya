import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { OrderItem } from '../models/order-item.model';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private apiUrl = environment.apiUrl + '/api/v1/orders';

  constructor(private http: HttpClient) {}

  checkout(items: OrderItem[]): Observable<any> {
    return this.http.post(this.apiUrl, { items });
  }
}