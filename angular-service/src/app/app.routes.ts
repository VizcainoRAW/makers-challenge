import { inject } from '@angular/core';
import { Routes } from '@angular/router';
import { authGuard, guestGuard } from './core/guards/auth.guard';
import { AuthService } from './core/services/auth.service';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: () => inject(AuthService).homeUrl() },
  {
    path: 'login',
    canActivate: [guestGuard],
    loadComponent: () => import('./pages/login/login').then((m) => m.Login),
  },
  {
    path: 'register',
    canActivate: [guestGuard],
    loadComponent: () => import('./pages/register/register').then((m) => m.Register),
  },
  {
    path: 'loans/new',
    canActivate: [authGuard],
    data: { role: 'CUSTOMER' },
    loadComponent: () => import('./pages/create-loan/create-loan').then((m) => m.CreateLoan),
  },
  {
    path: 'admin/loans',
    canActivate: [authGuard],
    data: { role: 'ADMIN' },
    loadComponent: () => import('./pages/admin-loans/admin-loans').then((m) => m.AdminLoans),
  },
  { path: '**', redirectTo: '' },
];
