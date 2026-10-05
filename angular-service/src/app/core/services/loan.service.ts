import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  AdminLoanApplicationResponse,
  CreateLoanRequest,
  LoanApplicationResponse,
  LoanStatus,
} from '../models/loan.model';

@Injectable({ providedIn: 'root' })
export class LoanService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.apiUrl;

  /** CUSTOMER: apply for a new loan. */
  createLoan(request: CreateLoanRequest): Observable<LoanApplicationResponse> {
    return this.http.post<LoanApplicationResponse>(`${this.baseUrl}/loans`, request);
  }

  /** ADMIN: list every loan application. */
  getAllLoans(): Observable<AdminLoanApplicationResponse[]> {
    return this.http.get<AdminLoanApplicationResponse[]>(`${this.baseUrl}/admin/loans`);
  }

  /** ADMIN: approve / reject / reset a loan application. */
  updateLoanStatus(id: string, status: LoanStatus): Observable<LoanApplicationResponse> {
    return this.http.patch<LoanApplicationResponse>(`${this.baseUrl}/admin/loans/${id}/status`, {
      status,
    });
  }
}
