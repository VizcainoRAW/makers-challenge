import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { authGuard, guestGuard } from './auth.guard';

function fakeToken(role: string): string {
  const payload = btoa(JSON.stringify({ sub: 'user-1', role, exp: Date.now() / 1000 + 3600 }));
  return `header.${payload}.signature`;
}

describe('auth guards', () => {
  let auth: AuthService;
  let http: HttpTestingController;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    auth = TestBed.inject(AuthService);
    http = TestBed.inject(HttpTestingController);
  });

  function loginAs(role: string) {
    auth.login({ loginIdentifier: 'a', password: 'b' }).subscribe();
    http.expectOne(() => true).flush({ accessToken: fakeToken(role), tokenType: 'Bearer ', expiresIn: 1 });
  }

  function runAuthGuard(role?: string) {
    const route = { data: role ? { role } : {} } as unknown as ActivatedRouteSnapshot;
    return TestBed.runInInjectionContext(() => authGuard(route, {} as RouterStateSnapshot));
  }

  function urlOf(result: unknown): string {
    return TestBed.inject(Router).serializeUrl(result as UrlTree);
  }

  it('redirects anonymous users to /login', () => {
    expect(urlOf(runAuthGuard('ADMIN'))).toBe('/login');
  });

  it('allows a user with the required role', () => {
    loginAs('ADMIN');
    expect(runAuthGuard('ADMIN')).toBe(true);
  });

  it('sends a customer away from admin pages', () => {
    loginAs('CUSTOMER');
    expect(urlOf(runAuthGuard('ADMIN'))).toBe('/loans/new');
  });

  it('sends logged-in users away from guest pages', () => {
    loginAs('ADMIN');
    const result = TestBed.runInInjectionContext(() =>
      guestGuard({} as ActivatedRouteSnapshot, {} as RouterStateSnapshot),
    );
    expect(urlOf(result)).toBe('/admin/loans');
  });
});
