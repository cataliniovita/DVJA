import React, {useEffect, useState} from "react";
import "./Spending.css";
import {Header} from "../header/Header";
import {Footer} from "../footer/Footer";
import {addNewSpending, deleteSpending, getSpendings, getUser} from "../api/ApiServices";
import {Transaction, User} from "../api/budgetPlannerTypes";

export const Spending: React.FC = () => {
    const [user, setUser] = useState<User>();
    const [loading, setLoading] = useState(false);
    const [amount, setAmount] = useState<string>("");
    const [category, setCategory] = useState<string>("");
    const [transactions, setTransactions] = useState<Transaction[]>([]);

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const data = await getUser();
                setUser(data);
            } catch (err) {
               console.log(err);
            } finally {
                setLoading(false);
            }
        };
        fetchUser();
    }, []);

    useEffect(() => {
        const fetchSpendings = async () => {
            try {
                if(user) {
                    const data = await getSpendings(parseInt(user.id));
                    setTransactions(data);
                }
            } catch (err) {
                console.log(err);
            } finally {
                setLoading(false);
            }
        };
        fetchSpendings();
    }, [user]);

    const handleAddTransaction = async (e: React.FormEvent) => {
        e.preventDefault();
        if (amount && category && user) {
            const newTransaction: Transaction = {
                date: new Date().toISOString().split("T")[0],
                category,
                amount: parseFloat(amount),
            };
            setTransactions((prev) => [...prev, newTransaction]);

            try {
                const spendingAmount = parseFloat(amount as string);

                await addNewSpending(parseInt(user.id), category, spendingAmount, new Date().toISOString().split("T")[0]);

            } catch (error) {
                console.log(`Error: ${error.message}`);
            }

            setAmount("");
            setCategory("");
        }
    };

    const handleDeleteTransaction = async(transactionId : number) => {
        setTransactions(
            transactions.filter((transaction) => transaction.id !== transactionId)
        );
        try {
            await deleteSpending(transactionId);
        } catch (error) {
            console.log(`Error: ${error.message}`);
        }
    }

    return (
        <div className="spending-container">
            <Header/>
            <div className="spending-page">
                <h2 className="title-spending">Record Your Spending</h2>
                <form className="spending-form" onSubmit={handleAddTransaction}>
                    <div className="form-group-spending">
                        <label htmlFor="amount">Amount Spent</label>
                        <input
                            type="number"
                            id="amount"
                            placeholder="Enter amount"
                            value={amount}
                            onChange={(e) => setAmount(e.target.value)}
                        />
                    </div>
                    <div className="form-group-spending">
                        <label htmlFor="category">Category</label>
                        <select
                            id="category"
                            value={category}
                            onChange={(e) => setCategory(e.target.value)}
                        >
                            <option value="">Select category</option>
                            <option value="Food">Food</option>
                            <option value="Transportation">Transportation</option>
                            <option value="Entertainment">Entertainment</option>
                            <option value="Utilities">Utilities</option>
                            <option value="Other">Other</option>
                        </select>
                    </div>
                    <button type="submit" className="submit-button-spending">
                        Record Spending
                    </button>
                </form>
                <h3 className="subtitle-spending">Recent Transactions</h3>
                <table className="transactions-table-spending">
                    <thead>
                    <tr>
                        <th>Date</th>
                        <th>Category</th>
                        <th>Amount</th>
                        <th>Delete button</th>
                    </tr>
                    </thead>
                    <tbody>
                    {transactions.map((transaction, index) => (
                        <tr key={index}>
                            <td>{transaction.date}</td>
                            <td>{transaction.category}</td>
                            <td>${transaction.amount.toFixed(2)}</td>
                            <td>
                                <button
                                    className="delete-button"
                                    onClick={() => handleDeleteTransaction(transaction?.id || index)}
                                >
                                    Delete
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
            <Footer/>
        </div>
    );
};

