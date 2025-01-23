import React from "react";

import "./LandingPage.css";
import {Header} from "../header/Header";
import {Footer} from "../footer/Footer";

export const LandingPage: React.FC = () => {
    return (
        <div className="landing-page">
            <Header/>
            {/* Main Content */}
            <div className="main-content">
                <h1>Take Control of Your Finances</h1>
                <p>
                    Our advanced budget planner helps you manage your money, track
                    spending, and achieve your financial goals.
                </p>
            </div>

            <div className="container">
            </div>

            {/* Footer */}
            <Footer/>
        </div>
    );
};

