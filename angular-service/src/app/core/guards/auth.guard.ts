import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Role } from '../models/user.model';
import { AuthService } from '../services/auth.service';

/** Requires a valid session; optionally restricts by `data: { role }`. */
export const authGuard: CanActivateFn = (route) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (!auth.isAuthenticated()) {
    return router.createUrlTree(['/login']);
  }
  const role = route.data['role'] as Role | undefined;
  if (role && !auth.hasRole(role)) {
    return router.parseUrl(auth.homeUrl());
  }
  return true;
};

/** Keeps logged-in users away from login / register. */
export const guestGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  return auth.isAuthenticated() ? inject(Router).parseUrl(auth.homeUrl()) : true;
};
