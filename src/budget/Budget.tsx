import React, {useEffect, useState} from "react";
import './Budget.css';
import {Footer} from "../footer/Footer";
import {Header} from "../header/Header";
import {Categories, Category, User} from "../api/budgetPlannerTypes";
import {addNewBudget, addNewSpending, getBudgets, getSpendings, getUser} from "../api/ApiServices";

export const Budget: React.FC = () => {
    const [user, setUser] = useState<User | null>(null);
    const [totalBudget, setTotalBudget] = useState(0);
    const [remainingBudget, setRemainingBudget] = useState<number>(0);
    const [budgetsPerCategory, setBudgetsPerCategory] = useState<Category[]>([]);
    const [budgets, setBudgets] = useState({
        total: "",
        food: "",
        transportation: "",
        entertainment: "",
        utilities: "",
        other: "",
    });
    const [sumCategories, setSumCategories] = useState(0);
    const [submitButton, setSubmitButton] = useState(false);
    const categoriesValues = Object.values(Categories)

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
                    setBudgetsPerCategory([{name: Categories.food, amount: data[0].foodBudget},
                        {name: Categories.entertainment, amount: data[0].entertainmentBudget},
                        {name: Categories.transportations, amount: data[0].transportBudget},
                        {name: Categories.utilities, amount: data[0].utilitiesBudget},
                        {name: Categories.other, amount: data[0].otherBudget}]);

                }
            } catch (err) {
                console.log(err);
            }
        };
        fetchBudget();
    }, [user]);

    useEffect(() => {
        const sum = (parseInt(budgets.food)  || 0) + (parseInt(budgets.entertainment) || 0) + (parseInt(budgets.other)  || 0) +
            (parseInt(budgets.utilities)  || 0) + (parseInt(budgets.transportation)  || 0);
        setSumCategories(sum);
    }, [budgets]);

    useEffect(() => {
        const sum = budgetsPerCategory.reduce(
            (total, category) => total + category.amount,
            0
        );
        setRemainingBudget(totalBudget - sum);
    }, [budgetsPerCategory]);

    useEffect(() => {
        if(parseInt(budgets.total) < sumCategories)
            setSubmitButton(true);
        else
            setSubmitButton(false);

        if (budgetsPerCategory == undefined || budgetsPerCategory.length == 0) {
            setSubmitButton(false);
        } else {
            setSubmitButton(true);
        }
    }, [budgets, sumCategories, budgetsPerCategory]);

    const handleAddBudgets = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if(user && budgets) {
            try {
                await addNewBudget(parseInt(user.id),
                    parseInt(budgets.total) || 0,
                    parseInt(budgets.food) || 0, parseInt(budgets.transportation) || 0,
                    parseInt(budgets.entertainment) || 0, parseInt(budgets.utilities) || 0,
                    parseInt(budgets.other) || 0);

                setBudgets({ total: "",
                    food: "",
                    transportation: "",
                    entertainment: "",
                    utilities: "",
                    other: ""});

            } catch (error) {
                console.log(`Error: ${error.message}`);
            }
        }
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setBudgets({ ...budgets, [e.target.name]: e.target.value });
    };

    return (
        <div>
            <Header/>
            <div className="budget-management">
                <h1 className="title-budget">Budget Management</h1>
                <div className="budget-overview">
                    <h2>Current Budget Overview</h2>
                    <p className="total-budget">
                        Total Budget: <span>${totalBudget.toFixed(2)}</span>
                    </p>
                    <div className="progress-bar-budget">
                        <div
                            className="progress-bar-filled-budget"
                            style={{
                                width: `${((totalBudget - remainingBudget) / totalBudget) * 100}%`,
                            }}
                        ></div>
                    </div>
                    <ul className="budget-breakdown">
                        {budgetsPerCategory && budgetsPerCategory.map((category, index) => (
                            <li key={index}>
                                {category.name}: <span>${category.amount}</span>
                            </li>
                        ))}
                    </ul>
                    <p className="remaining-budget">
                        Remaining: <span>${remainingBudget.toFixed(2)}</span>
                    </p>
                </div>
                <form onSubmit={handleAddBudgets} className="form-budget">
                    <div className="input-group">
                        <label htmlFor="total">Total Monthly Budget</label>
                        <input
                            type="text"
                            id="total"
                            name="total"
                            value={budgets.total}
                            onChange={handleChange}
                            placeholder="Enter total budget"
                        />
                    </div>
                    <h3>Category Budgets</h3>
                    {categoriesValues.map(
                        (category) => (
                            <div key={category}>
                                <label htmlFor={category.toLowerCase()}>{category}</label>
                                <input
                                    className="input-budget"
                                    type="text"
                                    id={category.toLowerCase()}
                                    name={category.toLowerCase()}
                                    value={(budgets as any)[category.toLowerCase()]}
                                    onChange={handleChange}
                                    placeholder={`Enter ${category.toLowerCase()} budget`}
                                />
                            </div>
                        )
                    )}
                    <div>
                        { parseInt(budgets.total) < sumCategories &&
                            <p style={{color: "red"}}>Category budgets exceeds total monthly budget</p>
                        }
                        <button type="submit" className="button-budget" disabled={submitButton}>
                            Set Budget
                        </button>
                    </div>

                </form>
            </div>
            <Footer/>
        </div>
    );
};
