import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private apiCategory=`${environment.apiBaseUrl}/categories`;
  constructor(private http:HttpClient) { }
  getCagetory():Observable<any>{
    return this.http.get<any[]>(this.apiCategory);
  }
}
