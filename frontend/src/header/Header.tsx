import React, {useState, useEffect} from "react";
import logo from '../assets/logo.jpg';
import {useLocation, useNavigate} from "react-router-dom";
import './Header.css';

export const Header = () => {
    const [currentPage, setCurrentPage] = useState(useLocation().pathname);
    const [logged, setLogged] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        // Check if a valid token exists in localStorage or sessionStorage
        const token = localStorage.getItem('authToken'); // or sessionStorage.getItem('authToken')

        // If a token exists and is valid, mark the user as logged in
        if (token) {
            // You can further validate the token here if necessary (e.g., decoding and checking expiration)
            setLogged(true);
        } else {
            setLogged(false);
        }
    }, []);

    const navigateHome = () => {
        setCurrentPage('/');
        navigate('/');
    };

    const navigateLogin = () => {
        setCurrentPage('/login');
        navigate('/login');
    };

    const navigateRegister = () => {
        setCurrentPage('/register');
        navigate('/register');
    };

    const navigateLogout = () => {
        localStorage.removeItem('authToken');
        navigate('/');
        window.location.reload();
    };

    const navigateBudget = () => {
       setCurrentPage('/budget');
       navigate('/budget');
    };

    const navigateSavings = () => {
        setCurrentPage('/savings');
        navigate('/savings');
    };

    const navigateSpending = () => {
        setCurrentPage('/spending');
        navigate('/spending');
    };

    const Tab = (pageName: string, callFunction: any, path: string) => {
        return (
            <>
                <div
                    className={`tab ${currentPage === path ? 'tab-color' : '' }`}
                    onClick={callFunction}>
                    {pageName}
                </div>
            </>
        );
    };

    return (
        <>
            <nav className="navbar">
                <img className="logo" src={logo} alt="Logo" onClick={navigateHome}/>
                {!logged && <div className="nav-links">
                    <button className="login-button" onClick={navigateLogin}>Login</button>
                    <button className="login-button" onClick={navigateRegister}>Register</button>
                </div>}
                {logged &&
                    <div className='tabs'>
                        {Tab("Dashboard", navigateHome, '/')}
                        {Tab("Budget", navigateBudget, '/budget')}
                        {Tab("Spending", navigateSpending, '/spending')}
                        {Tab("Savings", navigateSavings, '/savings')}

                        <button className="logout-button" onClick={navigateLogout}>Logout</button>
                    </div>
                }
            </nav>
        </>)
}
