-- 1. Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 2. Create Enums
CREATE TYPE role_enum AS ENUM (
    'CUSTOMER',
    'ADMIN'
);

CREATE TYPE loan_status_enum AS ENUM (
    'PENDING',
    'APPROVED',
    'REJECTED'
);

-- 3. Users Table
CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                       role role_enum NOT NULL DEFAULT 'CUSTOMER',
                       login_identifier VARCHAR(150) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       active BOOLEAN NOT NULL DEFAULT TRUE,
                       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 4. Loan Applications Table
CREATE TABLE loan_applications (
                                   id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                   user_id UUID NOT NULL,
                                   status loan_status_enum NOT NULL DEFAULT 'PENDING',
                                   amount NUMERIC(15, 2) NOT NULL CHECK (amount > 0),
                                   created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    -- Foreign Key Constraint
                                   CONSTRAINT fk_loan_applications_user
                                       FOREIGN KEY (user_id)
                                           REFERENCES users(id)
                                           ON DELETE RESTRICT
);

-- 5. Indexes for Performance
CREATE INDEX idx_users_login_identifier ON users(login_identifier);
CREATE INDEX idx_loan_applications_user_id ON loan_applications(user_id);
CREATE INDEX idx_loan_applications_status ON loan_applications(status);

-- 6. Trigger to automatically update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
   NEW.updated_at = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_loan_applications_updated_at
    BEFORE UPDATE ON loan_applications
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();