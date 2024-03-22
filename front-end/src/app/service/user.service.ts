import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RegisterDto } from '../dtos/user/register.dto';
import { LoginDto } from '../dtos/user/login.dto';
import { environment } from '../environments/environment';
import { UserUpdateDto } from '../dtos/user/update.dto';
@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiRegister = `${environment.apiBaseUrl}/users/register`;
  private apiLogin = `${environment.apiBaseUrl}/users/login`;
  private apiUserDetail = `${environment.apiBaseUrl}/users/details`;
  private apiUpdate = `${environment.apiBaseUrl}/users/user-detail`;

  private apiConfig = {
    headers: this.createHeader(),
  };
  constructor(private http: HttpClient) {}
  private createHeader(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
   //   'Accept-Language': 'en',
    });
  }
  register(registerDto: RegisterDto): Observable<any> {
    return this.http.post(this.apiRegister, registerDto, this.apiConfig);
  }
  login(loginDto: LoginDto): Observable<any> {
    return this.http.post(this.apiLogin, loginDto, this.apiConfig);
  }
  update(userUpdate: UserUpdateDto, token:string): Observable<any> {
    return this.http.put(this.apiUpdate, userUpdate, {
      headers:new HttpHeaders({
        "Content-Type":"application/json",
        Authorization:`Bearer ${token}`
      })
    });
  }
  getUserDetails(token:string){
    return this.http.post(this.apiUserDetail, {
      headers:new HttpHeaders({
        "Content-Type":"application/json",
        Authorization:`Bearer ${token}`
      })
    })
  }
}
