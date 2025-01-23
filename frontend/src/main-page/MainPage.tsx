import React, {useEffect, useState} from 'react';
import {LandingPage} from "../landing-page/LandingPage";
import {User} from "../api/budgetPlannerTypes";
import {getUser} from "../api/ApiServices";
import {Dashboard} from "../dashboard/Dashboard";

export const MainPage: React.FC = () => {
    const [user, setUser] = useState<User | null>(null);

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const data = await getUser();
                setUser(data);
            } catch (err) {
                console.log(err);
            }
        };
        fetchUser();
    }, []);

    return (
        <>
            {user ? <Dashboard/> : <LandingPage/>}
        </>
    )
}