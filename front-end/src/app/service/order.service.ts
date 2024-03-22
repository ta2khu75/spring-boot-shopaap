import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { OrderDto } from '../dtos/order.dto';
import { Observable } from 'rxjs';
import { OrderResponse } from '../response/order.response';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private apiOrder = `${environment.apiBaseUrl}/orders`;
  private apiOrderList = `${environment.apiBaseUrl}/orders/get-orders-by-keyword`;
  private apiUpdateOrder = `${environment.apiBaseUrl}/orders`;

  private apiConfig = {
    headers: this.createHeader(),
  };
  constructor(private http: HttpClient) {}
  private createHeader(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept-Language': 'en',
    });
  }
  order(orderDto: OrderDto): Observable<any> {
    return this.http.post(this.apiOrder, orderDto, this.apiConfig);
  }
  getOrder(order_id: number): Observable<any> {
    // const params=new HttpParams().set("order-id",order_id);
    return this.http.get<OrderResponse>(
      `${environment.apiBaseUrl}/orders/${order_id}`
    );
  }
  getAllOrders(
    keyword: string,
    page: number,
    limit: number
  ): Observable<OrderResponse[]> {
    const params = new HttpParams()
      .set('keyword', keyword)
      .set('page', page.toString())
      .set('limit', limit.toString());
    return this.http.get<any>(this.apiOrderList, { params });
  }
  updateOrder(orderResponse: OrderResponse): Observable<OrderResponse> {
    debugger;
    return this.http.put<any>(
      `${environment.apiBaseUrl}/orders/${orderResponse.id}`,
      orderResponse,
      this.apiConfig
    );
  }
  deleteOrder(id: number): Observable<any> {
    debugger;
    return this.http.delete(`${environment.apiBaseUrl}/orders/${id}`, {
      responseType: 'text',
    });
  }
}
