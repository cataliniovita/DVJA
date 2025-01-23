import React from "react";
import styles from "./Chart.module.css";

interface ChartProps {
    data: { label: string; value: number }[];
}

export const Chart: React.FC<ChartProps> = ({ data }: ChartProps) => {
    const maxValue = Math.max(...data.map((item) => item.value));


    return (
        <div className={styles.chartContainer}>
            {data.map((item, index) => (
                <div key={index} className={styles.barGroup}>
                    <div
                        className={styles.bar}
                        style={{
                            height: `${(item.value / maxValue) * 100}px`,
                        }}
                    >
                        <span className={styles.tooltiptext}>{item.value} $</span>
                    </div>

                    <p className={styles.label}>{item.label}</p>
                </div>
            ))}
        </div>
    );
};

