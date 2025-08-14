import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../models/product.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ProductService {
  private apiUrl = environment.apiUrl + '/api/v1/products';

  constructor(private http: HttpClient) {}

  getProducts(search: string, page: number, size: number): Observable<{content: Product[], totalElements: number}> {
    let params = new HttpParams()
      .set('search', search)
      .set('page', page)
      .set('size', size);
    return this.http.get<{content: Product[], totalElements: number}>(this.apiUrl, { params });
  }
}