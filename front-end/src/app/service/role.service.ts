import { Injectable, OnInit } from '@angular/core';
import { environment } from '../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RoleService{
  private apiRoleGet=`${environment.apiBaseUrl}/roles`
  constructor(private http:HttpClient) { }
  getRoles():Observable<any>{
    return this.http.get<any[]>(this.apiRoleGet);
  }
}
