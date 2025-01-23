import React from "react";
import styles from "./Card.module.css";

interface CardProps {
    title: string;
    value: string;
    subtitle?: string;
    progress?: number; // Between 0 to 100
}

export const Card: React.FC<CardProps> = ({ title, value, subtitle, progress }) => {
    return (
        <div className={styles.card}>
            <h4 className={styles.title}>{title}</h4>
            <p className={styles.value}>{value}</p>
            {subtitle && <p className={styles.subtitle}>{subtitle}</p>}
            {progress !== undefined && (
                <div className={styles.progressBarContainer}>
                    <div
                        className={styles.progressBar}
                        style={{ width: `${progress}%` }}
                    ></div>
                </div>
            )}
        </div>
    );
};

