import React, { useState } from 'react';
import './Login.css';
import {Header} from "../header/Header";
import {useLocation, useNavigate} from "react-router-dom";
import {Footer} from "../footer/Footer";

export const Login: React.FC = () => {
    const [email, setEmail] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [error, setError] = useState<string>('');

    const [_, setCurrentPage] = useState(useLocation().pathname);
    const navigate = useNavigate();

    const navigateRegister = () => {
        setCurrentPage('/register');
        navigate('/register');
    };

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();

        if (email === '' || password === '') {
            setError('Please fill in both fields');
        } else {
            setError('');

            if (email === '' || password === '') {
                setError('Please fill in both fields');
            } else {
                setError('');

                // Make the API call to log and receive the token
                try {
                    const response = await fetch('http://localhost:8080/auth/login', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify({
                            email: email,
                            password: password,
                        }),
                    });

                    if (response.ok) {
                        const data = await response.json();
                        console.log(data);
                        const token = data.token;

                        // Store token in localStorage
                        localStorage.setItem('authToken', token);

                        alert('Logged in successfully!');
                        // Redirect user to dashboard (/) or protected route after successful login
                        navigate('/');

                    } else {
                        const errorData = await response.json();
                        setError(errorData.message || 'Invalid credentials');
                    }


                } catch (err) {
                    setError('An error occurred during login');
                }
            }
        }
    };

    return (
        <div className="login-container">
        <Header/>
        <div className="input-container">
            <div className="login">
                <div className="title-login">Login</div>
                    <form onSubmit={handleSubmit} className="login-form">
                        <div className="input-group">
                            <label htmlFor="email">Email</label>
                            <input
                                type="text"
                                id="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                                placeholder="Enter your email"
                            />
                        </div>

                        <div className="input-group">
                            <label htmlFor="password">Password</label>
                            <input
                                type="password"
                                id="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                                placeholder="Enter your password"
                            />
                        </div>

                        {error && <p className="error-message">{error}</p>}

                        <button type="submit" className="login-button">Login</button>
                        <p className="register-link">
                            Don't have an account?
                            <button type="button" className="register-button-link" onClick={navigateRegister}>
                                Register
                            </button>
                        </p>
                    </form>
                </div>
            </div>
            <Footer/>
        </div>
    );
};

