import { Injectable, inject } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivateFn,
  Router,
  RouterStateSnapshot,
} from '@angular/router';
import { StoreService } from '../service/store.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard {
  constructor(private storeService: StoreService, private router: Router) {}
  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    debugger
    const isTokenExpired = this.storeService.isTokenExpired();
    const isUserIdValid = this.storeService.getUserId() > 0;
    debugger;
    if (!isTokenExpired && isUserIdValid) {
      return true;
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }
}
export const authGuard: CanActivateFn = (route, state) => {
  debugger;
  return inject(AuthGuard).canActivate(route, state);
};
