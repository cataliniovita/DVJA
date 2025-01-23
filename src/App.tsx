import React, {useEffect} from 'react';
import {BrowserRouter as Router, Route, Routes, useLocation} from 'react-router-dom';
import './App.css';
import {LandingPage} from "./landing-page/LandingPage";
import {Login} from "./login/Login";
import {Register} from "./register/Register";
import {Budget} from "./budget/Budget";
import {Spending} from "./spending/Spending";
import {Savings} from "./savings/Savings";
import {Dashboard} from "./dashboard/Dashboard";
import {MainPage} from "./main-page/MainPage";


const ScrollToTop = () => {
    // Extracts pathname property(key) from an object
    const { pathname } = useLocation();

    // Automatically scrolls to top whenever pathname changes
    useEffect(() => {
        window.scrollTo(0, 0);
    }, [pathname]);

    return <></>
}

function App() {
  return (
      <Router>
          <ScrollToTop />
          <Routes>
              <Route path="/" element={<MainPage />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/budget" element={<Budget />} />
              <Route path="/savings" element={<Savings />} />
              <Route path="/spending" element={<Spending />} />
          </Routes>
      </Router>
  );
}

export default App;
