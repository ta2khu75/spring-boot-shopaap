import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { Observable } from 'rxjs';
import { Product } from '../model/product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiGetProduct=`${environment.apiBaseUrl}/products`;
  constructor(private http:HttpClient) { }
  getProduct(page:number, limit:number, keyword:string, category:number):Observable<Product[]>{
    const params=new HttpParams().set('page',page.toString()).set("limit",limit.toString()).set("category",category).set("keyword",keyword);
    return this.http.get<Product[]>(this.apiGetProduct,{params});
  }
  getDetailProduct(product_id:number){
    return this.http.get<Product>(`${environment.apiBaseUrl}/products/${product_id}`)
  }
  getProductIds(product_ids:number[]){
    const params=new HttpParams().set("ids",product_ids.join(","));
    return this.http.get<Product[]>(`${environment.apiBaseUrl}/products/by-ids`, {params})
  }
  getProductId(product_id:number){
    const params=new HttpParams().set("ids",product_id.toString());
    return this.http.get<Product[]>(`${environment.apiBaseUrl}/products/by-ids`, {params})
  }
}
