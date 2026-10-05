import { TestBed } from '@angular/core/testing';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { Router, provideRouter } from '@angular/router';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { environment } from '../../../environments/environment';
import { authInterceptor } from '../interceptors/auth.interceptor';
import { AuthService } from './auth.service';
import { LoanService } from './loan.service';

function fakeToken(role: string, expSeconds = Date.now() / 1000 + 3600): string {
  const payload = btoa(JSON.stringify({ sub: 'user-1', role, exp: expSeconds }));
  return `header.${payload}.signature`;
}

describe('AuthService & LoanService', () => {
  let http: HttpTestingController;
  let auth: AuthService;
  let loans: LoanService;
  const api = environment.apiUrl;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({
      providers: [
        provideRouter([{ path: '**', children: [] }]),
        provideHttpClient(withInterceptors([authInterceptor])),
        provideHttpClientTesting(),
      ],
    });
    http = TestBed.inject(HttpTestingController);
    auth = TestBed.inject(AuthService);
    loans = TestBed.inject(LoanService);
  });

  afterEach(() => http.verify());

  function loginAs(role: string) {
    auth.login({ loginIdentifier: 'a@b.com', password: 'secret' }).subscribe();
    http
      .expectOne(`${api}/auth/login`)
      .flush({ accessToken: fakeToken(role), tokenType: 'Bearer ', expiresIn: 3600000 });
  }

  it('creates a user', () => {
    auth.createUser({ role: 'CUSTOMER', loginIdentifier: 'a@b.com', password: 'secret' }).subscribe();
    const req = http.expectOne(`${api}/users`);
    expect(req.request.method).toBe('POST');
    expect(req.request.headers.has('Authorization')).toBe(false);
    req.flush({ id: '1', role: 'CUSTOMER', loginIdentifier: 'a@b.com', active: true });
  });

  it('logs in, stores the token and exposes the role', () => {
    loginAs('ADMIN');
    expect(auth.isAuthenticated()).toBe(true);
    expect(auth.isAdmin()).toBe(true);
    expect(localStorage.getItem('makers.accessToken')).toBeTruthy();
  });

  it('creates a loan with the bearer token', () => {
    loginAs('CUSTOMER');
    loans.createLoan({ amount: 1000 }).subscribe();
    const req = http.expectOne(`${api}/loans`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ amount: 1000 });
    expect(req.request.headers.get('Authorization')).toMatch(/^Bearer header\./);
    req.flush({});
  });

  it('lists loans and updates a loan status as admin', () => {
    loginAs('ADMIN');
    loans.getAllLoans().subscribe();
    expect(http.expectOne(`${api}/admin/loans`).request.method).toBe('GET');

    loans.updateLoanStatus('loan-1', 'APPROVED').subscribe();
    const req = http.expectOne(`${api}/admin/loans/loan-1/status`);
    expect(req.request.method).toBe('PATCH');
    expect(req.request.body).toEqual({ status: 'APPROVED' });
  });

  it('logs out on 401', () => {
    loginAs('ADMIN');
    loans.getAllLoans().subscribe({ error: () => {} });
    http.expectOne(`${api}/admin/loans`).flush('', { status: 401, statusText: 'Unauthorized' });
    expect(auth.isAuthenticated()).toBe(false);
  });

  it('ends the session and redirects to /login when the token expires', () => {
    vi.useFakeTimers();
    try {
      const navigate = vi.spyOn(TestBed.inject(Router), 'navigate').mockResolvedValue(true);
      auth.login({ loginIdentifier: 'a', password: 'b' }).subscribe();
      http
        .expectOne(`${api}/auth/login`)
        .flush({ accessToken: fakeToken('CUSTOMER', Date.now() / 1000 + 60), tokenType: 'Bearer ', expiresIn: 60000 });
      expect(auth.isAuthenticated()).toBe(true);

      vi.advanceTimersByTime(60_000);

      expect(auth.isAuthenticated()).toBe(false);
      expect(auth.getToken()).toBeNull();
      expect(navigate).toHaveBeenCalledWith(['/login'], { state: { expired: true } });
    } finally {
      vi.useRealTimers();
    }
  });

  it('ignores expired tokens', () => {
    auth.login({ loginIdentifier: 'a', password: 'b' }).subscribe();
    http
      .expectOne(`${api}/auth/login`)
      .flush({ accessToken: fakeToken('ADMIN', 1), tokenType: 'Bearer ', expiresIn: 0 });
    expect(auth.isAuthenticated()).toBe(false);
    expect(auth.getToken()).toBeNull();
  });
});
