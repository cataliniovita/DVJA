export type User =  {
    id: string;
    name: string;
    email: string;
    // Add more fields based on your API response
}

export type ChartData = {
    label: string;
    value: number
}

export type Category = {
    name: string;
    amount: number;
}

export enum Categories {
    food = "Food",
    transportations = "Transportation",
    entertainment ="Entertainment",
    utilities  = "Utilities",
    other  = "Other"
}

export type Goal = {
    goalName: string;
    id?: number;
    savedAmount: number;
    targetAmount: number;
}

export type Transaction = {
    id?: number;
    date: string;
    category: string;
    amount: number;
}