export type Role = 'ADMIN' | 'CUSTOMER';

export interface CreateUserRequest {
  role: Role;
  loginIdentifier: string;
  password: string;
}

export interface UserResponse {
  id: string;
  role: Role;
  loginIdentifier: string;
  active: boolean;
}

export interface LoginRequest {
  loginIdentifier: string;
  password: string;
}

export interface LoginResponse {
  accessToken: string;
  tokenType: string;
  expiresIn: number;
}

export interface SessionUser {
  userId: string;
  role: Role;
  expiresAt: number;
}
