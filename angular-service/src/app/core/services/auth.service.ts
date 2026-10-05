import { HttpClient } from '@angular/common/http';
import { Injectable, PLATFORM_ID, computed, inject, signal } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  CreateUserRequest,
  LoginRequest,
  LoginResponse,
  Role,
  SessionUser,
  UserResponse,
} from '../models/user.model';

const TOKEN_KEY = 'makers.accessToken';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly isBrowser = isPlatformBrowser(inject(PLATFORM_ID));

  /** Only ever holds a non-expired token; a timer clears it when it expires. */
  private readonly token = signal<string | null>(null);
  private expiryTimer?: ReturnType<typeof setTimeout>;

  readonly currentUser = computed<SessionUser | null>(() => {
    const token = this.token();
    return token ? decodeToken(token) : null;
  });
  readonly isAuthenticated = computed(() => this.currentUser() !== null);
  readonly isAdmin = computed(() => this.hasRole('ADMIN'));
  readonly isCustomer = computed(() => this.hasRole('CUSTOMER'));

  constructor() {
    this.setToken(this.readStoredToken());
  }

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${environment.apiUrl}/auth/login`, credentials)
      .pipe(tap((res) => this.setToken(res.accessToken)));
  }

  createUser(request: CreateUserRequest): Observable<UserResponse> {
    return this.http.post<UserResponse>(`${environment.apiUrl}/users`, request);
  }

  logout(): void {
    this.setToken(null);
  }

  getToken(): string | null {
    return this.token();
  }

  /** Landing page for the current session. */
  homeUrl(): string {
    if (this.isAdmin()) return '/admin/loans';
    if (this.isCustomer()) return '/loans/new';
    return '/login';
  }

  hasRole(role: Role): boolean {
    return this.currentUser()?.role === role;
  }

  private setToken(token: string | null): void {
    clearTimeout(this.expiryTimer);

    const user = token ? decodeToken(token) : null;
    const remainingMs = user ? user.expiresAt - Date.now() : 0;
    if (remainingMs <= 0) {
      token = null;
    }

    this.token.set(token);
    if (!this.isBrowser) return;

    if (token) {
      localStorage.setItem(TOKEN_KEY, token);
      this.expiryTimer = setTimeout(() => this.expireSession(), remainingMs);
    } else {
      localStorage.removeItem(TOKEN_KEY);
    }
  }

  private expireSession(): void {
    this.setToken(null);
    this.router.navigate(['/login'], { state: { expired: true } });
  }

  private readStoredToken(): string | null {
    return this.isBrowser ? localStorage.getItem(TOKEN_KEY) : null;
  }
}

function decodeToken(token: string): SessionUser | null {
  try {
    const payload = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
    const claims = JSON.parse(atob(payload));
    return { userId: claims.sub, role: claims.role, expiresAt: claims.exp * 1000 };
  } catch {
    return null;
  }
}
