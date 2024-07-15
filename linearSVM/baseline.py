import warnings
import numpy as np
import pandas as pd
from sklearn.svm import SVC
from imblearn.over_sampling import SMOTE
from sklearn.exceptions import ConvergenceWarning
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, f1_score
from sklearn.preprocessing import LabelEncoder, MinMaxScaler

pd.set_option('display.max_columns', None)
warnings.filterwarnings("ignore", category=ConvergenceWarning)

random_state = 78
scaling_factor = 2 ** 20

df = pd.read_csv('healthcare-dataset-stroke-data.csv', delimiter=',', encoding='utf-8')
df['bmi'] = df['bmi'].fillna(round(df['bmi'].median(), 2))
df.drop('id', axis=1, inplace=True)

categorical = ['gender', 'ever_married', 'work_type', 'Residence_type', 'smoking_status']
numerical = ['age', 'bmi', 'avg_glucose_level']

le = LabelEncoder()
for col in categorical:
    df[col] = le.fit_transform(df[col])

scaler = MinMaxScaler()
df[numerical] = scaler.fit_transform(df[numerical])
df[categorical] = scaler.fit_transform(df[categorical])

df['stroke'] = df['stroke'].replace({0: -1})

X = df.drop('stroke', axis=1)
y = df['stroke']

smote = SMOTE(random_state=random_state)
X_resample, y_resample = smote.fit_resample(X, y)

X_resample *= scaling_factor
X_resample = X_resample.astype(int)

pos_idx = np.where(y_resample == 1)[0]
neg_idx = np.where(y_resample == -1)[0]

np.random.seed(random_state)
pos_samples = np.random.choice(pos_idx, size=1500, replace=False)
neg_samples = np.random.choice(neg_idx, size=1500, replace=False)

selected_samples = np.concatenate((pos_samples, neg_samples))
X_selected = X_resample.loc[selected_samples]
y_selected = y_resample.loc[selected_samples]

X_train, X_test, y_train, y_test = train_test_split(X_selected, y_selected, test_size=0.1, shuffle=True,
                                                    random_state=random_state)

clf = SVC(kernel='linear', random_state=random_state, max_iter=800)
clf.fit(X_train, y_train)
y_preds = clf.predict(X_test)

accuracy = accuracy_score(y_test, y_preds)
print(f"Accuracy on test set: {accuracy * 100:.2f}%")
f1 = f1_score(y_test, y_preds)
print(f"F1 score on test set: {f1 * 100:.2f}%\n")

train_data = X_train.assign(stroke=y_train)
test_data = X_test.assign(stroke=y_test)

train_data.to_csv('train_data.csv', index=False)
test_data.to_csv('test_data.csv', index=False)
