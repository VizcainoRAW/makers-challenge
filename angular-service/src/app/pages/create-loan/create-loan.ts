import { Component, inject, signal } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { LoanApplicationResponse } from '../../core/models/loan.model';
import { LoanService } from '../../core/services/loan.service';
import { errorMessage } from '../../core/utils/error-message';

@Component({
  selector: 'app-create-loan',
  imports: [ReactiveFormsModule, CurrencyPipe, DatePipe],
  templateUrl: './create-loan.html',
})
export class CreateLoan {
  private readonly loans = inject(LoanService);

  protected readonly amount = new FormControl<number | null>(null, [
    Validators.required,
    Validators.min(0.01),
  ]);
  protected readonly loading = signal(false);
  protected readonly error = signal<string | null>(null);
  protected readonly created = signal<LoanApplicationResponse | null>(null);

  protected submit(): void {
    if (this.amount.invalid) {
      this.amount.markAsTouched();
      return;
    }
    this.loading.set(true);
    this.error.set(null);
    this.created.set(null);

    this.loans.createLoan({ amount: this.amount.value! }).subscribe({
      next: (loan) => {
        this.created.set(loan);
        this.amount.reset();
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(errorMessage(err, 'Could not submit the loan application'));
        this.loading.set(false);
      },
    });
  }
}
