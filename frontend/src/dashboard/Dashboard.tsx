import React, {useEffect, useState} from "react";
import {Card} from "./card/Card";
import {Chart} from "./chart/Chart";
import styles from "./Dashboard.module.css";
import {Header} from "../header/Header";
import {Footer} from "../footer/Footer";
import {Categories, ChartData, Goal, Transaction, User} from "../api/budgetPlannerTypes";
import {getBudgets, getSavings, getSpendings, getUser} from "../api/ApiServices";

export const Dashboard: React.FC = () => {
    const [user, setUser] = useState<User | null>(null);
    const [chartData, setChartData] = useState<ChartData[]>([]);
    const [totalBudget, setTotalBudget] = useState<number>(0);
    const [transactions, setTransactions] = useState<Transaction[]>([]);
    const [totalSpending, setTotalSpending] = useState<number>(0);
    const [progressBudget, setProgressBudget] = useState(0);
    const [goals, setGoals] = useState<Goal[]>([]);
    const [savings, setSavings] = useState(0);
    const [totalSavingsTarget, setTotalSavingsTarget] = useState(0);
    const [savingsProgress, setSavingsProgress] = useState(0);

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

    useEffect(() => {
        const fetchBudget = async () => {
            try {
                if(user) {
                    const data = await getBudgets(parseInt(user.id));
                    setTotalBudget(data[0].totalBudget);
                }
            } catch (err) {
                console.log(err);
            }
        };
        fetchBudget();
    }, [user]);

    useEffect(() => {
        const fetchSpendings = async () => {
            try {
                if(user) {
                    const data = await getSpendings(parseInt(user.id));
                    setTransactions(data);
                }
            }catch (err) {
            }
        }

        fetchSpendings();
    }, [user]);


    useEffect(() => {
        const sum = transactions.reduce((total, transaction) => total + transaction.amount,
            0);
        setTotalSpending(sum);

        const sumByCategory: Record<string, number> = transactions.reduce((acc, transaction) => {
                const { category, amount } = transaction;
                acc[category] = (acc[category] || 0) + amount;
                return acc;
            }, {} as Record<string, number>);

        setChartData([{label: Categories.food, value: sumByCategory[Categories.food] || 0},
            {label: Categories.entertainment, value: sumByCategory[Categories.entertainment] || 0},
            {label: Categories.transportations, value: sumByCategory[Categories.transportations] || 0},
            {label: Categories.utilities, value: sumByCategory[Categories.utilities] || 0},
            {label: Categories.other, value: sumByCategory[Categories.other] || 0}]);
    }, [transactions]);

    useEffect(() => {
        const percentage = (totalSpending * 100) / totalBudget;
        setProgressBudget(100 - percentage);
    }, [totalSpending, totalBudget]);

    useEffect(() => {
        const fetchSavings = async () => {
            try {
                if(user) {
                    const data = await getSavings(parseInt(user.id));
                    setGoals(data);
                }
            } catch (err) {
                console.log(err);
            }
        };
        fetchSavings();
    }, [user]);

    useEffect(() => {
        const fetchSavings = async () => {
            const sumTargets = goals.reduce(
                (total, goal) => total + goal.targetAmount,
                0
            );
            setTotalSavingsTarget(sumTargets);
            const sumSavings = goals.reduce( (total, goal) => total + goal.savedAmount,
                0)
            setSavings(sumSavings);
            const percentage = ((sumSavings * 100) / sumTargets).toFixed(2);
            setSavingsProgress(parseInt(percentage));
        };
        fetchSavings();
    }, [goals]);

    return (
        <div>
            <Header/>
            <div className={styles.container}>
                <h1 className={styles.title}>Your Financial Overview</h1>
                <div className={styles.cards}>
                    <Card
                        title="Monthly Budget"
                        value={`${totalSpending}$ / ${totalBudget}$`}
                        subtitle={`${progressBudget}% remaining`}
                        progress={100 - progressBudget}
                    />
                    <Card
                        title="Total Spending"
                        value={`${totalSpending}$`}
                        subtitle="Total spending this month"
                    />
                    <Card
                        title="Savings Goal"
                        value={`${savings}$ / ${totalSavingsTarget}$`}
                        subtitle={`${savingsProgress}% achieved`}
                        progress={savingsProgress}
                    />
                </div>
                <div className={styles.chartSection}>
                    <h2>Spending Breakdown</h2>
                    <Chart data={chartData} />
                </div>

            </div>
            <Footer/>
        </div>
    );
};

