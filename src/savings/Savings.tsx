import React, {useEffect, useState} from 'react';
import './Savings.css';
import {Header} from "../header/Header";
import {Footer} from "../footer/Footer";
import {addNewSaving, addNewSpending, contributeToSaving, getSavings, getSpendings, getUser} from "../api/ApiServices";
import {Goal, User} from "../api/budgetPlannerTypes";

export const Savings: React.FC = () => {
    const [user, setUser] = useState<User | null>(null);
    const [goals, setGoals] = useState<Goal[]>([]);
    const [contributions, setContributions] = useState<(number | string)[]>([]);
    const [newGoalName, setNewGoalName] = useState('');
    const [newGoalTarget, setNewGoalTarget] = useState<number | string>('');

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
    }, [user, goals]);

    const handleAddGoal = async () => {
        if (newGoalName && newGoalTarget && user) {
            try {
                await addNewSaving(parseInt(user.id), newGoalName, Number(newGoalTarget));
                setContributions([...contributions, ""]);
                setGoals([...goals, { goalName: newGoalName, targetAmount: Number(newGoalTarget), savedAmount: 0 }]);
                setNewGoalName('');
                setNewGoalTarget('');
            } catch (error) {
                console.log(`Error: ${error.message}`);
            }
        }
    };

    const handleContribute = async (index: number, amount: string) => {
        if (parseInt(amount) <= 0) return;
        const updatedGoals = [...goals];
        updatedGoals[index].savedAmount += parseInt(amount);
        try {
            await contributeToSaving(updatedGoals[index].id || index, parseInt(amount));
            setGoals(updatedGoals);
            const updatedContributions = [...contributions];
            updatedContributions[index] = ""; // Clear the input field for this goal
            setContributions(updatedContributions);
        } catch (err) {
            console.log(err);
        }
    };

    const handleContributionChange = (index: number, value: string) => {
        const updatedContributions = [...contributions];
        updatedContributions[index] = value; // Update only the specific goal's contribution
        setContributions(updatedContributions);
    };

    // const handleDeleteSaving = async(savingsId: number) => {
    //     try {
    //         await deleteSaving(savingsId);
    //     }
    //     catch (err) {
    //         console.log(err);
    //     }
    // }

    return (
        <div className="savings-container">
        <Header/>
            <div className="savings-goals">
                <h1>Add New Savings Goal</h1>
                <div className="add-goal">
                    <input
                        type="text"
                        placeholder="Enter goal name"
                        value={newGoalName}
                        onChange={(e) => setNewGoalName(e.target.value)}
                    />
                    <input
                        type="number"
                        placeholder="Enter target amount"
                        value={newGoalTarget}
                        onChange={(e) => setNewGoalTarget(e.target.value)}
                    />
                    <button onClick={handleAddGoal}>+ Add Goal</button>
                </div>

                {goals.map((goal, index) => (
                    <div key={index} className="goal">
                        <div className="savings-header">
                            <h2>{goal.goalName}</h2>
                            {/*<button className="delete-button-saving"*/}
                            {/*        onClick={() => handleDeleteSaving(goal.id || index)}>X</button>*/}
                        </div>

                        <div className="progress-bar">
                            <div
                                className="progress"
                                style={{width: `${(goal.savedAmount / goal.targetAmount) * 100}%`}}
                            ></div>
                        </div>
                        <p>
                            ${goal.savedAmount.toFixed(2)} / ${goal.targetAmount.toFixed(2)}
                        </p>
                        <div className="contribute" key={index}>
                            <input
                                type="number"
                                key={index}
                                placeholder="Contribute amount"
                                value={contributions[index]}
                                onChange={(e) =>
                                    handleContributionChange(index, e.target.value)
                                }
                            />
                            <button onClick={() => handleContribute(index, contributions[index] as string)}>Contribute</button>
                        </div>
                    </div>
                ))}

            </div>
            <Footer/>
        </div>
    );
};
