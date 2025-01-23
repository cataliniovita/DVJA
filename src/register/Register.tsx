import React, { useState } from 'react';
import './Register.css';
import {Header} from "../header/Header";
import {Footer} from "../footer/Footer";
import {useNavigate} from "react-router-dom"; // Importing CSS for RegisterPage

export const Register: React.FC = () => {
    // State variables for all form fields
    const [name, setName] = useState('');
    const [surname, setSurname] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();


    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        // Check if any required fields are missing
        if (!name || !surname || !email || !password || !confirmPassword) {
            setError('All fields are required');
            return;
        }

        // Check if passwords match
        if (password !== confirmPassword) {
            setError('Passwords do not match');
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/auth/signup', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    email,
                    password,
                    fullName: `${name} ${surname}`,
                }),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Failed to create account');
            }

            const result = await response.json();
        } catch (error) {
            console.error('Error during signup:', error);
        }

        setError('');
        alert('Account created successfully!');

        navigate('/login');
    };

    return (
        <div className="register">
            <Header/>
            <div className="register-container">
                <h2>Register</h2>
                <form onSubmit={handleSubmit} className="register-form">
                    <div className="input-group-reg">
                        <label htmlFor="name">First Name</label>
                        <input
                            type="text"
                            id="name"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            required
                        />
                    </div>

                    <div className="input-group-reg">
                        <label htmlFor="surname">Last Name</label>
                        <input
                            className=""
                            type="text"
                            id="surname"
                            value={surname}
                            onChange={(e) => setSurname(e.target.value)}
                            required
                        />
                    </div>

                    <div className="input-group-reg">
                        <label htmlFor="email">Email</label>
                        <input
                            type="email"
                            id="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>

                    <div className="input-group-reg">
                        <label htmlFor="password">Password</label>
                        <input
                            type="password"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>

                    <div className="input-group-reg">
                        <label htmlFor="confirm-password">Confirm Password</label>
                        <input
                            type="password"
                            id="confirm-password"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                        />
                    </div>

                    {error && <p className="error-message">{error}</p>}
                    <button type="submit" className="register-button">Register</button>
                </form>
            </div>
            <Footer/>
        </div>
    );
};

