export type LoanStatus = 'PENDING' | 'APPROVED' | 'REJECTED';

export interface CreateLoanRequest {
  amount: number;
}

export interface LoanApplicationResponse {
  id: string;
  userId: string;
  status: LoanStatus;
  amount: number;
  createdAt: string;
}

export interface AdminLoanApplicationResponse extends LoanApplicationResponse {
  loginIdentifier: string;
}

export interface UpdateLoanStatusRequest {
  status: LoanStatus;
}
