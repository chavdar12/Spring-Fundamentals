/** Builds a patient chart with a rounded BMI, a status and, for obese patients, a recommendation. */
export function personalBMI(name, age, weight, height) {
    const BMI = Math.round(weight / (height / 100) ** 2);
    const status = BMI < 18.5 ? 'underweight' : BMI < 25 ? 'normal' : BMI < 30 ? 'overweight' : 'obese';
    const chart = { name, personalInfo: { age, weight, height }, BMI, status };
    if (status === 'obese') {
        chart.recommendation = 'admission required';
    }
    return chart;
}
