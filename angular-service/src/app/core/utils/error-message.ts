import { HttpErrorResponse } from '@angular/common/http';

export function errorMessage(error: unknown, fallback = 'Something went wrong'): string {
  if (!(error instanceof HttpErrorResponse)) return fallback;
  if (error.status === 0) return 'Cannot reach the server';
  if (typeof error.error === 'string' && error.error.trim()) return error.error;
  if (error.error?.message) return error.error.message;
  if (error.status === 403) return 'You are not allowed to do this';
  return fallback;
}
