import { Component, OnInit, inject, signal } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { AdminLoanApplicationResponse, LoanStatus } from '../../core/models/loan.model';
import { LoanService } from '../../core/services/loan.service';
import { errorMessage } from '../../core/utils/error-message';

@Component({
  selector: 'app-admin-loans',
  imports: [CurrencyPipe, DatePipe],
  templateUrl: './admin-loans.html',
  styleUrl: './admin-loans.css',
})
export class AdminLoans implements OnInit {
  private readonly loanService = inject(LoanService);

  protected readonly loans = signal<AdminLoanApplicationResponse[]>([]);
  protected readonly loading = signal(false);
  protected readonly updatingId = signal<string | null>(null);
  protected readonly error = signal<string | null>(null);

  ngOnInit(): void {
    this.load();
  }

  protected load(): void {
    this.loading.set(true);
    this.error.set(null);
    this.loanService.getAllLoans().subscribe({
      next: (loans) => {
        this.loans.set(loans);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(errorMessage(err, 'Could not load loan applications'));
        this.loading.set(false);
      },
    });
  }

  protected setStatus(loan: AdminLoanApplicationResponse, status: LoanStatus): void {
    this.updatingId.set(loan.id);
    this.error.set(null);
    this.loanService.updateLoanStatus(loan.id, status).subscribe({
      next: (updated) => {
        this.loans.update((list) =>
          list.map((l) => (l.id === updated.id ? { ...l, status: updated.status } : l)),
        );
        this.updatingId.set(null);
      },
      error: (err) => {
        this.error.set(errorMessage(err, 'Could not update the loan status'));
        this.updatingId.set(null);
      },
    });
  }
}
