import { Injectable } from '@angular/core';
import { User } from '../model/user';
import { JwtHelperService } from '@auth0/angular-jwt';
@Injectable({
  providedIn: 'root',
})
export class StoreService {
  private jwtHelperService = new JwtHelperService();
  constructor() {}
  addToCart(product_id: number, quantity: number = 1): void {
    debugger;
    let cart=this.getCart();
    if (cart.has(product_id)) {
      cart.set(product_id, cart.get(product_id)! + quantity);
    } else {
      cart.set(product_id, quantity);
    }
    this.saveChange(cart);
  }

  private getCartKey(): string {
    return `cart:${this.getUser().id}`;
  }
  private saveChange(cart:Map<number,number>): void {
    debugger;
    localStorage.setItem(
      this.getCartKey(),
      JSON.stringify(Array.from(cart.entries()))
    );
  }
  getCart() {
    let cart: Map<number, number> = new Map();
    const storeCart = localStorage.getItem(this.getCartKey());
    if (storeCart) {
      cart = new Map(JSON.parse(storeCart));
    }
    return cart;
  }
  getUser() {
    try {
      const user = localStorage.getItem('user');
      if (user == null || !user) {
        return null;
      }
      return JSON.parse(user!);
    } catch (error) {
      console.log('Error retrieving use response from local storage:', error);
      return null;
    }
  }
  setUser(user: User) {
    debugger;
    try {
      if (user == null || !user) {
        return;
      }
      localStorage.setItem('user', JSON.stringify(user));
      console.log('user saved to local storage');
    } catch (error) {
      console.log('error saving user local storage: ', error);
    }
  }
  removeUser() {
    localStorage.removeItem('user');
  }
  private readonly TOKEN_KEY = 'access_token';
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }
  setToken(token: string) {
    localStorage.setItem(this.TOKEN_KEY, token);
  }
  removeToken(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }
  isTokenExpired(): boolean {
    if (this.getToken() == null) {
      return false;
    }
    return this.jwtHelperService.isTokenExpired(this.getToken()!);
  }
  getUserId(): number {
    let userObject = this.jwtHelperService.decodeToken(this.getToken() ?? '');
    if (!userObject) {
      return 0;
    }
    return 'userId' in userObject ? parseInt(userObject['userId']) : 0;
  }
}
