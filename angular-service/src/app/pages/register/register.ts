import { Component, inject, signal } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Role } from '../../core/models/user.model';
import { AuthService } from '../../core/services/auth.service';
import { errorMessage } from '../../core/utils/error-message';

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './register.html',
})
export class Register {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  protected readonly roles: Role[] = ['CUSTOMER', 'ADMIN'];
  protected readonly form = inject(NonNullableFormBuilder).group({
    loginIdentifier: ['', Validators.required],
    password: ['', [Validators.required, Validators.minLength(6)]],
    role: ['CUSTOMER' as Role, Validators.required],
  });
  protected readonly loading = signal(false);
  protected readonly error = signal<string | null>(null);

  protected submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading.set(true);
    this.error.set(null);

    this.auth.createUser(this.form.getRawValue()).subscribe({
      next: () => this.router.navigate(['/login'], { state: { registered: true } }),
      error: (err) => {
        this.error.set(err.status === 409 ? 'That user already exists' : errorMessage(err));
        this.loading.set(false);
      },
    });
  }
}
