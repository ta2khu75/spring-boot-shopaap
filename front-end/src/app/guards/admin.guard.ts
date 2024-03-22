import {
  ActivatedRouteSnapshot,
  CanActivateFn,
  Router,
  RouterStateSnapshot,
} from '@angular/router';
import { StoreService } from '../service/store.service';
import { Injectable, inject } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class AdminGuard {
  constructor(private storeService: StoreService, private router: Router) {}
  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    debugger;
    const isTokenExpired = this.storeService.isTokenExpired();
    const isUserIdValid = this.storeService.getUserId() > 0;
    const isAdmin=this.storeService.getUser().role_id===2;
    debugger;
    if (!isTokenExpired && isUserIdValid && isAdmin) {
      return true;
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }
}
export const adminGuard: CanActivateFn = (route, state) => {
  debugger
  return inject(AdminGuard).canActivate(route, state);
};
